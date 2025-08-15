package com.dbiz.app.userservice.file;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.dbiz.app.userservice.constant.AppConstant;
import com.dbiz.app.userservice.domain.Customer;
import com.dbiz.app.userservice.domain.PartnerGroup;
import com.dbiz.app.userservice.repository.CustomerRepository;
import com.dbiz.app.userservice.repository.PartnerGroupRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.FileTemplateDto;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationUserService {

    private final JdbcTemplate jdbcTemplate;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final PartnerGroupRepository partnerGroupRepository;
    private final CustomerRepository customerRepository;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final QueryEngine queryEngine;
    private final static String GROUP_ID = "file-import-group";
    private final static String IMPORT_CUSTOMER_TOPIC = "import-file-customer-topic";
    private final static String IMPORT_PARTNER_GROUP_TOPIC = "import-file-partner-group-topic";
    private final static String UPDATE_IMPORT_TOPIC = "update-import-file-topic";
    private final static String defaultN = "0";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_PARTNER_GROUP_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void integratePartnerGroup(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("integratePartnerGroup():\nSave interface to main table: Received message from Kafka topic: " + IMPORT_PARTNER_GROUP_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            String sql = "SELECT * FROM pos.d_i_file_partner_group ORDER BY row_number";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            log.info(results.stream()
                    .map(Map::toString)
                    .collect(Collectors.joining("\n\t")));

            Map<String, String> headers = getHeaders(fileIEDto);
            List<String> errorMessages = new ArrayList<>();
            boolean hasIntegrate = false;
            for (Map<String, Object> row : results) {
                int index = ParseHelper.INT.parse(row.get("row_number"));
                String docNo = row.get("grp_code") == null ?
                        "BP" + (partnerGroupRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth() :
                        ParseHelper.STRING.parse(row.get("grp_code"));
                PartnerGroup partnerGroupSave = PartnerGroup.builder()
                        .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                        .orgId(0)
                        .groupCode(docNo)
                        .groupName(ParseHelper.STRING.parse(row.get("grp_name")))
                        .isCustomer(defaultN.equals(ParseHelper.STRING.parse(row.get("is_cus_grp"))) ? "N" : "Y") // default Y unless defaultN
                        .build();
                partnerGroupSave.setIsActive(defaultN.equals(ParseHelper.STRING.parse(row.get("is_grp_active"))) ? "N" : "Y");
                log.info("partnerGroupSave: {}", partnerGroupSave);

                PartnerGroup partnerGroup = partnerGroupRepository.findByGroupCode(partnerGroupSave.getGroupCode());
                if (partnerGroup != null) {
                    modelMapper.map(partnerGroupSave, partnerGroup);
                } else {
                    partnerGroup = partnerGroupSave;
                }

                String errorMessage = validatePartner(partnerGroup, headers, index);
                if (errorMessage == null) {
                    partnerGroupRepository.save(partnerGroup);
                    hasIntegrate = true;
                } else {
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
            log.error("Caught error in integratePartnerGroup(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in integratePartnerGroup(): " + e.getMessage() :
                    "Caught error in integratePartnerGroup(): " + e.getMessage()
            );
            kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
        }

    }

    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_CUSTOMER_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void integrateCustomer(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("integrateCustomer():\nSave interface to main table: Received message from Kafka topic: " + IMPORT_CUSTOMER_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            String sql = "SELECT * FROM pos.d_i_file_customer ORDER BY row_number";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            log.info(results.stream()
                    .map(Map::toString)
                    .collect(Collectors.joining("\n\t")));

            Integer partnerGroupId;
            Map<String, String> headers = getHeaders(fileIEDto);
            List<String> errorMessages = new ArrayList<>();
            boolean hasIntegrate = false;
            for(Map<String, Object> row : results) {
                int index = ParseHelper.INT.parse(row.get("row_number"));
                if (!partnerGroupRepository.existsByGroupName(ParseHelper.STRING.parse(row.get("cus_group_name")))) {
                    String docNo = "BP" + (partnerGroupRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth();
                    PartnerGroup partnerGroup = PartnerGroup.builder()
                            .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                            .orgId(0)
                            .groupCode(docNo)
                            .groupName(ParseHelper.STRING.parse(row.get("cus_group_name")))
                            .isCustomer("Y")
                            .build();
                    partnerGroupRepository.save(partnerGroup);
                    partnerGroupId = partnerGroup.getId();
                } else {
                    partnerGroupId = partnerGroupRepository.findByGroupName(ParseHelper.STRING.parse(row.get("cus_group_name"))).getId();
                }

                String docNo = row.get("cus_code") == null ?
                        "CUS" + (customerRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth() :
                        ParseHelper.STRING.parse(row.get("cus_code"));
                Customer customerSave = Customer.builder()
                        .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                        .code(docNo)
                        .name(ParseHelper.STRING.parse(row.get("cus_name")))
                        .phone1(ParseHelper.STRING.parse(row.get("cus_phone")))
                        .partnerGroupId(partnerGroupId)
                        .partnerName(ParseHelper.STRING.parse(row.get("cus_group_name")))
                        .isCustomerType(defaultN.equals(ParseHelper.STRING.parse(row.get("is_personal_cus"))) ? "N" : "Y") // default Y unless defaultN
                        .isDebt(ParseHelper.STRING.parse(row.get("is_debt_managed")) != null ? "Y" : "N") // default N unless not null
                        .debitAmount(ParseHelper.BIGDECIMAL.parse(row.get("debt_amount")))
                        .build();
                customerSave.setIsActive(defaultN.equals(ParseHelper.STRING.parse(row.get("is_cus_active"))) ? "N" : "Y"); // default Y unless defaultN
                log.info("customerSave: {}", customerSave);

                Customer customer = customerRepository.findByCode(customerSave.getCode());
                if (customer != null) {
                    modelMapper.map(customerSave, customer);
                } else {
                    customer = customerSave;
                }

                String errorMessage = validateCustomer(customer, headers, index);
                if (errorMessage == null) {
                    customerRepository.save(customer);
                    hasIntegrate = true;
                } else {
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
            log.error("Caught error in integrateCustomer(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in integrateCustomer(): " + e.getMessage() :
                    "Caught error in integrateCustomer(): " + e.getMessage()
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
        }
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    private String validatePartner(PartnerGroup partnerGroup, Map<String, String> headers, Integer index) {
        if (partnerGroup.getGroupName() == null) {
            return messageSource.getMessage(
                    "file.validation.missing_partner_name",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
        }

        return null;
    }
    private String validateCustomer(Customer customer, Map<String, String> headers, Integer index) {
        if (customer.getName() == null) {
            return messageSource.getMessage(
                    "file.validation.missing_customer_name",
                     new Object[]{index},
                    LocaleContextHolder.getLocale());
        }

        return null;
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
                    headerNames.put(ParseHelper.STRING.parse(item.get(("Column_Name"))), header);
                }
                log.info("{}", AuditContext.getAuditInfo().getLanguage());
                log.info("headerNames: {}", headerNames);

                return headerNames;
            }
        } catch (Exception e) {
            log.error("Caught error in getHeaders(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in getHeaders(): " + e.getMessage() :
                    "Caught error in getHeaders(): " + e.getMessage()
            );
            kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
        }

        return null;
    }

}
