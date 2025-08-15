package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import com.dbiz.app.integrationservice.exception.IntegrationServiceException;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.helper.mapper.ErpIntegrationMapper;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.repository.InterfaceIntegrationRepository;
import com.dbiz.app.integrationservice.service.*;
import com.dbiz.app.integrationservice.specification.ErpIntegrationSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.dto.integrationDto.FloorKafkaDto;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.userDto.IntPartnerDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/*
 * Nùi bùi nhuùi của ThanhNC
 * thu tu dong bo
 * dong bo org
 * dong bo user
 * dong bo posterminal
 * dong bo product
 * dong bo product category
 * dong bo floor
 * dong bo table

 */
@Service
@Slf4j
@RequiredArgsConstructor
//@Transactional
@SuppressWarnings("unchecked")
public class ErpIntegrationServiceV2Impl implements ErpIntegrationServiceV2 {

    private final ErpIntegrationRepository erpIntegrationRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final ErpIntegrationMapper erpIntegrationMapper;

    private final IntegrationHistoryRepository integrationHistoryRepository;

    private final OrderIntegrationService orderIntegrationService;

    private final KitchenOrderIntegrationService kitchenOrderIntegrationService;

    private final ProductIntegrationService productIntegrationService;

    private final PriceListIntegrationService priceListIntegrationService;

    private final CouPonService couPonService;

    private final PartnerIntegrationService partnerIntegrationService;

    private final OrgIntService orgIntService;

    private final WarehouseIntService warehouseIntService;

    private final PosTerminalIntService posTerminalIntService;

    private final UserIntegrationService userIntegrationService;

    private final TableIntegrationService tableIntegration;

    private final FloorIntegrationService floorIntegration;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final CommonIntegrationService commonIntegrationService;
    private final InterfaceIntegrationRepository interfaceIntegrationRepository;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public GlobalReponsePagination findAll(ErpIntegrationQueryRequest request) {
        GlobalReponsePagination response = new GlobalReponsePagination();
        Pageable page = requestParamsUtils.getPageRequest(request);
        Specification<ErpIntegration> specification = ErpIntegrationSpecification.getErpIntegrationSpecification(request);
        Page<ErpIntegration> dataPage = erpIntegrationRepository.findAll(specification, page);
        List<ErpIntegrationDto> data = dataPage.getContent().stream().map(entity -> modelMapper.map(entity, ErpIntegrationDto.class)).collect(Collectors.toList());
        response.setData(data);
        response.setCurrentPage(dataPage.getNumber());
        response.setTotalItems(dataPage.getTotalElements());
        response.setTotalPages(dataPage.getTotalPages());
        response.setPageSize(dataPage.getSize());
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    public GlobalReponse save(ErpIntegrationDto entity) {

        GlobalReponse response = new GlobalReponse();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByErpUrl(entity.getErpUrl());
        if (erpIntegration != null) {
            this.erpIntegrationRepository.updateAllIsDefaultById("N", AuditContext.getAuditInfo().getTenantId(), "Y");
            erpIntegration = erpIntegrationMapper.updateEntity(entity, erpIntegration);
            erpIntegration.setIsDefault("Y");
            erpIntegrationRepository.save(erpIntegration);
            response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
        } else {
            this.erpIntegrationRepository.updateAllIsDefaultById("N", AuditContext.getAuditInfo().getTenantId(), "Y");
            erpIntegration = erpIntegrationMapper.toEntity(entity);
            erpIntegration.setTenantId(AuditContext.getAuditInfo().getTenantId());
            erpIntegration.setIsDefault("Y");
            erpIntegration.setOrgId(0);
            erpIntegration = erpIntegrationRepository.save(erpIntegration);
            response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
        }
        response.setMessage(messageSource.getMessage("erp.integration.success", null, LocaleContextHolder.getLocale()));
        response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse activeToggle(ErpIntegrationDto entity) {

        GlobalReponse response = new GlobalReponse();
        ErpIntegration erpIntegration = new ErpIntegration();

        if (entity.getIsDefault() != null && "N".equals(entity.getIsDefault())) {

            erpIntegration = erpIntegrationRepository.findByErpUrl(entity.getErpUrl());
            erpIntegration = erpIntegrationMapper.updateEntity(entity, erpIntegration);

            erpIntegration.setIsDefault("N");
            erpIntegration.setIsActive("N");

            erpIntegrationRepository.save(erpIntegration);
            response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
        } else {

            this.erpIntegrationRepository.updateAllIsDefaultById("Y", AuditContext.getAuditInfo().getTenantId(), "N");
            erpIntegration.setIsActive("Y");

            erpIntegration = erpIntegrationRepository.findByIsActiveAndIsDefault("Y", "Y");
            response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
        }
        response.setMessage(messageSource.getMessage("erp.integration.success", null, LocaleContextHolder.getLocale()));
        response.setData(modelMapper.map(erpIntegration, ErpIntegrationDto.class));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    @Transactional
    public GlobalReponse syncIntegration(SyncIntegrationCredential credential) {
        //        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntegrationHistory integrationHistory = null;
        GlobalReponse response = new GlobalReponse();
        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;


        IntegrationHistory checkIntProcess = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId(credential.getDataType(), AppConstant.IntStatus.INPROCESSING, AuditContext.getAuditInfo().getTenantId()).orElse(null);
        if (checkIntProcess != null && checkIntProcess.getIntType().equals(credential.getDataType()))
            throw new IntegrationServiceException(messageSource.getMessage("erp.integration.inprogressing", null, LocaleContextHolder.getLocale()));
        else if (checkIntProcess != null && checkIntProcess.getIntType().equals(AppConstant.DataTypeInt.PRICE_LIST)) {
            if (credential.getDataType().equals(AppConstant.DataTypeInt.POS_TERMINAL))
                throw new PosException(messageSource.getMessage("erp.integration.pricelist.inprogress", null, LocaleContextHolder.getLocale()));
            if(credential.getDataType().equals(AppConstant.DataTypeInt.PRODUCT))
                throw new PosException(messageSource.getMessage("erp.integration.pricelist.inprogress", null, LocaleContextHolder.getLocale()));
        }

        integrationHistory = IntegrationHistory.builder()
                .orgId(credential.getOrgId())
                .intType(credential.getDataType())
                .intFlow(credential.getIntFlow())
                .intDate(Instant.now())
                .intStatus(AppConstant.IntStatus.INPROCESSING)
                .userId(credential.getUserId())
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .orgId(0)
                .integrationType(AppConstant.IntegrationType.ERP)
                .build();

        integrationHistory = integrationHistoryRepository.save(integrationHistory);
        credential.setIntegrationHistoryId(integrationHistory.getId());
        switch (credential.getDataType()) {
            case AppConstant.DataTypeInt.ORGANIZATION:
                integrationHistory = this.orgIntService.orgIntegration(credential);
                break;
            case AppConstant.DataTypeInt.POS_TERMINAL:
                integrationHistory = this.posTerminalIntService.posTerminalIntegration(credential);
                break;
            case AppConstant.DataTypeInt.PRODUCT_CATEGORY:
                integrationHistory = this.productIntegrationService.productCategoryIntegration(credential);
                break;
            case AppConstant.DataTypeInt.FLOOR:
                integrationHistory = this.floorIntegration.floorIntegration(credential);
                break;
            case AppConstant.DataTypeInt.TABLE:
                integrationHistory = this.tableIntegration.tableIntegration(credential);
                break;
            case AppConstant.DataTypeInt.CUSTOMER_VENDOR:
                integrationHistory = partnerIntegrationService.partnerIntegration(credential);
                break;
            case AppConstant.DataTypeInt.USER:
                integrationHistory = this.userIntegrationService.userIntegration(credential);
                break;
            case AppConstant.DataTypeInt.SALES_ORDER:
                integrationHistoryInfoDto = orderIntegration(credential);
                break;
            case AppConstant.DataTypeInt.WAREHOUSE:
                integrationHistory = this.warehouseIntService.warehouseIntegration(credential);
                break;
            case AppConstant.DataTypeInt.PRODUCT:
                integrationHistoryInfoDto = productIntegration(credential);
                if (!integrationHistoryInfoDto.getStatusIntegration().equals("INP")) {
                    integrationHistory.setIntStatus(integrationHistoryInfoDto.getStatusIntegration());
                    integrationHistory.setDescription(integrationHistoryInfoDto.getError());
                    this.integrationHistoryRepository.save(integrationHistory);
                }
                break;
            case AppConstant.DataTypeInt.PRICE_LIST:
                integrationHistoryInfoDto = priceListIntegrationService.syncPriceListErp(credential);
                break;
            case AppConstant.DataTypeInt.COUPON:
                integrationHistory = couPonService.syncCounponIntegration(credential);
                break;
            case AppConstant.DataTypeInt.KITCHEN_ORDER:
                integrationHistoryInfoDto = kitchenOrderIntegration(credential);
                break;
            default:
                throw new PosException(messageSource.getMessage("erp.integration.data.type.not.found", null, LocaleContextHolder.getLocale()));
        }
        if (integrationHistoryInfoDto != null) {
            integrationHistory.setIntStatus(integrationHistoryInfoDto.getStatusIntegration());
            integrationHistory.setPayload(integrationHistoryInfoDto.getPayload());
            integrationHistory.setResponse(integrationHistoryInfoDto.getResponse());
        }
        integrationHistory = integrationHistoryRepository.save(integrationHistory);
        IntegrationHistoryDto dto = modelMapper.map(integrationHistory, IntegrationHistoryDto.class);
        dto.setIntDate(integrationHistory.getIntDate() != null ? DateHelper.fromInstant(integrationHistory.getIntDate()) : DateHelper.fromInstant(Instant.now()));
        response.setData(dto);
        response.setMessage(messageSource.getMessage(integrationHistory.getIntStatus().equals("COM") ? "erp.integration.success" : "erp.integration.failed", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        if (integrationHistory.getIntStatus().equals("INP"))
            response.setMessage(messageSource.getMessage("erp.integration.inprogress", null, LocaleContextHolder.getLocale()));
        if (integrationHistory.getIntStatus().equals("FAI"))
            response.setErrors(integrationHistory.getDescription());
        return response;
    }

    public IntegrationHistoryInfoDto orderIntegration(SyncIntegrationCredential credential) {
        log.info("**ServiceImpl::layer -  order integration **");
        return orderIntegrationService.syncOrderIntegrationMain(credential);

    }

    public IntegrationHistoryInfoDto productIntegration(SyncIntegrationCredential credential) {
        log.info("**ServiceImpl::layer -  product integration **");

        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        credential.setSyncKafka("N");
        credential.setLineProduct(0);
        IntegrationHistoryInfoDto result = this.productIntegrationService.getProductErp(credential);
        return result;
    }

    public IntegrationHistoryInfoDto kitchenOrderIntegration(SyncIntegrationCredential credential) {
        log.info("**ServiceImpl::layer - kitchen order integration **");
        return kitchenOrderIntegrationService.syncKitchenOrderIntegrationMain(credential);

    }

    public boolean validateSynchronizationOrder(SyncIntegrationCredential credential)
    {
        // validate order
        // org - warehouse - user - posterminal - cusmter , vendor , partner - product category - product - price list - table - floor -
        Map<String,Integer> validateOrder = new HashMap<>();
        validateOrder.put(AppConstant.DataTypeInt.ORGANIZATION, 1);
        validateOrder.put(AppConstant.DataTypeInt.WAREHOUSE, 2);
        validateOrder.put(AppConstant.DataTypeInt.USER, 3);
        validateOrder.put(AppConstant.DataTypeInt.POS_TERMINAL, 4);
        validateOrder.put(AppConstant.DataTypeInt.PRODUCT_CATEGORY, 5);
        validateOrder.put(AppConstant.DataTypeInt.PRODUCT, 6);
        validateOrder.put(AppConstant.DataTypeInt.PRICE_LIST, 7);
        if(credential.getIntType().equals("INSERT"))
        {
            int  preOrder = 0;
            if(!validateOrder.containsKey(credential.getDataType()))
                System.out.println("không phải bước đồng bộ");
            int currentOrder = validateOrder.get(credential.getDataType());
            if(currentOrder< preOrder)
                System.out.println("Thứ tự đồng bộ không hợp lệ tại bước ");
        }
        return true;
    }
        public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }
}
