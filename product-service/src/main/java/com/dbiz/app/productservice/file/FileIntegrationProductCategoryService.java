package com.dbiz.app.productservice.file;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.PcTerminalAccess;
import com.dbiz.app.productservice.domain.ProductCategory;
import com.dbiz.app.productservice.repository.PcTerminalAccessRepository;
import com.dbiz.app.productservice.repository.ProductCategoryRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final PcTerminalAccessRepository pcTerminalAccessRepository;

    private final static String GROUP_ID = "file-import-group";
    private final static String IMPORT_PRODUCT_CATEGORY_TOPIC = "import-file-product-category-topic";
    private final static String UPDATE_IMPORT_TOPIC = "update-import-file-topic";
    private final static String defaultN = "0";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;

    private final JdbcTemplate jdbcTemplate;
    private final QueryEngine queryEngine;
    private final MessageSource messageSource;

    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_PRODUCT_CATEGORY_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void integrateProductCategory(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("UserService: Received message from Kafka topic: " + IMPORT_PRODUCT_CATEGORY_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            String sqlProduct = "SELECT * FROM pos.d_i_file_product_category ORDER BY row_number";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlProduct);
            log.info(results.stream()
                    .map(Map::toString)
                    .collect(Collectors.joining("\n\t")));

            Map<String, String> headers = getHeaders(fileIEDto);
            List<String> errorMessages = new ArrayList<>();
            String errorMessage;
            boolean hasIntegrate = false;
            for (Map<String, Object> row : results) {
                int index = ParseHelper.INT.parse(row.get("row_number"));
                String name = ParseHelper.STRING.parse(row.get("pr_category_name"));
                String code = ParseHelper.STRING.parse(row.get("pr_category_code"));
                String parentCode = ParseHelper.STRING.parse(row.get("parent_name"));
                String isMenu = defaultN.equals(ParseHelper.STRING.parse(row.get("is_menu"))) ? "N" : "Y" ;
                String isParent = ParseHelper.STRING.parse(row.get("is_parent")) != null ? "Y" : "N";
                String isActive = defaultN.equals(ParseHelper.STRING.parse(row.get("is_prcategory_active"))) ? "N" : "Y";

                //Xu ly thong tin product category
                ProductCategory productCategory = productCategoryRepository.findByCode(code);
                if(productCategory != null){
                    productCategory.setName(name);
                    productCategory.setIsMenu(isMenu);
                    productCategory.setIsActive(isActive);
                }else{
                    productCategory = ProductCategory.builder()
                            .code(code)
                            .isMenu(isMenu)
                            .name(name)
                            .orgId(0)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    productCategory.setIsActive(isActive);
                }

                //Xư ly parent
                if("Y".equals(isParent)){
                    productCategory.setIsSummary("Y"); // có cha
                    ProductCategory productCategoryParent = productCategoryRepository.findByCode(parentCode);
                    if(productCategoryParent != null)
                        productCategory.setProductCategoryParentId(productCategoryParent.getId());
                    else{
                        errorMessage = messageSource.getMessage(
                                "file.validation.missing_parent",
                                new Object[]{index},
                                LocaleContextHolder.getLocale());
                        errorMessages.add(errorMessage);
                        fileIEDto.getErrorRows().add(index);
                        if ("N".equals(fileIEDto.getIsSkipErrors())) { // stop integrate if case STOP
                            break;
                        } else {
                            continue;
                        }
                    }
                }

                //validation();
                errorMessage = validateProductCategory(productCategory, headers, index);
                if(errorMessage == null) {
                    productCategory = productCategoryRepository.save(productCategory);
                    log.info("product category {}", productCategory);
                    //Assign org
                    pcTerminalAccess(productCategory.getId(), fileIEDto);
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
            log.error("Caught error in integrateProductCategory(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in integrateProductCategory(): " + e.getMessage() :
                    "Caught error in integrateProductCategory(): " + e.getMessage()
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

    private String validateProductCategory(ProductCategory productCategory, Map<String, String> headers, Integer index) {
        if (productCategory.getName() == null || productCategory.getName().isEmpty()) {
            return messageSource.getMessage(
                    "file.validation.missing_prod_cat_name",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
        }

//        if (productCategory.getCode() == null || productCategory.getCode().isEmpty()) {
//            return messageSource.getMessage(
//                    "file.validation.empty",
//                    new Object[]{index, headers.get("pr_category_code")},
//                    LocaleContextHolder.getLocale());
//        }
        return null;
    }

    private void pcTerminalAccess(Integer productCategoryId, FileIEDto fileIEDto){
        log.info("Assign product category in pos terminal");

        pcTerminalAccessRepository.updateIsActiveByTenantIdAndProductCategoryId("N", AuditContext.getAuditInfo().getTenantId(), productCategoryId);
        fileIEDto.getOrgIds().forEach(orgId -> {
            List<Integer> pcTerminalAccessId = getTerminalIdAccessInOrg(orgId);

            pcTerminalAccessId.forEach(terminalId -> {
                PcTerminalAccess pcCheck = pcTerminalAccessRepository.findByProductCategoryIdAndOrgIdAndPosTerminalId(productCategoryId, orgId, terminalId);
                if (pcCheck == null) {
                    PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
                            .productCategoryId(productCategoryId)
                            .posTerminalId(terminalId)
                            .orgId(orgId)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    pcTerminalAccess.setIsActive("Y");
                    pcTerminalAccessRepository.save(pcTerminalAccess);
                } else {
                    pcCheck.setIsActive("Y");
                    pcTerminalAccessRepository.save(pcCheck);
                }
            });
        });
    }

    //Lấy ra các diem ban tai 1 chi nhanh
    private List<Integer> getTerminalIdAccessInOrg(Integer orgId){
        String sql = "SELECT d_pos_terminal_id FROM pos.d_pos_org_access where is_active = 'Y' and d_org_id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,orgId);
        List<Integer> posTerminalAccessId = new ArrayList<>();
        for(Map<String, Object> item: result){
            posTerminalAccessId.add(ParseHelper.INT.parse(item.get("d_pos_terminal_id")));
        }

        log.info("ID terminal access Org {}", posTerminalAccessId);

        return posTerminalAccessId;
    }
}
