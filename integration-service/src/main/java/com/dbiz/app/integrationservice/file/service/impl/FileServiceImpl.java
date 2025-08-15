package com.dbiz.app.integrationservice.file.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;

import com.dbiz.app.integrationservice.domain.ImportFileHistory;
import com.dbiz.app.integrationservice.file.service.FileService;
import com.dbiz.app.integrationservice.file.service.strategy.FileProcessorFactory;
import com.dbiz.app.integrationservice.file.service.strategy.FileProcessorStrategy;
import com.dbiz.app.integrationservice.repository.ImportFileHistoryRepository;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.dto.integrationDto.file.ImportFileHistoryDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.file.ImportFileHistoryReqDto;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.nio.file.Path;

import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileStorageServiceImpl fileStorageService;
    private final MessageSource messageSource;
    private final JdbcTemplate jdbcTemplate;
    private final ImportFileHistoryRepository importFileHistoryRepository;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final FileProcessorFactory fileProcessorFactory;
    private final static String GROUP_ID = "file-import-group";
    private final static String IMPORT_TOPIC = "import-file-topic";
    private final static String IMPORT_CUSTOMER_TOPIC = "import-file-customer-topic";
    private final static String IMPORT_PARTNER_GROUP_TOPIC = "import-file-partner-group-topic";
    private final static String IMPORT_PRODUCT_TOPIC = "import-file-product-topic";
    private final static String IMPORT_PRICE_LIST_TOPIC = "import-file-price-list-topic";
    private final static String IMPORT_PRICE_LIST_PRODUCT_TOPIC = "import-file-price-list-product-topic";
    private final static String IMPORT_PRODUCT_CATEGORY_TOPIC = "import-file-product-category-topic";
    private final static String UPDATE_IMPORT_TOPIC = "update-import-file-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final QueryEngine queryEngine;
    private final EntityManager entityManager;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Override
    public GlobalReponsePagination getImportFileHistory(ImportFileHistoryReqDto req) {
        Parameter parameter = new Parameter();
        parameter.add("file_name", req.getFileName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("object_type", req.getObjectType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("full_name", req.getFullName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("import_status", req.getImportStatus(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("import_date", Param.getBetweenParam(req.getFromDate(),req.getToDate()), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        parameter.add("is_active", "Y", Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        req.setSortBy("updated");
        req.setOrder("desc");
        ResultSet rs = queryEngine.getRecords( "d_import_file_history_v",
                parameter, req);

        List<ImportFileHistoryDto> data = new ArrayList<>();
        try{
            while (rs.next()) {
                ImportFileHistoryDto importFileHistoryDto = ImportFileHistoryDto.builder()
                        .id(rs.getInt("d_import_file_history_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .userId(rs.getInt("created_by"))
                        .fullName(rs.getString("full_name"))
                        .fileName(rs.getString("file_name"))
                        .isActive(rs.getString("is_active"))
                        .code(rs.getString("code"))
                        .importStatus(rs.getString("import_status"))
                        .importStatusName(rs.getString("import_status_name"))
                        .importDate(rs.getString("import_date"))
                        .objectType(rs.getString("object_type"))
                        .objectTypeName(rs.getString("object_type_name"))
                        .fileType(rs.getString("file_type"))
                        .errorMessage(rs.getString("error_message"))
                        .build();
                data.add(importFileHistoryDto);
            }
        }catch (Exception e){
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
        Pagination pagination = queryEngine.getPagination("d_import_file_history_v",
                parameter, req);
        log.info("Load pagination...");
        return GlobalReponsePagination.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

//    private String getObjectTypeName(String objectType) {
//        String sql =
//                "SELECT name FROM pos.d_reference_get_v " +
//                "WHERE name_reference = ? AND value = ?";
//        try {
//            return jdbcTemplate.queryForObject(sql, String.class, "File Object Type", objectType);
//        } catch (EmptyResultDataAccessException e) {
//            log.error("Expected 1 result, received 0: ", e);
//            return null;
//        } catch (IncorrectResultSizeDataAccessException e) {
//            log.error("Expected 1 result, received more than 1: ", e);
//            return null;
//        }
//    }

    @Override
    public GlobalReponse importFile(MultipartFile fileContent,
                                    String orgOrAll,
                                    List<Integer> orgIds,
                                    String fileCode,
                                    String fileType,
                                    String fileTail,
                                    String objectType,
                                    String isUpdateInv,
                                    String isUpdateCom,
                                    String isSkipErrors) {

        log.info("importFile():\nStore file on server, Save history, Send Kafka to indicateImportService() to save to interface in background");

//        MultipartFile fileContent = file.getFileContent();
        String fileName = fileContent.getOriginalFilename();

        ImportFileHistory history = saveImportFileHistory(orgIds.get(0), fileCode ,fileType, fileTail, objectType);

        String importId = history.getCode();

        history.setFileName(fileName);

//        fileName = fileName.concat("_" + importId);
        fileName = importId + "_" + fileName;

        // 1. Lưu file tạm (có thể lưu vào S3, thư mục local, v.v.)
        Path savedFilePath = fileStorageService.storeFile(fileContent, fileName);

        history.setFileUrl(savedFilePath.toString());
        importFileHistoryRepository.save(history);

        if ("ALL".equals(orgOrAll)) {
            List<Integer> allOrgIds = jdbcTemplate.queryForList("SELECT d_org_id FROM pos.d_org WHERE is_active = 'Y'", Integer.class);
            orgIds = allOrgIds;
        }

        // 3. Đẩy thông tin vào Kafka
        FileIEDto fileIEDto = FileIEDto.builder()
                .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                .orgOrAll(orgOrAll)
                .orgIds(orgIds)
                .language(AuditContext.getAuditInfo().getLanguage())
                .isUpdateInv(isUpdateInv)
                .isUpdateCom(isUpdateCom)
                .isSkipErrors(isSkipErrors)
                .fileName(fileName)
                .fileUrl(savedFilePath.toString())
                .fileType(fileType)
                .fileTail(fileTail)
                .objectType(objectType)
                .importStatus(AppConstant.ImportFileStatus.IN_PROGRESS)
                .build();
        log.info("fileIEDto():\n{}", fileIEDto);
        kafkaTemplate.send(IMPORT_TOPIC, fileIEDto);

//        // 4. Lưu trạng thái vào Redis
//        redisTemplate.opsForValue().set("import-status:" + importId, "PENDING");

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("file.import.in_progress", null, LocaleContextHolder.getLocale()))
                .data(modelMapper.map(history, ImportFileHistoryDto.class))
                .build();
    }

    @Override
    public GlobalReponse exportFile(FileIEDto fileIEDto) {

        log.info("Export file with File type: {} and Object type: {}", fileIEDto.getFileType(), fileIEDto.getObjectType());

        FileProcessorStrategy processor = fileProcessorFactory.getProcessor(fileIEDto.getFileType());
        return processor.exportFile(fileIEDto);
    }

    // check if there is already an import for this objectType in progress, if not, save based on params and
    // importFileHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
    @Override
    public ImportFileHistory saveImportFileHistory(Integer orgId,String fileCode, String fileType, String fileTail, String objectType) {

        Optional<ImportFileHistory> importFileHistoryOptional = importFileHistoryRepository
                .findByObjectTypeAndImportStatus(objectType,AppConstant.ImportFileStatus.IN_PROGRESS);
        if (importFileHistoryOptional.isPresent()) {
            throw new PosException(
                    messageSource.getMessage("file.import.in_progress", null, LocaleContextHolder.getLocale())
            );
        }

        Integer maxId = importFileHistoryRepository.getMaxId();
        String code = DocHelper.generateDocNo("IPF", maxId + 1);

        // 2. Lưu metadata vào DB (ImportHistory table)
        ImportFileHistory importFileHistory = new ImportFileHistory();
        importFileHistory.setCode(code);
        importFileHistory.setFileType(fileType);
        importFileHistory.setFileTail(fileTail);
        importFileHistory.setObjectType(objectType);
        importFileHistory.setImportStatus(AppConstant.ImportFileStatus.IN_PROGRESS);
//        importFileHistory.setErrorMessage("Process just started...no errors yet...");
        importFileHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        importFileHistory.setOrgId(orgId);
        importFileHistory.setImportDate(DateHelper.toInstantNowUTC());
        return importFileHistoryRepository.save(importFileHistory);
    }


    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void indicateImportService(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key();
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("indicateImportService():\nStarted background import to interface: Received message from Kafka topic: " + IMPORT_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            //Lưu vào Bảng interface
            FileProcessorStrategy processor = fileProcessorFactory.getProcessor(fileIEDto.getFileType());
            Map<String, Object> output = processor.importFile(fileIEDto);

            if (output.get("errorMessage") != null) {
                fileIEDto.setErrorMessage(ParseHelper.STRING.parse(output.get("errorMessage")));
            }

            // if HasData && ( No Error || case SKIP )
            if ((Boolean) output.get("hasImport") && (output.get("errorMessage") == null || "Y".equals(fileIEDto.getIsSkipErrors()))) {
                //Call integrate based on Object
                switch (fileIEDto.getObjectType()) {
                    case AppConstant.FileObjectType.CUSTOMER:
                        log.info("Customer");
                        kafkaTemplate.send(IMPORT_CUSTOMER_TOPIC, fileIEDto);
                        break;
                    case AppConstant.FileObjectType.BUSINESS_PARTNER_GROUP:
                        log.info("Partner group");
                        kafkaTemplate.send(IMPORT_PARTNER_GROUP_TOPIC, fileIEDto);
                        break;
                    case AppConstant.FileObjectType.PRODUCT:
                        log.info("Product");
                        kafkaTemplate.send(IMPORT_PRODUCT_TOPIC, fileIEDto);
                        break;
                    case AppConstant.FileObjectType.PRICE_LIST:
                        log.info("Price List");
                        kafkaTemplate.send(IMPORT_PRICE_LIST_TOPIC, fileIEDto);
                        break;
                    case AppConstant.FileObjectType.PRICE_LIST_PRODUCT:
                        log.info("Price List product");
                        kafkaTemplate.send(IMPORT_PRICE_LIST_PRODUCT_TOPIC, fileIEDto);
                        break;
                    case AppConstant.FileObjectType.PRODUCT_CATEGORY:
                        log.info("Product category");
                        kafkaTemplate.send(IMPORT_PRODUCT_CATEGORY_TOPIC, fileIEDto);
                        break;
                    default:
                        fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
                        fileIEDto.setErrorMessage("Object not found!");
                        kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
                }
            } else {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            }

        } catch (Exception e) {
            log.error("Caught error in indicateImportService(): {}", e.getMessage());
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage("Caught error in indicateImportService(): " + e.getMessage());
            kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
        }

    }

    @KafkaListener(groupId = GROUP_ID, topics = UPDATE_IMPORT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void updateImportFile(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("updateImportFile():\nFinal history update: Received message from Kafka topic: " + UPDATE_IMPORT_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();

            changeTenantAndLocale(fileIEDto);
            //send thong bao

            Optional<ImportFileHistory> importFileHistoryOptional = importFileHistoryRepository
                    .findByObjectTypeAndImportStatus(fileIEDto.getObjectType(),AppConstant.ImportFileStatus.IN_PROGRESS);

            if (importFileHistoryOptional.isPresent()) {
                ImportFileHistory importFileHistory = importFileHistoryOptional.get();
                importFileHistory.setImportStatus(fileIEDto.getImportStatus());
                importFileHistory.setErrorMessage(getImportDetails(fileIEDto));
                importFileHistoryRepository.save(importFileHistory);
                log.info("ImportFileHistory updated successfully for object type: {}", fileIEDto.getObjectType());

                sendNotify(importFileHistory, fileIEDto);
                log.info("send notify");
            } else {
                log.info("No ImportFileHistory found for object type: {}", fileIEDto.getObjectType());
            }
            // Xóa file tạm
            String fileName = fileIEDto.getFileName();
            fileStorageService.deleteFile(fileName);

        } catch (Exception e) {
            log.error("Caught error in updateImportFile(): ", e);
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
            log.error("Something went wrong", e);
        }
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    private void sendNotify(ImportFileHistory importFileHistory, FileIEDto fileIEDto) {
        log.info("*** KitchenOrderLine, service; send notify *");

        String NOTIFICATION_TITLE;
        String status;
        String NOTIFICATION_BODY;
        String Router = "IMPORT_FILE";
        String CODE = importFileHistory.getCode();
        List<String> deviceTokens = new ArrayList<>();

        setLocale("vi");

        if(fileIEDto.getErrorRows().isEmpty()){
            status = AppConstant.ImportFileStatus.SUCCESS;

            NOTIFICATION_TITLE =  messageSource.getMessage(
                    "file.import.title",
                    new Object[]{fileIEDto.getFileName()},
                    LocaleContextHolder.getLocale());

            NOTIFICATION_BODY =  messageSource.getMessage(
                    "file.import.body-success",
                    new Object[]{fileIEDto.getFileName()},
                    LocaleContextHolder.getLocale());


        }else{
            status = AppConstant.ImportFileStatus.FAILED;

            NOTIFICATION_TITLE =  messageSource.getMessage(
                    "file.import.title",
                    new Object[]{fileIEDto.getFileName()},
                    LocaleContextHolder.getLocale());

            NOTIFICATION_BODY = messageSource.getMessage(
                    "file.import.body-fail",
                    new Object[]{fileIEDto.getFileName()},
                    LocaleContextHolder.getLocale());
        }

        HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
        GlobalReponse exService = null;

        String token = getUserDeviceToken(importFileHistory.getCreatedBy());
        deviceTokens.add(token);
        log.info("token: {}", token);
        SendNotification send = SendNotification.builder()
                .title(NOTIFICATION_TITLE)
                .body(NOTIFICATION_BODY)
                .status(status)
                .code(CODE)
                .deviceTokens(deviceTokens)
                .router(Router)
                .type("ANN")
                .build();

        log.info("dto send: {}", send);
        HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
        exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);

        if (exService.getStatus().intValue() != HttpStatus.OK.value()
                && exService.getStatus().intValue() != HttpStatus.CREATED.value()) {
            throw new RuntimeException(exService.getMessage());
        }
    }

    public void setLocale(String lang) {
        Locale locale = Locale.lookup(Locale.LanguageRange.parse(lang), Arrays.asList(Locale.getAvailableLocales()));
        LocaleContextHolder.setLocale(locale);
    }

    public String getUserDeviceToken(Integer userId) {
        String result = null;
        String sql = "SELECT du.device_token\n" +
                "FROM d_user du\n" +
                "WHERE du.d_user_id = :userId " +
                "AND du.device_token IS NOT NULL;";

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("userId", userId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        log.info("device token: {}", results);
        for (Map<String, Object> row : results) {
            result = ParseHelper.STRING.parse(row.get("device_token"));
        }
        return result;
    }

    private String getImportDetails(FileIEDto fileIEDto) {
        String importDetails;
        if (fileIEDto.getErrorRows().isEmpty()) { // no errors
            importDetails = messageSource.getMessage(
                    "file.import.no_errors",
                    new Object[]{
                            fileIEDto.getRowCount(),
                            fileIEDto.getRowCount()
                    },
                    LocaleContextHolder.getLocale());
        } else if ("Y".equals(fileIEDto.getIsSkipErrors())) { // case SKIP
            importDetails = messageSource.getMessage(
                    "file.import.skip_errors",
                    new Object[]{
                            fileIEDto.getRowCount() - fileIEDto.getErrorRows().size(),
                            fileIEDto.getRowCount(),
                            fileIEDto.getErrorRows().size(),
                            fileIEDto.getRowCount()
                    },
                    LocaleContextHolder.getLocale());
        } else { // case STOP
            importDetails = messageSource.getMessage("file.import.stopped", null, LocaleContextHolder.getLocale()) + " ";
        }
        return fileIEDto.getErrorMessage() != null ?
                importDetails + fileIEDto.getErrorMessage() :
                importDetails;
    }
}
