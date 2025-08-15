package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.Locator;
import com.dbiz.app.productservice.domain.ProductLocation;
import com.dbiz.app.productservice.repository.LocatorRepository;
import com.dbiz.app.productservice.repository.ProductLocationRepository;
import com.dbiz.app.productservice.service.ProductLocationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.productDto.ProductLocationDto;
import org.common.dbiz.dto.productDto.SaveProductLocationDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductLocationServiceImpl implements ProductLocationService {

    private final ProductLocationRepository productLocationRepository;
    private final LocatorRepository locatorRepository;
    private final EntityManager entityManager;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public GlobalReponse deleteById(Integer productLocationId) {
        log.info("*** ProductLocation, service; delete ProductLocation ***");

        ProductLocation productLocation = productLocationRepository.findById(productLocationId)
                .orElseThrow(() -> new PosException(messageSource.getMessage("productLocation.not.found", null, LocaleContextHolder.getLocale())));

        if (isProductLocationHasTransactions(productLocation.getProductId(), productLocation.getWarehouseId())) {
            throw new PosException(messageSource.getMessage("productLocation.has.transactions", null, LocaleContextHolder.getLocale()));
        }

        productLocationRepository.deleteById(productLocationId);
        return GlobalReponse.builder().message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponse saveProductLocation(SaveProductLocationDto request) {

        log.info("*** ProductDto List, service; save product location *");
        try{
            if (request.getListProductLocation() != null && !request.getListProductLocation().isEmpty()) {
                Map<Integer,String> validateOrgAndDefaultWarehouse = new HashMap<>();
                Map<Integer,Integer> validateUniqueWarehouse = new HashMap<>();
                for (ProductLocationDto item : request.getListProductLocation()) {
                    Optional<Locator> locator = locatorRepository.findByWarehouseIdAndIsDefault(item.getWarehouseId(), "Y");
                    log.info("location {}", locator);
                    if(locator.isEmpty()){
                        throw new PosException(messageSource.getMessage("location.only.default", null, LocaleContextHolder.getLocale()));
                    }

                    ProductLocation check = productLocationRepository.findByProductIdAndWarehouseIdAndLocatorIdAndOrgId(request.getProductId(), item.getWarehouseId(),
                            locator.get().getId(), item.getOrgId()).orElse(null);

                    if(validateOrgAndDefaultWarehouse.containsKey(item.getOrgId())) {
                        if (item.getIsDefault() != null && item.getIsDefault().equals("Y")) {
                            throw new PosException(messageSource.getMessage("location.only.default", null, LocaleContextHolder.getLocale()));
                        }
                    }else{
                        if(item.getIsDefault() != null && item.getIsDefault().equals("Y")) {
                            validateOrgAndDefaultWarehouse.put(item.getOrgId(), item.getIsDefault());
                        }
                    }


                    if(validateUniqueWarehouse.containsKey(item.getWarehouseId())) {
                        throw new PosException(messageSource.getMessage("warehouse.duplicate", null, LocaleContextHolder.getLocale()));
                    }else{
                        validateUniqueWarehouse.put(item.getWarehouseId(), item.getWarehouseId());
                    }

                    if (check == null) {
                        ProductLocation productLocation = ProductLocation.builder()
                                .productId(request.getProductId())
                                .warehouseId(item.getWarehouseId())
//                                .posTerminalId(item.getPosTerminalId())
                                .locatorId(locator.get().getId())
                                .orgId(item.getOrgId())
                                .isDefault(item.getIsDefault())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        productLocation.setIsActive(item.getIsActive());
                        productLocationRepository.saveAndFlush(productLocation);
                    } else {
                        check.setIsActive(item.getIsActive());
                        check.setProductId(request.getProductId());
                        check.setWarehouseId(item.getWarehouseId());
//                        check.setPosTerminalId(item.getPosTerminalId());
                        check.setLocatorId(locator.get().getId());
                        check.setOrgId(item.getOrgId());
                        check.setIsDefault(item.getIsDefault());
//                    check.setLocatorId(item.getLocatorId());
                        productLocationRepository.saveAndFlush(check);

                    }
                    item.setIsSysDefault("N");
                }
            }else{

                // insert default warehouse
                if(request.getIsFirstCreation().equals("Y")){
                    ProductLocation productLocation = ProductLocation.builder()
                            .productId(request.getProductId())
                            .warehouseId(0)
                            .orgId(0)
                            .locatorId(0)
                            .isDefault("Y")
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    productLocation.setIsActive("Y");
                    productLocation =  productLocationRepository.saveAndFlush(productLocation);
                    ProductLocationDto productLocationDto = modelMapper.map(productLocation, ProductLocationDto.class);
                    productLocationDto.setStockQty(BigDecimal.ZERO);
                    productLocationDto.setIsSysDefault("Y");
                    productLocationDto.setMinQty(null);
                    productLocationDto.setMaxQty(null);
                    request.setListProductLocation(new ArrayList<>());
                    request.getListProductLocation().add(productLocationDto);
                }
            }

            //Handle transaction
            if(request.getListProductLocation() != null && !request.getListProductLocation().isEmpty()){

                HttpHeaders headers = new HttpHeaders();
                headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
                headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());

                request.getListProductLocation().stream().map(
                        item -> {
                            item.setProductId(request.getProductId());
                            return item;
                        }
                ).collect(Collectors.toList());
                TransactionDto transactionDto = TransactionDto.builder()
                        .productLocationDtos(request.getListProductLocation())
                        .build();
                HttpEntity<TransactionDto> requestEntity = new HttpEntity<>(transactionDto, headers);
                GlobalReponse responseTransaction = this.restTemplate
                        .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION ,
                                requestEntity,
                                GlobalReponse.class)
                        .getBody();
                if(responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                        && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
                    throw new RuntimeException(responseTransaction.getMessage());
                }
            }
        }catch (Exception e){
            log.error("Error: ", e);
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse saveInternalProductLocation(SaveProductLocationDto request) {

        log.info("*** ProductDto List, service; save Internal product location *");
        try{
            if (request.getListProductLocation() != null && !request.getListProductLocation().isEmpty()) {
                Map<Integer,String> validateOrgAndDefaultWarehouse = new HashMap<>();
                Map<Integer,Integer> validateUniqueWarehouse = new HashMap<>();
                for (ProductLocationDto item : request.getListProductLocation()) {

                    if(validateOrgAndDefaultWarehouse.containsKey(item.getOrgId())) {
                        if (item.getIsDefault() != null && item.getIsDefault().equals("Y")) {
                            throw new PosException(messageSource.getMessage("location.only.default", null, LocaleContextHolder.getLocale()));
                        }
                    }else{
                        if(item.getIsDefault() != null && item.getIsDefault().equals("Y")) {
                            validateOrgAndDefaultWarehouse.put(item.getOrgId(), item.getIsDefault());
                        }
                    }


                    if(validateUniqueWarehouse.containsKey(item.getWarehouseId())) {
                        throw new PosException(messageSource.getMessage("warehouse.duplicate", null, LocaleContextHolder.getLocale()));
                    }else{
                        validateUniqueWarehouse.put(item.getWarehouseId(), item.getWarehouseId());
                    }

                    ProductLocation productLocation = ProductLocation.builder()
                            .productId(request.getProductId())
                            .warehouseId(item.getWarehouseId())
//                                .posTerminalId(item.getPosTerminalId())
                            .locatorId(item.getLocatorId())
                            .orgId(item.getOrgId())
                            .isDefault(item.getIsDefault())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    productLocation.setIsActive(item.getIsActive());
                    productLocationRepository.saveAndFlush(productLocation);
                }
            }

        }catch (Exception e){
            log.error("Error: ", e);
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    public boolean isProductLocationHasTransactions(Integer productId, Integer warehouseId) {
        try {
            String sql = "SELECT 1 " +
                    " FROM pos.d_transaction WHERE d_tenant_id = :tenantId " +
                    " AND d_product_id = :productId AND d_warehouse_id = :warehouseId  limit 1";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("productId", productId)
                    .setParameter("warehouseId", warehouseId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                return true;
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return false;
    }
}
