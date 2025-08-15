//package com.dbiz.app.integrationservice.service.impl;
//
//import com.dbiz.app.integrationservice.constant.AppConstant;
//import com.dbiz.app.integrationservice.domain.ErpIntegration;
//import com.dbiz.app.integrationservice.domain.IntegrationHistory;
//import com.dbiz.app.integrationservice.exception.IntegrationServiceException;
//import com.dbiz.app.integrationservice.helper.DateHelper;
//import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
//import com.dbiz.app.integrationservice.service.*;
//import com.dbiz.app.integrationservice.specification.ErpIntegrationSpecification;
//import com.dbiz.app.tenantservice.domain.AuditContext;
//import com.dbiz.app.tenantservice.domain.AuditInfo;
//import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.v3.core.util.Json;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.common.dbiz.dto.PcTerminalAccessDto;
//import org.common.dbiz.dto.integrationDto.*;
//import com.dbiz.app.integrationservice.helper.mapper.ErpIntegrationMapper;
//import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
//import org.common.dbiz.dto.orderDto.FloorDto;
//import org.common.dbiz.dto.orderDto.TableDto;
//import org.common.dbiz.dto.productDto.*;
//import org.common.dbiz.dto.tenantDto.OrgDto;
//import org.common.dbiz.dto.tenantDto.PosTerminalDto;
//import org.common.dbiz.dto.userDto.*;
//import org.common.dbiz.exception.PosException;
//import org.common.dbiz.payload.GlobalReponse;
//import org.common.dbiz.payload.GlobalReponsePagination;
//import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
//import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
//import org.common.dbiz.request.intergrationRequest.external.*;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.http.*;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.ResourceAccessException;
//import org.springframework.web.client.RestTemplate;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import javax.transaction.Transactional;
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//
///*
// * Nùi bùi nhuùi của ThanhNC
// * thu tu dong bo
// * dong bo org
// * dong bo user
// * dong bo posterminal
// * dong bo product
// * dong bo product category
// * dong bo floor
// * dong bo table
//
// */
//@Service
//@Slf4j
//@RequiredArgsConstructor
////@Transactional
//@SuppressWarnings("unchecked")
//public class ErpIntegrationServiceImpl implements ErpIntegrationService {
//    private final ErpIntegrationRepository erpIntegrationRepository;
//
//    private final ModelMapper modelMapper;
//
//    private final RequestParamsUtils requestParamsUtils;
//
//    private final MessageSource messageSource;
//
//    private final ErpIntegrationMapper erpIntegrationMapper;
//
//    private final IntegrationHistoryRepository integrationHistoryRepository;
//
//    private final OrderIntegrationService orderIntegrationService;
//
//    private final KitchenOrderIntegrationService kitchenOrderIntegrationService;
//
//    private final ProductIntegrationService productIntegrationService;
//
//    private final PriceListIntegrationService priceListIntegrationService;
//
//    private final CouPonService couPonService;
//
//    private final EntityManager entityManager;
//
//    private final PartnerIntegrationService partnerIntegrationService;
//
//    private final OrgIntService orgIntService;
//
//    private final WarehouseIntService warehouseIntService;
//
//    private final PosTerminalIntService posTerminalIntService;
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//    private final List<IntPartnerDto> messageQueue = new ArrayList<>();
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//    private final UserIntegrationService userIntegrationService;
//
//
//    private final DataSourceContextHolder dataSourceContextHolder;
//    private final DataSourceConfigService dataSourceConfigService;
//    private final DataSourceRoutingService dataSourceRoutingService;
//
//    private final CommonIntegrationService commonIntegrationService;
//
//    private StringBuilder error;
//    @Autowired
//    @Qualifier("restTemplateBean")
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    @Qualifier("externalRestTemplate")
//    private final RestTemplate externalRestTemplate;
//
//    @Override
//    public GlobalReponsePagination findAll(ErpIntegrationQueryRequest request) {
//        GlobalReponsePagination response = new GlobalReponsePagination();
//        Pageable page = requestParamsUtils.getPageRequest(request);
//        Specification<ErpIntegration> specification = ErpIntegrationSpecification.getErpIntegrationSpecification(request);
//        Page<ErpIntegration> dataPage = erpIntegrationRepository.findAll(specification, page);
//        List<ErpIntegrationDto> data = dataPage.getContent().stream().map(entity -> modelMapper.map(entity, ErpIntegrationDto.class)).collect(Collectors.toList());
//        response.setData(data);
//        response.setCurrentPage(dataPage.getNumber());
//        response.setTotalItems(dataPage.getTotalElements());
//        response.setTotalPages(dataPage.getTotalPages());
//        response.setPageSize(dataPage.getSize());
//        response.setStatus(HttpStatus.OK.value());
//        return response;
//    }
//
//    @Override
//    public GlobalReponse findById(Integer integer) {
//        return null;
//    }
//
//    @Override
//
//    public GlobalReponse save(ErpIntegrationDto entity) {
//
//        GlobalReponse response = new GlobalReponse();
//        ErpIntegration erpIntegration = erpIntegrationRepository.findByErpUrl(entity.getErpUrl());
//        if (erpIntegration != null) {
//            this.erpIntegrationRepository.updateAllIsDefaultById("N", AuditContext.getAuditInfo().getTenantId());
//            erpIntegration = erpIntegrationMapper.updateEntity(entity, erpIntegration);
//            erpIntegration.setIsDefault("Y");
//            erpIntegrationRepository.save(erpIntegration);
//            response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
//        } else {
//            this.erpIntegrationRepository.updateAllIsDefaultById("N", AuditContext.getAuditInfo().getTenantId());
//            erpIntegration = erpIntegrationMapper.toEntity(entity);
//            erpIntegration.setTenantId(AuditContext.getAuditInfo().getTenantId());
//            erpIntegration.setIsDefault("Y");
//            erpIntegration.setOrgId(0);
//            erpIntegration = erpIntegrationRepository.save(erpIntegration);
//            response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
//        }
//        response.setMessage(messageSource.getMessage("erp.integration.success", null, LocaleContextHolder.getLocale()));
//        response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
//        response.setStatus(HttpStatus.OK.value());
//        return response;
//    }
//
//    @Override
//    public GlobalReponse deleteById(Integer integer) {
//        return null;
//    }
//
//    /*
//     * 1. Lấy thông tin token từ ERP
//     * 2. Lấy thông tin từ ERP
//     * 3. Gửi thông tin lên hệ thống
//     * 4. Lưu lịch sử tích hợp
//     * 5. Trả về kết quả
//     */
//    @Override
//    @Transactional
//    public GlobalReponse syncIntegration(SyncIntegrationCredential credential) {
//        //        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
//        IntegrationHistory integrationHistory = null;
//        GlobalReponse response = new GlobalReponse();
//        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;
//
//        if (credential.getDataType().equals("PRO") || credential.getDataType().equals("PRL")
//                || credential.getDataType().equals("CAV")) {
//            List<IntegrationHistory> checkIntProduct = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("PRO", "INP", AuditContext.getAuditInfo().getTenantId());
//            if (checkIntProduct != null && !checkIntProduct.isEmpty())
//                throw new IntegrationServiceException(messageSource.getMessage("erp.integration.product.inprogress", null, LocaleContextHolder.getLocale()));
//
//            integrationHistory = IntegrationHistory.builder()
//                    .orgId(credential.getOrgId())
//                    .intType(credential.getDataType())
//                    .intFlow(credential.getIntFlow())
//                    .intDate(Instant.now())
//                    .intStatus("INP")
//                    .userId(credential.getUserId())
//                    .tenantId(AuditContext.getAuditInfo().getTenantId())
//                    .orgId(0)
//                    .description(error != null ? error.toString() : "")
//                    .integrationType(AppConstant.IntegrationType.ERP)
//                    .build();
//
//            integrationHistory = integrationHistoryRepository.save(integrationHistory);
//            credential.setIntegrationHistoryId(integrationHistory.getId());
//        }
//        switch (credential.getDataType()) {
//            case AppConstant.DataTypeInt.ORGANIZATION:
//                integrationHistory = this.orgIntService.orgIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.POS_TERMINAL:
//                List<IntegrationHistory> checkIntPricelist = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("PRL", "INP", AuditContext.getAuditInfo().getTenantId());
//                if (checkIntPricelist != null && !checkIntPricelist.isEmpty())
//                    throw new PosException(messageSource.getMessage("erp.integration.pricelist.inprogress", null, LocaleContextHolder.getLocale()));
//
//                integrationHistory = this.posTerminalIntService.posTerminalIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.PRODUCT_CATEGORY:
//                integrationHistory = this.productIntegrationService.productCategoryIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.FLOOR:
//                List<IntegrationHistory> checkIntFloor = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("FLO", "INP", AuditContext.getAuditInfo().getTenantId());
//                if (checkIntFloor != null && !checkIntFloor.isEmpty())
//                    throw new PosException(messageSource.getMessage("erp.integration.floor.inprogress", null, LocaleContextHolder.getLocale()));
//
//                integrationHistory = floorIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.TABLE:
//                List<IntegrationHistory> checkIntTable = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("TBL", "INP", AuditContext.getAuditInfo().getTenantId());
//                if (checkIntTable != null && !checkIntTable.isEmpty())
//                    throw new PosException(messageSource.getMessage("erp.integration.table.inprogress", null, LocaleContextHolder.getLocale()));
//                integrationHistory = tableIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.CUSTOMER_VENDOR:
//                List<IntegrationHistory> checkIntCAV = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndIdNotIn("CAV", "INP", credential.getIntegrationHistoryId());
//                if (checkIntCAV != null && !checkIntCAV.isEmpty())
//                    throw new PosException(messageSource.getMessage("erp.integration.customer.vendor.inprogress", null, LocaleContextHolder.getLocale()));
//                integrationHistory = partnerIntegrationService.partnerIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.USER:
//                List<IntegrationHistory> checkIntCUS = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("CUS", "INP", AuditContext.getAuditInfo().getTenantId());
//                if (checkIntCUS != null && !checkIntCUS.isEmpty())
//                    throw new PosException(messageSource.getMessage("erp.integration.user.inprogress", null, LocaleContextHolder.getLocale()));
//                integrationHistory = this.userIntegrationService.userIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.SALES_ORDER:
//                integrationHistoryInfoDto = orderIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.WAREHOUSE:
//                integrationHistory =this.warehouseIntService.warehouseIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.PRODUCT:
//                integrationHistoryInfoDto = productIntegration(credential);
//                if (!integrationHistoryInfoDto.getStatusIntegration().equals("INP")) {
//                    integrationHistory.setIntStatus(integrationHistoryInfoDto.getStatusIntegration());
//                    integrationHistory.setDescription(integrationHistoryInfoDto.getError());
//                    this.integrationHistoryRepository.save(integrationHistory);
//                }
//                break;
//            case  AppConstant.DataTypeInt.PRICE_LIST:
//                List<IntegrationHistory> checkIntProduct = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("PRO", "INP", AuditContext.getAuditInfo().getTenantId());
//                if (checkIntProduct != null && !checkIntProduct.isEmpty())
//                    throw new PosException(messageSource.getMessage("erp.integration.product.inprogress", null, LocaleContextHolder.getLocale()));
//                integrationHistoryInfoDto = priceListIntegrationService.syncPriceListErp(credential);
//                if (!integrationHistoryInfoDto.getStatusIntegration().equals("INP")) {
//                    integrationHistory.setIntStatus(integrationHistoryInfoDto.getStatusIntegration());
//                    integrationHistory.setDescription(integrationHistoryInfoDto.getError());
//                    this.integrationHistoryRepository.save(integrationHistory);
//                }
//                break;
//            case  AppConstant.DataTypeInt.COUPON:
//                integrationHistory = couPonService.syncCounponIntegration(credential);
//                break;
//            case  AppConstant.DataTypeInt.KITCHEN_ORDER:
//                integrationHistoryInfoDto = kitchenOrderIntegration(credential);
//                break;
//            default:
//                throw new PosException(messageSource.getMessage("erp.integration.data.type.not.found", null, LocaleContextHolder.getLocale()));
//        }
//
//
//        if (!(credential.getDataType().equals("PRO") || credential.getDataType().equals("PRL") || credential.getDataType().equals("CAV"))) {
//            integrationHistory = IntegrationHistory.builder()
//                    .orgId(credential.getOrgId())
//                    .intType(credential.getDataType())
//                    .intFlow(credential.getIntFlow())
//                    .intDate(Instant.now())
//                    .userId(credential.getUserId())
//                    .tenantId(AuditContext.getAuditInfo().getTenantId())
//                    .orgId(0)
//                    .intStatus(integrationHistoryInfoDto != null ? integrationHistoryInfoDto.getStatusIntegration() : integrationHistory.getIntStatus())
//                    .description(error != null ? error.toString() : "")
//                    .build();
//            if (integrationHistoryInfoDto != null) {
//                integrationHistory.setIntStatus(integrationHistoryInfoDto.getStatusIntegration());
//                integrationHistory.setPayload(integrationHistoryInfoDto.getPayload());
//                integrationHistory.setResponse(integrationHistoryInfoDto.getResponse());
//            }
//
//            integrationHistory = integrationHistoryRepository.save(integrationHistory);
//        }
//
//
//        IntegrationHistoryDto dto = modelMapper.map(integrationHistory, IntegrationHistoryDto.class);
//        dto.setIntDate(integrationHistory.getIntDate() != null ? DateHelper.fromInstant(integrationHistory.getIntDate()) : DateHelper.fromInstant(Instant.now()));
//        response.setData(dto);
//        response.setMessage(messageSource.getMessage(integrationHistory.getIntStatus().equals("COM") ? "erp.integration.success" : "erp.integration.failed", null, LocaleContextHolder.getLocale()));
//        response.setStatus(HttpStatus.OK.value());
//        if (integrationHistory.getIntStatus().equals("INP"))
//            response.setMessage(messageSource.getMessage("erp.integration.inprogress", null, LocaleContextHolder.getLocale()));
//        if(integrationHistory.getIntStatus().equals("FAI"))
//            response.setErrors(integrationHistory.getDescription() );
//        return response;
//    }
//
//
//    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_RECEIVE_PCG, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessage(ConsumerRecord<String, ProductCategoryKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("kafla listener product category");
//        ProductCategoryKafkaDto value = consumerRecord.value();
//        try {
//            int tenantNumbers = getTenantNumbers();
//            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                Map<Object, Object> configuredDataSources = dataSourceConfigService
//                        .configureDataSources();
//
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (value.getTenantId() != 0) {
//            dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//        } else {
//            dataSourceContextHolder.setCurrentTenantId(null);
//
//        }
//        AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                "0", 0, "vi", value.getTenantId()));
//
//
//        List<IntegrationHistory> integrationHistory = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("PCG", "INP", AuditContext.getAuditInfo().getTenantId());
//
//        if (integrationHistory != null && !integrationHistory.isEmpty()) {
//            integrationHistory.forEach(item -> {
//                item.setIntStatus(value.getStatus());
//                item.setDescription(value.getError());
//                this.integrationHistoryRepository.save(item);
//            });
//        }
//        acknowledgment.acknowledge();
//    }
//
//    // tich hop floor
//
//    public IntegrationHistory floorIntegration(SyncIntegrationCredential credential) {
//        error = new StringBuilder();
//        GlobalReponse response = new GlobalReponse();
//        log.info("**ServiceImpl::layer -  floor integration **");
//
//        IntegrationHistory integrationHistory = new IntegrationHistory();
//        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
//        if (erpIntegration == null) {
//            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
//        }
//
//        List<FloorDto> getFloorErp = getFloorErp(credential, erpIntegration);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
//        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
//        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
//        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
//        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
//        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<List<FloorDto>> requestEntity = new HttpEntity<>(getFloorErp, headers);
//
//        if (credential.getIntType().equals("UPDATE") || (credential.getIntType().equals("INSERT") && getFloorErp.size() < 30)) {
//            response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_FLOOR_SAVE, requestEntity, GlobalReponse.class);
//
//            if (response.getStatus() != HttpStatus.OK.value()) {
//                error = new StringBuilder(response.getErrors());
//            }
//        }
//        integrationHistory.setIntType(credential.getDataType());
//        integrationHistory.setIntFlow(credential.getIntFlow());
//        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
//        if (credential.getIntType().equals("UPDATE") || (credential.getIntType().equals("INSERT") && credential.getSyncKafka().equals("N") ) )
//            integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
//        else
//            integrationHistory.setIntStatus("INP");
//        integrationHistory.setUserId(credential.getUserId());
//        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
//        integrationHistory.setOrgId(0);
//        integrationHistory.setDescription(error.toString());
//        integrationHistory.setPayload(Json.pretty(getFloorErp));
//        return integrationHistory;
//    }
//
////    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_RECEIVE_FLO, containerFactory = "kafkaListenerContainerFactory")
////    public void receivedMessageFLO(ConsumerRecord<String, FloorKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
////        log.info("kafla listener floor");
////        FloorKafkaDto value = consumerRecord.value();
////        try {
////            int tenantNumbers = getTenantNumbers();
////            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
////                Map<Object, Object> configuredDataSources = dataSourceConfigService
////                        .configureDataSources();
////
////                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////            }
//////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
//////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
//////                            .configureDataSources();
//////
//////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//////                }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        if (value.getTenantId() != 0) {
////            dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
////        } else {
////            dataSourceContextHolder.setCurrentTenantId(null);
////
////        }
////        AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
////                "0", 0, "vi", value.getTenantId()));
////
////        String dbName = "";
////        try {
////            Query query = entityManager.createNativeQuery("SELECT current_database()");
////            dbName = (String) query.getSingleResult();
////            log.info("dbName " + dbName);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        List<IntegrationHistory> integrationHistory = this.integrationHistoryRepository.findByIntTypeAndIntStatus("FLO", "INP");
////
////        if (value.getLastPage().equals("Y")) {
////            if (integrationHistory != null && !integrationHistory.isEmpty()) {
////                integrationHistory.forEach(item -> {
////                    item.setIntStatus(value.getStatus());
////                    item.setDescription(value.getError());
////                    this.integrationHistoryRepository.save(item);
////                });
////            }
////        }
////        acknowledgment.acknowledge();
////    }
//
//
//    public IntegrationHistory tableIntegration(SyncIntegrationCredential credential) {
//        log.info("**ServiceImpl::layer -  table integration **");
//        error = new StringBuilder();
//        GlobalReponse response = new GlobalReponse();
//        IntegrationHistory integrationHistory = new IntegrationHistory();
//        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
//        if (erpIntegration == null) {
//            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
//        }
//
//        List<TableDto> getTableErp = getTableERP(credential, erpIntegration);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
//        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
//        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
//        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
//        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
//        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<List<TableDto>> requestEntity = new HttpEntity<>(getTableErp, headers);
//
//
//        if (credential.getIntType().equals("UPDATE") || (credential.getIntType().equals("INSERT") && getTableErp.size() < 30)) {
//            response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_TABLE_SAVE, requestEntity, GlobalReponse.class);
//            if (response.getStatus() != HttpStatus.OK.value()) {
//                error = new StringBuilder(response.getErrors());
//            }
//        }
//
//        integrationHistory.setIntType(credential.getDataType());
//        integrationHistory.setIntFlow(credential.getIntFlow());
//        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
//        if (credential.getIntType().equals("INSERT") && credential.getSyncKafka().equals("Y"))
//            integrationHistory.setIntStatus("INP");
//        else
//            integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
//        integrationHistory.setUserId(credential.getUserId());
//        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
//        integrationHistory.setOrgId(0);
//        integrationHistory.setDescription(error.toString());
//        integrationHistory.setPayload(Json.pretty(getTableErp));
//        return integrationHistory;
//    }
//
//
//
//    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_RECEIVE_CAV, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessageCAV(ConsumerRecord<String, IntPartnerDto> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("kafla listener customer vendor");
//        IntPartnerDto value = consumerRecord.value();
//        try {
//            int tenantNumbers = getTenantNumbers();
//            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                Map<Object, Object> configuredDataSources = dataSourceConfigService
//                        .configureDataSources();
//
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (value.getTenantId() != 0) {
//            dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//        } else {
//            dataSourceContextHolder.setCurrentTenantId(null);
//
//        }
//        AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                "0", 0, "vi", value.getTenantId()));
//        List<IntegrationHistory> list = this.integrationHistoryRepository.findAll();
//        Optional<IntegrationHistory> integrationHistory = this.integrationHistoryRepository.findByIdAndIntStatus(value.getSyncIntegrationCredential().getIntegrationHistoryId(),"INP");
//        if (integrationHistory.isPresent()) {
//            IntegrationHistory history = integrationHistory.get();
//            history.setIntStatus(value.getStatus());
//            history.setDescription(value.getError());
//            this.integrationHistoryRepository.save(history);
//        }else{
//            log.info("not found integration history integration customer - vendor - partner group");
//        }
//        acknowledgment.acknowledge();
//    }
//
//    public IntegrationHistoryInfoDto productIntegration(SyncIntegrationCredential credential) {
//        log.info("**ServiceImpl::layer -  product integration **");
//
//        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
//        if (erpIntegration == null) {
//            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
//        }
//        credential.setSyncKafka("N");
//        IntegrationHistoryInfoDto result = this.productIntegrationService.getProductErp(credential);
//        return result;
//    }
//
//    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_INTERNAL_PRODUCT, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessageProduct(ConsumerRecord<String, SyncIntegrationCredential> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("kafka listener internal product");
//        SyncIntegrationCredential value = consumerRecord.value();
//        try {
//            int tenantNumbers = getTenantNumbers();
//            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                Map<Object, Object> configuredDataSources = dataSourceConfigService
//                        .configureDataSources();
//
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (value.getTenantId() != 0) {
//            dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//        } else {
//            dataSourceContextHolder.setCurrentTenantId(null);
//
//        }
//        AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                "0", 0, "vi", value.getTenantId()));
//
//        IntegrationHistoryInfoDto result = this.productIntegrationService.getProductErp(value);
//        acknowledgment.acknowledge();
//    }
//
//
//    // integration user
//
//    public IntegrationHistoryInfoDto orderIntegration(SyncIntegrationCredential credential) {
//        log.info("**ServiceImpl::layer -  order integration **");
//        return orderIntegrationService.syncOrderIntegrationMain(credential);
//
//    }
//
//
//    // integration user
//    public IntegrationHistoryInfoDto kitchenOrderIntegration(SyncIntegrationCredential credential) {
//        log.info("**ServiceImpl::layer - kitchen order integration **");
//        return kitchenOrderIntegrationService.syncKitchenOrderIntegrationMain(credential);
//
//    }
//
//    // get token from erp
//
//    public String getTokenErp() {
//        log.info("** getTokenErp **");
//        StringBuilder tokenResponse = null;
//        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
//        if (erpIntegration == null) {
//            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
//        }
//        GetTokenCredential token = GetTokenCredential.builder()
//                .userName(erpIntegration.getUsername())
//                .password(erpIntegration.getPassword())
//                .parameters(GetTokenCredential.Parameters.builder()
//                        .clientId(erpIntegration.getAdClientId())
//                        .roleId(erpIntegration.getAdRoleId())
//                        .language("en_US")
//                        .build()).build();
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json; charset=utf-8");
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//
//        HttpEntity<GetTokenCredential> requestEntity = new HttpEntity<>(token, headers);
//        String urlGetoken = erpIntegration.getErpUrl() + getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_GETTOKEn);
//
//        ResponseEntity<String> response = externalRestTemplate.exchange(urlGetoken, HttpMethod.POST, requestEntity, String.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            String responseBody = response.getBody();
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            try {
//                JsonNode root = objectMapper.readTree(responseBody);
//                String userId = root.path("userId").asText();
//                String language = root.path("language").asText(null);
//                tokenResponse = new StringBuilder(root.path("token").asText());
//                log.info("resultCode: " + userId);
//                log.info("errorMessage: " + language);
//                log.info("token: " + tokenResponse.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
//            }
//        }
//
//        return tokenResponse.toString();
//    }
//
//    //get endpoint from system service
//
//    public String getEndPointINT(String nameEnd) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
//
//        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
//
//        log.info("** getEndPointINT **");
//        StringBuilder endPoint = null;
//        GlobalReponse callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_API_URL_GET_BY_NAME + "/" + nameEnd, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//        if (callExService.getStatus() == HttpStatus.OK.value()) {
//            endPoint = new StringBuilder(callExService.getData().toString());
//        } else {
//            throw new PosException(messageSource.getMessage("get.endpoint.error", null, LocaleContextHolder.getLocale()));
//        }
//
//        return callExService.getData().toString();
//    }
//
//
//
//
//
//    // get product Category from erp
//
//
//
//    // lay data floor tu erp
//
//    public List<FloorDto> getFloorErp(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
//        credential.setSyncKafka("N");
//        log.info("** getFloorErp **");
//        List<FloorDto> listFloor = new ArrayList<>();
//        boolean sendKafka = false;
//
//        IntFloorCredential intFloorCredential = IntFloorCredential.builder()
//                .clientId(erpIntegration.getAdClientId())
//                .orgId(erpIntegration.getOrgId())
//                .intType(credential.getIntType())
//                .intDate(commonIntegrationService.castIntDate(credential)).build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth(getTokenErp());
//
//        try {
//            HttpEntity<IntFloorCredential> requestEntity = new HttpEntity<>(intFloorCredential, headers);
//            String url = erpIntegration.getErpUrl() + getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_FLO);
//            log.info("url integration floor: " + url);
//            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                String responseBody = response.getBody();
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                try {
//                    JsonNode root = objectMapper.readTree(responseBody);
//
//                    // khong lay c_bpartner_id
//                    for (JsonNode node : root.path("data")) {
//                        FloorDto floorDto = FloorDto.builder()
//                                .floorNo(node.path("floorNo").asText())
//                                .isActive(node.path("isActive").asText())
//                                .name(node.path("name").asText())
//                                .posTerminalId(node.path("C_POS_ID").asInt())
//                                .orgId(node.path("AD_Org_ID").asInt())
//                                .erpFloorId(node.path("floorId").asInt())
//                                .build();
//                        listFloor.add(floorDto);
//                        if (listFloor.size() == 30 && credential.getIntType().equals("INSERT")) {
//                            try {
//                                log.info("send message to kafka");
//                                FloorKafkaDto floorKafkaDto = FloorKafkaDto.builder()
//                                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                        .floorDtos(listFloor)
//                                        .lastPage("N")
//                                        .build();
//                                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_FLO, floorKafkaDto);
//                                sendKafka = true;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            listFloor.clear();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
//                }
//            }
//        } catch (ResourceAccessException e) {
//            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
//        }
//        if (credential.getIntType().equals("INSERT") && sendKafka) {
//            credential.setSyncKafka("Y");
//            try {
//                log.info("send message to kafka");
//                FloorKafkaDto floorKafkaDto = FloorKafkaDto.builder()
//                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                        .floorDtos(listFloor)
//                        .lastPage("Y")
//                        .build();
//                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_FLO, floorKafkaDto);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return listFloor;
//    }
//
//
//    // get table from erp
//
//    public List<TableDto> getTableERP(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
//        log.info("** getOrgErp **");
//        credential.setSyncKafka("N");
//        List<TableDto> listTable = new ArrayList<>();
//        boolean sendKafka = false;
////        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
//        IntTableCredential intTableCredential = IntTableCredential.builder()
//                .clientId(erpIntegration.getAdClientId())
//                .orgId(erpIntegration.getOrgId())
//                .intType(credential.getIntType())
//                .intDate(commonIntegrationService.castIntDate(credential)).build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth(getTokenErp());
//
//
//        try {
//            HttpEntity<IntTableCredential> requestEntity = new HttpEntity<>(intTableCredential, headers);
//            String url = erpIntegration.getErpUrl() + getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_TBL);
//            log.info("url integration org: " + url);
//            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                String responseBody = response.getBody();
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                try {
//                    JsonNode root = objectMapper.readTree(responseBody);
//
//                    for (JsonNode node : root.path("data")) {
//                        TableDto tableDto = TableDto.builder()
//                                .isBuffet(node.path("isBuffet").asText())
//                                // ad_user_id
//                                .isActive(node.path("isActive").asText())
//                                .numberSeats(node.path("seats").asInt())
//                                .isRoom(node.path("isRoom").asText())
//                                .floor(FloorDto.builder().erpFloorId(node.path("floorId").asInt()).build())
//                                .orgId(node.path("AD_Org_ID").asInt())
//                                .name(node.path("name").asText())
//                                .numberGuests(node.path("numberGuests").decimalValue())
//                                // bpartner_id
//                                .erpTableId(node.path("tableId").asInt())
//                                .isLocked(node.path("locked").asText())
//                                .tableNo(node.path("tableNo").asText())
//                                .build();
//                        listTable.add(tableDto);
//
//                        if (listTable.size() == 30 && credential.getIntType().equals("INSERT")) {
//                            credential.setSyncKafka("Y");
//                            try {
//                                TableKafkaDto syncTable = TableKafkaDto.builder()
//                                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                        .tableDtos(listTable)
//                                        .lastPage("N")
//                                        .build();
//                                log.info("send message to kafka");
//                                syncTable.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
//                                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_TBL, syncTable);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            listTable.clear();
//                            sendKafka = true;
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
//                }
//            }
//        } catch (ResourceAccessException e) {
//            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
//        }
//        if (credential.getIntType().equals("INSERT") && sendKafka) {
//            try {
//                TableKafkaDto syncTable = TableKafkaDto.builder()
//                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                        .tableDtos(listTable)
//                        .lastPage("Y")
//                        .build();
//                log.info("send message to kafka");
//                syncTable.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
//                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_TBL, syncTable);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return listTable;
//    }
//
//
//    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_RECEIVE_CUS, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessageCus(ConsumerRecord<String, UserIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
//        UserIntKafkaDto value = consumerRecord.value();
//        try {
//            int tenantNumbers = getTenantNumbers();
//            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                Map<Object, Object> configuredDataSources = dataSourceConfigService
//                        .configureDataSources();
//
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (value.getTenantId() != 0) {
//            dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//        } else {
//            dataSourceContextHolder.setCurrentTenantId(null);
//
//        }
//        AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                "0", 0, "vi", value.getTenantId()));
//
//
//        List<IntegrationHistory> integrationHistory = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId("CUS", "INP", AuditContext.getAuditInfo().getTenantId());
//
//        if (value.getLastPage().equals("Y")) {
//            if (integrationHistory != null && !integrationHistory.isEmpty()) {
//                integrationHistory.forEach(item -> {
//                    item.setIntStatus(value.getStatus());
//                    item.setDescription(value.getError());
//                    this.integrationHistoryRepository.save(item);
//                });
//            }
//        }
//        acknowledgment.acknowledge();
//    }
//    public int getTenantNumbers() {
//        return dataSourceConfigService.getTenantNumbersRedis();
//    }
//
//}
