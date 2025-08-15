package com.dbiz.app.productservice.service.impl;

import com.ctc.wstx.util.ExceptionUtil;
import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.*;
import com.dbiz.app.productservice.domain.view.GetKcProductV;
import com.dbiz.app.productservice.domain.view.PcERequestV;
import com.dbiz.app.productservice.domain.view.ProductERequestV;
import com.dbiz.app.productservice.helper.Mapper.ProductMapper;
import com.dbiz.app.productservice.repository.*;
import com.dbiz.app.productservice.service.ProductIntService;
import com.dbiz.app.productservice.service.ProductService;
import com.dbiz.app.productservice.specification.ProductSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.integrationDto.IntegrationProductDto;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.dto.productDto.otherServiceDto.PcERequestVDto;
import org.common.dbiz.dto.productDto.otherServiceDto.ProductERequestVDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.externalRequest.SaveImageMDMRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.productRequest.PcErEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ProductIntServiceImpl implements ProductIntService {

    private final IProductRepository iProductRepository;

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;

    private final ProductCategoryRepository productCategoryRepository;

    private final UomRepository uomRepository;

    private final MessageSource messageSource;

    private final ProductComboRepository productComboRepository;

    private final AssignOrgRepository assignOrgRepository;

    private final PriceListRepository priceListRepository;

    private final PriceListProductRepository priceListProductRepository;

    private final TaxCategoryRepository taxCategoryRepository;

    private final TaxRepository taxRepository;

    private final WarehouseRepository warehouseRepository;

    private final ProductLocationRepository productLocationRepository;

    private final LocatorRepository locatorRepository;

    private final EntityManager entityManager;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final String GROUP_ID = "gr-sync-order";
//    private final static String TOPIC = "sync-integration-to-product";

//    private final static String TOPIC2 = "sync-product-to-integration";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     *
     * @return
     */
    @Override
    public GlobalReponse syncProductInt() {
        return null;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public String saveSigProduct(ProductIntDto itemProduct, Integer priceListId) {
        StringBuilder messageResponse = new StringBuilder();
        try {
            // xu ly uom
            Uom uomCheck = null;
            TaxCategory taxCategoryCheck = null;
            if (itemProduct.getUom() != null) {
                uomCheck = uomRepository.findByNameIgnoreCaseAndCodeIgnoreCase(itemProduct.getUom().getName(), itemProduct.getUom().getCode()).orElse(null);
                if (uomCheck == null) {
                    uomCheck = modelMapper.map(itemProduct.getUom(), Uom.class);
                    uomCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    uomCheck.setOrgId(0);
                    uomCheck = uomRepository.saveAndFlush(uomCheck);
                } else {
                    uomCheck.setErpUomId(itemProduct.getUom().getErpUomId());
                    uomCheck = uomRepository.saveAndFlush(uomCheck);
                }
            }
            Tax taxCheck = null;
            if (itemProduct.getTaxCategory() != null) {
                // xu ly tax category
//                taxCategoryCheck = taxCategoryRepository.findByErpTaxCategoryId(itemProduct.getTaxCategory().getErpTaxCategoryId()).orElse(null);
                taxCategoryCheck = taxCategoryRepository.findByNameIgnoreCase(itemProduct.getTaxCategory().getName()).orElse(null);
                if (taxCategoryCheck == null) {
                    taxCategoryCheck = modelMapper.map(itemProduct.getTaxCategory(), TaxCategory.class);
                    taxCategoryCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    taxCategoryCheck.setOrgId(0);
                    taxCategoryCheck.setIsDefault("N");
                    ;
                    taxCategoryCheck = taxCategoryRepository.saveAndFlush(taxCategoryCheck);
                }
//                taxCheck = taxRepository.findByErpTaxId(itemProduct.getTaxCategory().getTax().getErpTaxId()).orElse(null);
                taxCheck = taxRepository.findByNameIgnoreCase(itemProduct.getTaxCategory().getTax().getName()).orElse(null);
                if (taxCheck == null) {
                    taxCheck = modelMapper.map(itemProduct.getTaxCategory().getTax(), Tax.class);
                    taxCheck.setTaxCategoryId(taxCategoryCheck.getId());
                    taxCheck.setOrgId(0);
                    taxCheck.setIsDefault("N");
                    taxCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    taxCheck = taxRepository.saveAndFlush(taxCheck);
                } else {
                    taxCheck.setErpTaxId(itemProduct.getTaxCategory().getTax().getErpTaxId());
                    taxCheck = taxRepository.saveAndFlush(taxCheck);
                }
            }
            ProductCategory productCategoryCheck = null;
            if (itemProduct.getProductCategory() != null) {
                productCategoryCheck = productCategoryRepository.findByErpProductCategoryId(itemProduct.getProductCategory().getErpProductCategoryId());
                if (productCategoryCheck == null) {
                    productCategoryCheck = modelMapper.map(itemProduct.getProductCategory(), ProductCategory.class);
                    productCategoryCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    productCategoryCheck.setOrgId(0);

                    productCategoryCheck = productCategoryRepository.saveAndFlush(productCategoryCheck);
                }
            }


            Product productCheck = productRepository.findByErpProductId(itemProduct.getErpProductId()).orElse(null);
            if (productCheck == null) {
                Image image = null;
                if (itemProduct.getImage() != null) {
                    image = saveImage(ProductDto.builder()
                            .image(ImageDto.builder()
                                    .image64(itemProduct.getImage()
                                            .getImage64())
                                    .build())
                            .build());
                }

                productCheck = modelMapper.map(itemProduct, Product.class);
                productCheck.setUom(uomCheck);
                productCheck.setTaxId(taxCheck.getId());
                productCheck.setProductCategoryId(productCategoryCheck.getId());
                productCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                productCheck.setOrgId(0);
                productCheck.setImage(image);
                productCheck.setIsActive("Y");
                productCheck = productRepository.saveAndFlush(productCheck);
            } else {
                productCheck.setName(itemProduct.getName());
                productCheck.setCode(itemProduct.getCode());
                productCheck.setDescription(itemProduct.getDescription());
                productCheck.setIsPurchased(itemProduct.getIsPurchased());
                productCheck.setIsSales(itemProduct.getIsSales());
                productCheck.setIsStocked(itemProduct.getIsStocked());
                productCheck.setErpProductId(itemProduct.getErpProductId());
                productCheck.setGroupType(itemProduct.getGroupType());
                productCheck.setIsTopping(itemProduct.getIsTopping());
                productCheck.setUom(uomCheck);
                productCheck.setTaxId(taxCheck.getId());
                productCheck.setProductCategoryId(productCategoryCheck.getId());
                productCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                productCheck.setOrgId(0);
                productCheck.setIsActive("Y");
                if (itemProduct.getImage() != null) {
                    log.info("update Image {}", itemProduct.getImage().getImageCode());
                    Image image = saveImage(ProductDto.builder()
                            .image(ImageDto.builder()
                                    .image64(itemProduct.getImage().getImage64())
                                    .imageCode(productCheck.getImage() == null ? "" : productCheck.getImage().getImageCode())
                                    .id(productCheck.getImage() != null ? productCheck.getImage().getId() : null)
                                    .isActive("Y")
                                    .build())
                            .build());
                    productCheck.setImage(image);
                }
                productCheck = productRepository.saveAndFlush(productCheck);
            }
            // tao bang gia chung cho product lon
            PriceListProduct priceListProduct = priceListProductRepository.findAllPriceListProductByGeneralPriceListAndProductId(productCheck.getId(), AuditContext.getAuditInfo().getTenantId());
            if (priceListProduct == null) {
                priceListProduct = PriceListProduct.builder()
                        .productId(productCheck.getId())
                        .priceListId(priceListId)
                        .costPrice(BigDecimal.ZERO)
                        .salesPrice(BigDecimal.ZERO)
                        .lastOrderPrice(BigDecimal.ZERO)
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .orgId(0).build();
                priceListProductRepository.saveAndFlush(priceListProduct);
            }
            List<ProductLocationDto> productLocationDtos = itemProduct.getProductLocation();
            if (productLocationDtos != null && !productLocationDtos.isEmpty()) {
                this.productLocationRepository.deleteAllByProductId(productCheck.getId());
                //check locaton duplicate orgid
                Map<Integer, Long> idCountMap = productLocationDtos.stream()
                        .collect(Collectors.groupingBy(ProductLocationDto::getOrgId, Collectors.counting()));

                List<Integer> duplicateIds = idCountMap.entrySet().stream()
                        .filter(entry -> entry.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                 if(!duplicateIds.isEmpty())
                     messageResponse.append("Duplicate orgId in product location - ProductName: "+itemProduct.getName());
                //  end check

                for(int i = 0; i < productLocationDtos.size(); i++){
                    ProductLocationDto itemLocation = productLocationDtos.get(i);
                    if( !duplicateIds.isEmpty() && duplicateIds.contains(itemLocation.getOrgId()))
                        continue;
                    Warehouse warehouse = warehouseRepository.findByErpWarehouseId(itemLocation.getWarehouseId()).orElse(null);
                    if (warehouse == null) {
                        throw new PosException("warehouse not found: " + itemLocation.getWarehouseId());
                    }

                    StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                    List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                            .setParameter("orgId", itemLocation.getOrgId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    if (resultList.isEmpty())
                        throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
                    Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

                    ProductLocation productLocationCheck = null;

                    // check location cho product
                    if (itemLocation.getPosTerminalId() != null && itemLocation.getPosTerminalId() > 0) {
                        productLocationCheck = productLocationRepository.findByProductIdAndWarehouseIdAndPosTerminalId(productCheck.getId(), warehouse.getId(), itemLocation.getPosTerminalId()).orElse(null);
                    } else {
                        productLocationCheck = productLocationRepository.findByProductIdAndWarehouseId(productCheck.getId(), warehouse.getId()).orElse(null);
                    }

                    if (productLocationCheck == null) {
                        Locator locator = this.locatorRepository.findByWarehouseIdAndIsDefault(warehouse.getId(), "Y").orElse(null);

                        ProductLocation productLocation = ProductLocation.builder()
                                .productId(productCheck.getId())
                                .warehouseId(warehouse.getId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(orgDtoId)
                                .isDefault(itemLocation.getIsDefault())
                                .stockQty(new BigDecimal(0))
                                .build();
                        if(locator != null)
                            productLocation.setLocatorId(locator.getId());
//                        }
                        if(itemLocation.getPosTerminalId() != null && itemLocation.getPosTerminalId() > 0)
                            productLocation.setPosTerminalId( itemLocation.getPosTerminalId());
                        productLocationCheck =  productLocationRepository.saveAndFlush(productLocation);
                    } else {
                        productLocationCheck.setIsActive(itemLocation.getIsActive());
                        productLocationCheck.setWarehouseId(warehouse.getId());
                        productLocationCheck.setOrgId(orgDtoId);
                        productLocationCheck.setPosTerminalId(itemLocation.getPosTerminalId() == 0 ? null : itemLocation.getPosTerminalId());
                        productLocationCheck.setIsDefault(itemLocation.getIsDefault());
                        productLocationRepository.saveAndFlush(productLocationCheck);
                    }
                    productLocationDtos.set(i, modelMapper.map(productLocationCheck, ProductLocationDto.class));
                }
                //
                if (itemProduct.getIsStocked().equals("Y") && productLocationDtos != null && !productLocationDtos.isEmpty()) {
                    Integer productIdSave = productCheck.getId();
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                    headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                    headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                    headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                    headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());

                    productLocationDtos.stream().map(
                            item -> {
                                item.setProductId(productIdSave);
                                return item;
                            }
                    ).collect(Collectors.toList());
                    TransactionDto transactionDto = TransactionDto.builder()
                            .productLocationDtos(productLocationDtos)
                            .build();
                    HttpEntity<TransactionDto> requestEntity = new HttpEntity<>(transactionDto, headers);
                    GlobalReponse responseTransaction = this.restTemplate
                            .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION,
                                    requestEntity,
                                    GlobalReponse.class)
                            .getBody();
                    if (responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                            && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
                        throw new RuntimeException(responseTransaction.getMessage());
                    }
                }


            }

            if (itemProduct.getAssignOrg() != null) {
                for (AssignOrgProductDto itemOrg : itemProduct.getAssignOrg()) {
                    StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                    List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                            .setParameter("orgId", itemOrg.getOrgId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    if (resultList.isEmpty())
                        throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
                    Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

                    AssignOrgProduct assignOrgCheck = assignOrgRepository.getByOrgIdAndProductId(orgDtoId, productCheck.getId()).orElse(null);
                    if (assignOrgCheck == null) {
                        AssignOrgProduct assignOrgProduct = AssignOrgProduct.builder()
                                .productId(productCheck.getId())
                                .orgId(orgDtoId)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        assignOrgRepository.saveAndFlush(assignOrgProduct);
                    } else {
                        assignOrgCheck.setOrgId(orgDtoId);
                        assignOrgCheck.setIsActive("Y");
                        assignOrgRepository.saveAndFlush(assignOrgCheck);
                    }

                }
            }


            if (itemProduct.getProductCombo() != null) {
//                productComboRepository.updateAllByProductId(productCheck.getId(), "Y");
                for (ProductComboIntDto itemCombo : itemProduct.getProductCombo()) {
                    Product productComponent = productRepository.findByErpProductId(itemCombo.getProductComponentId()).orElse(null);
                    if (productComponent != null) {
                        ProductCombo comboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(productCheck.getId(), productComponent.getId(), "Y").orElse(null);
                        this.productComboRepository.updateAllByProductId(productCheck.getId(), "Y");
                        if (comboCheck == null) {
                            ProductCombo combo = ProductCombo.builder()
                                    .productId(productCheck.getId())
                                    .productComponentId(productComponent.getId())
                                    .sequence(itemCombo.getSequence())
                                    .orgId(0)
                                    .isItem("Y")
                                    .qty(itemCombo.getQty())
                                    .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                            productComboRepository.saveAndFlush(combo);
                        } else {
                            comboCheck.setProductId(productCheck.getId());
                            comboCheck.setProductComponentId(productComponent.getId());
                            comboCheck.setSequence(itemCombo.getSequence());
                            comboCheck.setQty(itemCombo.getQty());
                            productComboRepository.saveAndFlush(comboCheck);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error save Product : " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            Throwable root = e;
            while (root.getCause() != null) {
                return ExceptionUtils.getRootCauseMessage(e);
            }

            return e.getMessage();
        }
        return messageResponse.toString();
    }

    /**
     *
     * @param productIntDto
     * @return
     */
    @Override
    public GlobalReponse saveInterfaceProduct(ProductIntDto productIntDto) {
        IProduct iProduct = IProduct.builder()
                .erpProductId(productIntDto.getErpProductId())
                .errorMessage("Error")
                .tenantId(0)
                .orgId(0)
                .productCategoryId(productIntDto.getProductCategory().getId())
                .build();
        this.iProductRepository.save(iProduct);
        return GlobalReponse.builder()
                .data(iProduct)
                .message("Save success")
                .status(HttpStatus.OK.value()).build();
    }

    @Transactional
    public Image saveImage(ProductDto productDto) {
        log.info("save Image ");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + ""); // Thêm tenantId vào header
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        GlobalReponse callExService = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_ORG_TENANT_CODE + "/" + com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId() + "/" + AuditContext.getAuditInfo().getOrgId(), GlobalReponse.class);

        GlobalReponse callExService = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.GET_ORG_TENANT_CODE + "/"
                        + AuditContext.getAuditInfo().getTenantId() + "/"
                        + AuditContext.getAuditInfo().getOrgId(),
                HttpMethod.GET,
                entity,
                GlobalReponse.class
        ).getBody();


        TenantAndOrgDto tenantAndOrgDto = modelMapper.map(callExService.getData(), TenantAndOrgDto.class);
        SaveImageMDMRequest mdmRequest = new SaveImageMDMRequest();
        mdmRequest.setTenantCode(tenantAndOrgDto.getCodeTenant());
        mdmRequest.setBranchCode(tenantAndOrgDto.getCodeIndustry());
        mdmRequest.setImage64(productDto.getImage().getImage64());
        mdmRequest.setImageCode(productDto.getImage().getImageCode());
        callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_HOST_FINDBYNAME + AppConstant.NameSystemConfig.MDM_URL_SAVE_IMAGE, HttpMethod.GET, entity, GlobalReponse.class).getBody();
        String urlMDM = String.valueOf(callExService.getData());
        Map<String, Object> finalRequest = new HashMap<>();
        finalRequest.put("data", List.of(mdmRequest));

        HttpHeaders headersPost = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(finalRequest, headersPost);

        ResponseEntity<String> response = externalRestTemplate.exchange(urlMDM, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode root = objectMapper.readTree(responseBody);

                String resultCode = root.path("resultCode").asText();
                String errorMessage = root.path("errorMessage").asText(null);
                JsonNode data = root.path("data");
                log.info("resultCode: " + resultCode);
                log.info("errorMessage: " + errorMessage);
                log.info("data: " + data);

                if (resultCode.equals("00") && data.isArray()) {
                    for (JsonNode item : data) {
                        String urlValue = item.path("url").asText();
                        String imageCode = item.path("imageCode").asText();

                        Image image = Image.builder()
                                .imageUrl(urlValue)
                                .imageCode(imageCode)
                                .isActive("Y")
                                .dTenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        if (productDto.getImage().getId() != null)
                            image.setId(productDto.getImage().getId());

                        image = imageRepository.save(image);
                        return image;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API call failed with status code: " + response.getStatusCode());
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProduct(ProductIntDto itemProduct, Integer priceListId) {

    }
//    @KafkaListener(groupId = GROUP_ID, topics = TOPIC, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessage(ConsumerRecord<String, ProductIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
////        log.info("Received message: " + consumerRecord.value());
//        try {
//            String key = consumerRecord.key(); // could be null
//            ProductIntKafkaDto value = consumerRecord.value();
//
//            try {
//                int tenantNumbers = getTenantNumbers();
//                if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                    Map<Object, Object> configuredDataSources = dataSourceConfigService
//                            .configureDataSources();
//
//                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//                }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (value.getTenantId() != 0) {
//                dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//            } else {
//                dataSourceContextHolder.setCurrentTenantId(null);
//
//            }
//            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                    "0", 0, "vi", value.getTenantId()));
//
//            if ( value.getLastPage().equals("N")) {
//                log.info("Received message:");
//                log.info("Key: " + key);
////                log.info("Value: " + value);
//                this.intProductKalka(value.getProductIntDtoList(), value.getSyncIntegrationCredential());
//            } else {
//                log.info("product integration laspage ");
//                IntegrationProductDto result = new IntegrationProductDto();
//                result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
//                result.setLastPage("Y");
//                result.setSyncIntegrationCredential(value.getSyncIntegrationCredential());
//                kafkaTemplate.send(TOPIC2, result);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        acknowledgment.acknowledge();
//    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }


//    @Transactional
//    public void intProductKalka(List<ProductIntDto> request, SyncIntegrationCredential value) {
//        IntegrationProductDto result = new IntegrationProductDto();
//        result.setError("");
//        result.setStatusIntegration("COM");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
//        log.info("maintenantId {}", AuditContext.getAuditInfo().getMainTenantId());
//        log.info("tenantId {}", AuditContext.getAuditInfo().getTenantId());
//        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
//        int rsInsertProduct = 0;
//        int rsUpdateProduct = 0;
//        int rsInsertImage = 0;
//        int rsUpdateImage = 0;
//        int rsFail = 0;
//
//        log.info("*** List<ProductDto>, service; save int product  *");
//        Integer priceListId = priceListRepository.findPriceListByGeneralPriceList("Y", AuditContext.getAuditInfo().getTenantId(), 0);
//        log.info("priceListId {}", priceListId);
////        Integer priceListId2 = this.priceListRepository.findPriceListByGeneralPriceList("Y");
////        log.info("priceListId2 {}",priceListId2);
//        try {
//            List<ProductIntDto> listProduct = new ArrayList<>();
//            for (ProductIntDto itemProduct : request) {
//                LocalDateTime startTime = LocalDateTime.now();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//                log.info("Start processing Product [{}] at {}", itemProduct.getName(), startTime.format(formatter));
//                // xu ly uom
////             try{
//                Uom uomCheck = null;
//                TaxCategory taxCategoryCheck = null;
//                if (itemProduct.getUom() != null) {
////                    uomCheck = uomRepository.findByErpUomId(itemProduct.getUom().getErpUomId()).orElse(null);
//                    uomCheck = uomRepository.findByNameIgnoreCaseAndCodeIgnoreCase(itemProduct.getUom().getName(),itemProduct.getUom().getCode()).orElse(null);
//                    if (uomCheck == null) {
//                        uomCheck = modelMapper.map(itemProduct.getUom(), Uom.class);
//                        uomCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                        uomCheck.setOrgId(0);
//                        uomCheck = uomRepository.save(uomCheck);
//                    }else{
//                        uomCheck.setErpUomId(itemProduct.getUom().getErpUomId());
//                        uomCheck = uomRepository.save(uomCheck);
//                    }
//                }
//                Tax taxCheck = null;
//                if (itemProduct.getTaxCategory() != null) {
//                    // xu ly tax category
//                    taxCategoryCheck = taxCategoryRepository.findByNameIgnoreCase(itemProduct.getTaxCategory().getName()).orElse(null);
//                    if (taxCategoryCheck == null) {
//                        taxCategoryCheck = modelMapper.map(itemProduct.getTaxCategory(), TaxCategory.class);
//                        taxCategoryCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                        taxCategoryCheck.setOrgId(0);
//                        taxCategoryCheck.setIsDefault("N");
//                        ;
//                        taxCategoryCheck = taxCategoryRepository.save(taxCategoryCheck);
//                    }
//                    taxCheck = taxRepository.findByNameIgnoreCase(itemProduct.getTaxCategory().getTax().getName()).orElse(null);
//                    if (taxCheck == null) {
//                        taxCheck = modelMapper.map(itemProduct.getTaxCategory().getTax(), Tax.class);
//                        taxCheck.setTaxCategoryId(taxCategoryCheck.getId());
//                        taxCheck.setOrgId(0);
//                        taxCheck.setIsDefault("N");
//                        taxCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                        taxCheck = taxRepository.save(taxCheck);
//                    }
//                }
//                ProductCategory productCategoryCheck = null;
//                if (itemProduct.getProductCategory() != null) {
//                    productCategoryCheck = productCategoryRepository.findByErpProductCategoryId(itemProduct.getProductCategory().getErpProductCategoryId());
//                    if (productCategoryCheck == null) {
//                        productCategoryCheck = modelMapper.map(itemProduct.getProductCategory(), ProductCategory.class);
//                        productCategoryCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                        productCategoryCheck.setOrgId(0);
//
//                        productCategoryCheck = productCategoryRepository.save(productCategoryCheck);
//                    }
//                }
//
//
//                Product productCheck = productRepository.findByErpProductId(itemProduct.getErpProductId()).orElse(null);
//                if (productCheck == null) {
//                    rsInsertProduct++;
//                    Image image = null;
//                    if (itemProduct.getImage() != null) {
//                        rsInsertImage++;
//                        image = saveImage(ProductDto.builder()
//                                .image(ImageDto.builder()
//                                        .image64(itemProduct.getImage()
//                                                .getImage64())
//                                        .build())
//                                .build());
//                    }
//
//                    productCheck = modelMapper.map(itemProduct, Product.class);
//                    productCheck.setUom(uomCheck);
//                    productCheck.setTaxId(taxCheck.getId());
//                    productCheck.setProductCategoryId(productCategoryCheck.getId());
//                    productCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                    productCheck.setOrgId(0);
//                    productCheck.setImage(image);
//                    productCheck.setIsActive("Y");
//                    productCheck = productRepository.save(productCheck);
//                } else {
//                    rsUpdateProduct++ ;
////                    productCheck.setName(itemProduct.getName());
////                    productCheck.setCode(itemProduct.getCode());
////                    productCheck.setDescription(itemProduct.getDescription());
////                    productCheck.setIsPurchased(itemProduct.getIsPurchased());
////                    productCheck.setIsSales(itemProduct.getIsSales());
////                    productCheck.setIsStocked(itemProduct.getIsStocked());
////                    productCheck.setErpProductId(itemProduct.getErpProductId());
////                    productCheck.setGroupType(itemProduct.getGroupType());
////                    productCheck.setIsTopping(itemProduct.getIsTopping());
////                    productCheck.setUom(uomCheck);
////                    productCheck.setTaxId(taxCheck.getId());
////                    productCheck.setProductCategoryId(productCategoryCheck.getId());
////                    productCheck.setIsActive("Y");
////                    if (itemProduct.getImage() != null)
////                        log.info("update Image {}", itemProduct.getImage().getImageCode());
////                    if (itemProduct.getImage() != null) {
////                        log.info("update Image {}", itemProduct.getImage().getImageCode());
////                        rsUpdateImage++;
////                        Image image = saveImage(ProductDto.builder()
////                                .image(ImageDto.builder()
////                                        .image64(itemProduct.getImage().getImage64())
////                                        .imageCode(productCheck.getImage() == null ? "" : productCheck.getImage().getImageCode())
////                                        .id(productCheck.getImage() != null ? productCheck.getImage().getId() : null)
////                                        .isActive("Y")
////                                        .build())
////                                .build());
////                        productCheck.setImage(image);
////                    }
//                    int rs = productRepository.updateProductDetails(productCheck.getId(),itemProduct.getName(), itemProduct.getCode(), itemProduct.getSaleprice(), itemProduct.getDescription(),
//                            itemProduct.getIsPurchased(), itemProduct.getIsSales(), itemProduct.getIsStocked(), itemProduct.getErpProductId(), itemProduct.getGroupType(), itemProduct.getIsTopping(), uomCheck,
//                            taxCheck.getId(), productCategoryCheck.getId(), "Y", productCheck.getImage());
//                }
//                // tao bang gia chung cho product lon
//                PriceListProduct priceListProduct = priceListProductRepository.findAllPriceListProductByGeneralPriceListAndProductId(productCheck.getId(), AuditContext.getAuditInfo().getTenantId());
//                if (priceListProduct == null) {
//                    priceListProduct = PriceListProduct.builder()
//                            .productId(productCheck.getId())
//                            .priceListId(priceListId)
//                            .costPrice(BigDecimal.ZERO)
//                            .salesPrice(BigDecimal.ZERO)
//                            .lastOrderPrice(BigDecimal.ZERO)
//                            .tenantId(AuditContext.getAuditInfo().getTenantId())
//                            .orgId(0).build();
//                    priceListProductRepository.save(priceListProduct);
//                }
//
//                if (itemProduct.getProductLocation() != null) {
////                productLocationRepository.updateIsActiveAllByProductId(productCheck.getId());
//                    for (ProductLocationDto itemLocation : itemProduct.getProductLocation()) {
//                        Warehouse warehouse = warehouseRepository.findByErpWarehouseId(itemLocation.getWarehouseId()).orElse(null);
//                        if (warehouse == null) {
//                            throw new PosException("warehouse not found: " + itemLocation.getWarehouseId());
//                        }
////                        GlobalReponse exresponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_BY_ORGERP + "/" + itemLocation.getOrgId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
////                        if (exresponse.getStatus() != HttpStatus.OK.value()) {
////                            throw new PosException("Org not found");
////                        }
////                        OrgDto orgDto = modelMapper.map(exresponse.getData(), OrgDto.class);
//
//                        StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
//                        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
//                                .setParameter("orgId", itemLocation.getOrgId())
//                                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                                .unwrap(NativeQuery.class)
//                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                                .getResultList();
//                        if (resultList.isEmpty())
//                            throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
//                        Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));
//
//                        ProductLocation productLocationCheck = null;
//                        if (itemLocation.getPosTerminalId() != null && itemLocation.getPosTerminalId() > 0) {
//                            productLocationCheck = productLocationRepository.findByProductIdAndWarehouseIdAndPosTerminalId(productCheck.getId(), warehouse.getId(), itemLocation.getPosTerminalId()).orElse(null);
//                        } else {
//                            productLocationCheck = productLocationRepository.findByProductIdAndWarehouseId(productCheck.getId(), warehouse.getId()).orElse(null);
//                        }
//
//                        if (productLocationCheck == null) {
//                            ProductLocation productLocation = ProductLocation.builder()
//                                    .productId(productCheck.getId())
//                                    .warehouseId(warehouse.getId())
//                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
//                                    .orgId(orgDtoId)
//                                    .isDefault("N")
//                                    .build();
////                            if (itemLocation.getPosTerminalId() != null && itemLocation.getPosTerminalId() > 0) {
//////                                exresponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_POSTERMINAL_BY_ERP_ID + "/" + itemLocation.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//////                                if (exresponse.getStatus() != HttpStatus.OK.value()) {
//////                                    throw new PosException("PosTerminal not found");
//////                                }
//////                                PosTerminalDto posTerminalDto = modelMapper.map(exresponse.getData(), PosTerminalDto.class);
////                                sqlGetEntity = new StringBuilder("select d_pos_terminal_id from d_pos_terminal where erp_pos_id = :posTerminalId and d_tenant_id = :tenantId");
////                                resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
////                                        .setParameter("posTerminalId", itemLocation.getPosTerminalId())
////                                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
////                                        .unwrap(NativeQuery.class)
////                                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
////                                        .getResultList();
////                                if (resultList.size() == 0)
////                                    throw new PosException(String.format(messageSource.getMessage("posterminal.not.sync", null, LocaleContextHolder.getLocale()), itemLocation.getPosTerminalId()));
////                                Integer posTerminalId =  ParseHelper.INT.parse(resultList.get(0).get("d_pos_terminal_id"));
////
////                                productLocation.setPosTerminalId(posTerminalId);
////                            }
//
//                            productLocationRepository.save(productLocation);
//                        } else {
//                            productLocationCheck.setIsActive(itemLocation.getIsActive());
//                            productLocationCheck.setWarehouseId(warehouse.getId());
//                            productLocationCheck.setOrgId(orgDtoId);
//                            productLocationCheck.setPosTerminalId(itemLocation.getPosTerminalId() == 0 ? null : itemLocation.getPosTerminalId());
//                            productLocationRepository.save(productLocationCheck);
//                        }
//
//
//                    }
//                }
//
//                if (itemProduct.getAssignOrg() != null) {
////                assignOrgRepository.deleteAllByProductId(productCheck.getId());
////                assignOrgRepository.updateIsActiveAllByProductId(productCheck.getId());
//                    for (AssignOrgProductDto itemOrg : itemProduct.getAssignOrg()) {
////                        GlobalReponse exresponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_BY_ORGERP + "/" + itemOrg.getOrgId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
////                        if (exresponse.getStatus() != HttpStatus.OK.value()) {
////                            throw new PosException("Org not found");
////                        }
////                        OrgDto orgDto = modelMapper.map(exresponse.getData(), OrgDto.class);
//
//                        StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
//                        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
//                                .setParameter("orgId", itemOrg.getOrgId())
//                                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                                .unwrap(NativeQuery.class)
//                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                                .getResultList();
//                        if (resultList.isEmpty())
//                            throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
//                        Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));
//
//                        AssignOrgProduct assignOrgCheck = assignOrgRepository.getByOrgIdAndProductId(orgDtoId, productCheck.getId()).orElse(null);
//                        if (assignOrgCheck == null) {
//                            AssignOrgProduct assignOrgProduct = AssignOrgProduct.builder()
//                                    .productId(productCheck.getId())
//                                    .orgId(orgDtoId)
//                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
//                                    .build();
//                            assignOrgRepository.save(assignOrgProduct);
//                        } else {
//                            assignOrgCheck.setOrgId(orgDtoId);
//                            assignOrgCheck.setIsActive("Y");
//                            assignOrgRepository.save(assignOrgCheck);
//                        }
//
//                    }
//                }
//
//                if (itemProduct.getProductCombo() != null) {
////                productComboRepository.updateAllByProductId(productCheck.getId(), "Y");
//                    for (ProductComboIntDto itemCombo : itemProduct.getProductCombo()) {
//                        Product productComponent = productRepository.findByErpProductId(itemCombo.getProductComponentId()).orElse(null);
//                        if (productComponent != null) {
//                            ProductCombo comboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(productCheck.getId(), productComponent.getId(), "Y").orElse(null);
//                            this.productComboRepository.updateAllByProductId(productCheck.getId(), "Y");
//                            if (comboCheck == null) {
//                                ProductCombo combo = ProductCombo.builder()
//                                        .productId(productCheck.getId())
//                                        .productComponentId(productComponent.getId())
//                                        .sequence(itemCombo.getSequence())
//                                        .orgId(0)
//                                        .isItem("Y")
//                                        .qty(itemCombo.getQty())
//                                        .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
//                                productComboRepository.save(combo);
//                            } else {
//                                comboCheck.setProductId(productCheck.getId());
//                                comboCheck.setProductComponentId(productComponent.getId());
//                                comboCheck.setSequence(itemCombo.getSequence());
//                                comboCheck.setQty(itemCombo.getQty());
//                                productComboRepository.save(comboCheck);
//                            }
//                        }
//
//                    }
//                }
//                LocalDateTime endTime = LocalDateTime.now();
//                log.info("Finished processing Product [{}] at {}", itemProduct.getName(), endTime.format(formatter));
//                listProduct.add(itemProduct);
////             }catch (Exception e){
////                 e.printStackTrace();
////                 result.setError(e.getMessage());
////                 result.setStatusIntegration("FAI");
////                 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////             }
//            }
//        } catch (Exception e)
//        {
//            rsFail++;
//            e.printStackTrace();
//            result.setError(e.getMessage());
//            result.setStatusIntegration("FAI");
//            result.setLastPage("Y");
//        }
//        log.info("rsInsertProduct {}", rsInsertProduct + " - rsUpdateProduct " + rsUpdateProduct + " - rsInsertImage " + rsInsertImage + " - rsUpdateImage " + rsUpdateImage);
//        log.info("rsFail {}", rsFail);
//        result.setSyncIntegrationCredential(value);
//        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
//        result.setLastPage("N");
//        kafkaTemplate.send(TOPIC2, result);
//    }

}









