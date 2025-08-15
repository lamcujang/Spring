package com.dbiz.app.productservice.file;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.domain.PriceListOrg;
import com.dbiz.app.productservice.repository.PriceListOrgRepository;
import com.dbiz.app.productservice.repository.PriceListRepository;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.FileTemplateDto;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationPriceListService {
    private final PriceListRepository priceListRepository;
    private final PriceListOrgRepository priceListOrgRepository;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;

    private final static String GROUP_ID = "file-import-group";
    private final static String IMPORT_PRICE_LIST_TOPIC = "import-file-price-list-topic";
    private final static String UPDATE_IMPORT_TOPIC = "update-import-file-topic";
    private final static String defaultN = "0";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final JdbcTemplate jdbcTemplate;
    private final QueryEngine queryEngine;

    private final MessageSource messageSource;

    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_PRICE_LIST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void integratePriceList(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("" +
                    "UserService: Received message from Kafka topic: " + IMPORT_PRICE_LIST_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            String sqlProduct = "SELECT * FROM pos.d_i_file_pricelist ORDER BY row_number";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlProduct);
            log.info(results.stream()
                    .map(Map::toString)
                    .collect(Collectors.joining("\n\t")));

            Map<String, String> headers = getHeaders(fileIEDto);
            List<String> errorMessages = new ArrayList<>();
            boolean hasIntegrate = false;
            for (Map<String, Object> row : results) {
                int index = ParseHelper.INT.parse(row.get("row_number"));
                String name = ParseHelper.STRING.parse(row.get("pricelist_name"));
                String code = ParseHelper.STRING.parse(row.get("pricelist_code"));
                PriceList priceListCheck = this.priceListRepository.findByNameAndAndCode(name, code);

                if(priceListCheck != null){// Case cap nhat
                    priceListCheck.setName(name);
                    priceListCheck.setFromDate(ParseHelper.INSTANT.parse(row.get("from_date")));
                    priceListCheck.setToDate(ParseHelper.INSTANT.parse(row.get("to_date")));
                    priceListCheck.setIsActive(defaultN.equals(ParseHelper.STRING.parse(row.get("is_pricelist_active"))) ? "N" : "Y");
                }else{ //Case insert
                    priceListCheck = PriceList.builder()
                            .orgId(0)
                            .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                            .name(name)
                            .fromDate(ParseHelper.INSTANT.parse(row.get("from_date")))
                            .toDate(ParseHelper.INSTANT.parse(row.get("to_date")))
                            .code(code)
                            .isSaleprice("Y")
                            .build();
                    priceListCheck.setIsActive(defaultN.equals(ParseHelper.STRING.parse(row.get("is_pricelist_active"))) ? "N" : "Y");
                }

                log.info("Price List: {}" , priceListCheck );

                String errorMessage =  validatePriceList(priceListCheck, headers, index);

                //assign org
                if(errorMessage == null) {
                    priceListCheck = priceListRepository.save(priceListCheck);
                    assignPriceList(priceListCheck.getId(), fileIEDto);
                    hasIntegrate = true;
                }else{
                    errorMessages.add(errorMessage);
                    fileIEDto.getErrorRows().add(index);
                    if ("N".equals(fileIEDto.getIsSkipErrors())) { // stop integrate if case STOP
                        break;
                    }
                }
            }

            if (!errorMessages.isEmpty()) {
                fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                        fileIEDto.getErrorMessage() + "<br>" + String.join("<br>", errorMessages) :
                        String.join("<br>", errorMessages));
            }
            // HasIntegrate && ( No Error || case SKIP )
            if (hasIntegrate && ( fileIEDto.getErrorMessage() == null || "Y".equals(fileIEDto.getIsSkipErrors()) )) {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.SUCCESS);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            } else {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            }

        } catch (Exception e) {
            log.error("Caught error in integrateProduct(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in integrateProduct(): " + e.getMessage() :
                    "Caught error in integrateProduct(): " + e.getMessage()
            );
            kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
        }
    }

    public void changeTenantAndLocale(FileIEDto dto) {
        try {
            int tenantNumbers = getTenantNumbers();
            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
                Map<Object, Object> configuredDataSources = dataSourceConfigService
                        .configureDataSources();

                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
            }
            if (dto.getTenantId() != 0) {
                dataSourceContextHolder.setCurrentTenantId(Long.valueOf(dto.getTenantId()));
            } else {
                dataSourceContextHolder.setCurrentTenantId(null);
            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, dto.getLanguage(), dto.getTenantId()));
            LocaleContextHolder.setLocale(Locale.forLanguageTag(dto.getLanguage()));

        } catch (Exception e) {
            log.error("Caught error in changeTenantAndLocale(): ", e);
            throw new PosException("Caught error in changeTenantAndLocale(): " + e.getMessage(), e);
        }
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    private Map<String, String> getHeaders(FileIEDto fileIEDto){
        FileTemplateDto fileTemplate;

        Parameter parameter = new Parameter();
        parameter.add("file_type", fileIEDto.getFileType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("object_type", fileIEDto.getObjectType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_file_template", parameter);

        try {
            if (rs.next()) {
                fileTemplate = FileTemplateDto.builder()
                        .templateJson(rs.getString("template_json"))
                        .iTableName(rs.getString("i_table_name"))
                        .name(rs.getString("name"))
                        .build();

                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> templateColumns = objectMapper.readValue(fileTemplate.getTemplateJson(), new TypeReference<List<Map<String, Object>>>() {});

                Map<String, String> headerNames = new HashMap<>();

                for (Map<String, Object> item : templateColumns) {
                    String header = ParseHelper.STRING.parse(item.get("Header_Name_" + AuditContext.getAuditInfo().getLanguage()));
                    headerNames.put( ParseHelper.STRING.parse(item.get(("Column_Name"))), header);
                }
                log.info("{}", AuditContext.getAuditInfo().getLanguage());
                log.info("headerNames: {}", headerNames);

                return headerNames;
            }
        } catch (Exception e) {
            log.error("Caught error in getHeaders(): ", e);
            throw new PosException("Caught error in getHeaders(): " + e.getMessage(), e);
        }

        return null;
    }

    private String validatePriceList(PriceList priceList, Map<String, String> headers, Integer index) {
        if (priceList.getName() == null || priceList.getName().isEmpty()) {
            return messageSource.getMessage(
                    "file.validation.missing_pricelist_name",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
        }

//        if (priceList.getCode() == null || priceList.getCode().isEmpty()) {
//            return messageSource.getMessage(
//                    "file.validation.empty",
//                    new Object[]{index, headers.get("pricelist_code")},
//                    LocaleContextHolder.getLocale());
//        }

        if (priceList.getFromDate().isAfter(priceList.getToDate())) {
            return messageSource.getMessage(
                    "file.validation.wrong_from_date",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
        }

        if (priceList.getToDate().isBefore(Instant.now())) {
            return messageSource.getMessage(
                    "file.validation.wrong_to_date",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
        }
        return null;
    }

    private void assignPriceList(Integer priceListId, FileIEDto fileIEDto){
        priceListOrgRepository.updateIsActiveByPricelistId(priceListId, "N");
        fileIEDto.getOrgIds().forEach(orgId -> {
            Optional<PriceListOrg> check = this.priceListOrgRepository.findByOrgIdAndPricelistId(orgId, priceListId);
            PriceListOrg priceListOrg;
            if(check.isEmpty()){
                priceListOrg = new PriceListOrg();
                priceListOrg.setOrgId(orgId);
                priceListOrg.setPricelistId(priceListId);
                priceListOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
            }else{
                priceListOrg = check.get();
                priceListOrg.setIsActive("Y");
            }
            this.priceListOrgRepository.save(priceListOrg);
            log.info("PricelistOrg: {}", priceListOrg);
            log.info("Update Pricelist: {} for Org: {}", priceListOrg.getPricelistId(), priceListOrg.getOrgId());
        });
    }
}
