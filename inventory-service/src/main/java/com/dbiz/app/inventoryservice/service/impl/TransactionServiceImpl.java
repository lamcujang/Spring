package com.dbiz.app.inventoryservice.service.impl;

import com.dbiz.app.inventoryservice.constant.AppConstant;
import com.dbiz.app.inventoryservice.domain.StorageOnhand;
import com.dbiz.app.inventoryservice.domain.Transaction;
import com.dbiz.app.inventoryservice.repository.StorageOnhandRepository;
import com.dbiz.app.inventoryservice.repository.TransactionRepository;
import com.dbiz.app.inventoryservice.service.TransactionService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.*;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.dto.orderDto.ProductionLineDto;
import org.common.dbiz.dto.productDto.ProductCDto;
import org.common.dbiz.dto.productDto.ProductLocationDto;
import org.common.dbiz.dto.productDto.SaveProductLocationDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j

public class TransactionServiceImpl implements TransactionService {

    private final EntityManager entityManager;
    private final TransactionRepository transactionRepository;
    private final StorageOnhandRepository storageOnHandRepository;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;

    @Override
    public GlobalReponse createTransaction(TransactionDto transactionDto) {

        log.info("TransactionServiceImpl.createTransaction() - transactionDto: {}", transactionDto);
        GlobalReponse response = new GlobalReponse();
        String result = null;
        StockError lotOfStockError = null;
        if(transactionDto.getPurchaseOrderIds() != null && !transactionDto.getPurchaseOrderIds().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Purchase Order");
            result = createPurchaseOrderTransaction(transactionDto);
        }else if (transactionDto.getProductionIds() != null && !transactionDto.getProductionIds().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Production");
            result = createProductionTransaction(transactionDto);
        }else if(transactionDto.getPosOrderIds() != null && !transactionDto.getPosOrderIds().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Pos Order");
            lotOfStockError = createPosOrderTransaction(transactionDto);
        }else if(transactionDto.getReturnOrderIds() != null && !transactionDto.getReturnOrderIds().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Return Order");
            lotOfStockError = createReturnOrderTransaction(transactionDto);

        }else if(transactionDto.getKitchenOrderLineIds() != null && !transactionDto.getKitchenOrderLineIds().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Kitchen Order Line");
            result = createKitchenOrderLineTransaction(transactionDto);
        }else if(transactionDto.getProductLocationDtos() != null && !transactionDto.getProductLocationDtos().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Product Location");
            result = createProductLocationTransaction(transactionDto);
        }else if (transactionDto.getProductionDtos() != null && !transactionDto.getProductionDtos().isEmpty()) {
            log.info("TransactionServiceImpl.createTransaction() - Production with body");
            result = createProductionTransactionWithBody(transactionDto);
        }

        if(result != null ){
            response.setMessage(result);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }else if(lotOfStockError != null){
            if(lotOfStockError instanceof LotStockError){
                response.setData(LotOfStockRespDto.builder().lotError((LotStockError)lotOfStockError).build());
            }
            response.setMessage(lotOfStockError.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }else{
            response.setMessage("Transaction created successfully");
            response.setStatus(HttpStatus.OK.value());
        }
        return response;
    }

    @Override
    public GlobalReponsePagination findAllTransaction(TransactionParamDto dto) {
        log.info("TransactionServiceImpl.findAllTransaction() - transactionParamDto: {}", dto);

        StringBuilder sql = new StringBuilder(
                        "select " +
                        " d_tenant_id, " +
                        " d_org_id, " +
                        " org_name, " +
                        " d_transaction_id , " +
                        " is_active , " +
                        " qty , " +
                        " transaction_date , " +
                        " transaction_type , " +
                        " transaction_type_name, " +
                        " d_warehouse_id , " +
                        " warehouse_name ," +
                        " d_product_id, " +
                        " product_name,created_by,user_name,full_name " +
                        " FROM pos.d_transaction_v  WHERE d_tenant_id = :tenantId "

        );


        if(dto.getProductId() != null){
            sql.append(" and d_product_id = :productId ");
        }

        sql.append(" ORDER BY " + dto.getSortBy() + " " + dto.getOrder());
        sql.append(" LIMIT :limit OFFSET :offset ");

        Query transactionQuery = entityManager.createNativeQuery(sql.toString());
        transactionQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if(dto.getProductId() != null){
            transactionQuery.setParameter("productId", dto.getProductId());
        }


        transactionQuery.setParameter("limit", dto.getPageSize());
        transactionQuery.setParameter("offset", dto.getPage());


        // Fetch the paginated results
        List<Map<String, Object>> results = transactionQuery.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<TransactionRespDto> listTransactionRespDto = new ArrayList<>();

        for (Map<String, Object> result : results) {
            TransactionRespDto transactionRespDto = TransactionRespDto.builder()
                    .tenantId(ParseHelper.INT.parse(result.get("d_tenant_id")))
                    .org(OrgDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_org_id")))
                            .name(ParseHelper.STRING.parse(result.get("org_name")))
                            .build())
                    .id(ParseHelper.INT.parse(result.get("d_transaction_id")))
                    .isActive(ParseHelper.STRING.parse(result.get("is_active")))
                    .qty(ParseHelper.BIGDECIMAL.parse(result.get("qty")))
                    .transactionDate( DateHelper.fromInstant(ParseHelper.INSTANT.parse(result.get("transaction_date"))))
                    .transactionType(ParseHelper.STRING.parse(result.get("transaction_type")))
                    .transactionTypeName(ParseHelper.STRING.parse(result.get("transaction_type_name")))
                    .userName(ParseHelper.STRING.parse(result.get("user_name")))
                    .userFullName(ParseHelper.STRING.parse(result.get("full_name")))
                    .warehouse(WarehouseDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_warehouse_id")))
                            .name(ParseHelper.STRING.parse(result.get("warehouse_name")))
                            .build())
                    .product(ProductCDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_product_id")))
                            .name(ParseHelper.STRING.parse(result.get("product_name")))
                            .build())
                    .build();
            listTransactionRespDto.add(transactionRespDto);
        }


        StringBuilder countSql = new StringBuilder("SELECT count(1) FROM pos.d_transaction_v  WHERE d_tenant_id = :tenantId "
        );



        if(dto.getProductId()!= null)
        {
            countSql.append(" and d_product_id = :productId ");
        }


        // Get the total number of records
        Query totalRecordsQuery = entityManager.createNativeQuery(countSql.toString());
        totalRecordsQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if(dto.getProductId()!= null)
        {
            totalRecordsQuery.setParameter("productId", dto.getProductId());
        }

        Long totalRecords = ((Number) totalRecordsQuery.getSingleResult()).longValue();


        return GlobalReponsePagination.builder()
                .data(listTransactionRespDto)
                .pageSize(dto.getPageSize())
                .currentPage(dto.getPage())
                .totalItems(totalRecords)
                .totalPages((int) Math.ceil((double)totalRecords / dto.getPageSize()))
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
    }

    //Purchase Order
    public String createPurchaseOrderTransaction(TransactionDto transactionDto) {

        String sql = "SELECT  " +
                "dpol.d_purchase_orderline_id," +
                "dpo.d_warehouse_id, " +
                "dpol.d_product_id, " +
                "dp.name, " +
                "dp.is_stocked, " +
                "dpol.qty, " +
                "dw.is_negative, dpo.d_locator_id, dpol.d_lot_id,dpo.d_org_id " +
                "FROM pos.d_purchase_order dpo " +
                "INNER JOIN pos.d_purchase_orderline dpol ON dpo.d_purchase_order_id = dpol.d_purchase_order_id " +
                "INNER JOIN pos.d_product dp ON dp.d_product_id = dpol.d_product_id " +
                "INNER JOIN pos.d_warehouse dw on dw.d_warehouse_id = dpo.d_warehouse_id  " +
                "WHERE dpo.d_tenant_id = :tenantId  " +
                "AND dpo.d_purchase_order_id = :orderId " +
                "AND dpol.is_active = 'Y' AND dp.is_stocked = 'Y' ";

        List<Map<String, Object>> results = null;
        String transactionType = "POR";
        String docType = "PURCHASEORDER";
        for(Integer purchaseOrderId : transactionDto.getPurchaseOrderIds()){
            try {

                results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("orderId", purchaseOrderId)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);
                    BigDecimal qty = ParseHelper.BIGDECIMAL.parse(row.get("qty"));
                    Integer warehouseId = ParseHelper.INT.parse(row.get("d_warehouse_id"));
                    Integer productId = ParseHelper.INT.parse(row.get("d_product_id"));
                    Integer locatorId = ParseHelper.INT.parse(row.get("d_locator_id"));
                    Integer orgId = ParseHelper.INT.parse(row.get("d_org_id"));
                    Integer lotId = ParseHelper.INT.parse(row.get("d_lot_id"));
                    String isNegative = ParseHelper.STRING.parse(row.get("is_negative"));
                    if(locatorId == null){
                        locatorId = getLocatorIdDefaultByWarehouse(warehouseId);
                    }
                    Integer checkExistedProductLocation = checkExistedProductLocation(warehouseId,locatorId,productId);

                    if(checkExistedProductLocation <= 0){
                        processCreateProductLocation(orgId,warehouseId,locatorId,productId,qty);
                    }

                    String result = null;

                    if(lotId == null){
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                productId,
                                qty,transactionType,
                                ParseHelper.INT.parse(row.get("d_purchase_orderline_id")),
                                docType,warehouseId,locatorId,isNegative);
                    }else{
                        result = modifyLotStorageOnHand(transactionDto.getOrgId(),
                                productId,
                                lotId,
                                qty,transactionType,
                                ParseHelper.INT.parse(row.get("d_purchase_orderline_id")),
                                docType,warehouseId,locatorId,isNegative);
                    }

                    if(result != null && !result.equals("")) {
                        return result;
                    }



                }
            }catch (Exception e) {
                log.error("TransactionServiceImpl.createPosOrderTransaction() - Exception: {}", e.getMessage());
                return e.getMessage();
            }
        }
        return null;
    }

    //Production
    public String createProductionTransaction(TransactionDto transactionDto) {

        String sql = "select  " +
                "dp.d_tenant_id, " +
                "dp.d_org_id, " +
                "dp.d_production_id, " +
                "dp.d_warehouse_id , " +
                "dp.d_locator_id , " +
                "dpl.d_productionline_id, " +
                "dpl.d_product_id, " +
                "dpl.used_qty, " +
                "dpl.is_end_product " +
                "from pos.d_production dp " +
                "inner join pos.d_productionline dpl on dp.d_production_id = dpl.d_production_id   " +
                "where dp.d_tenant_id  = :tenantId " +
                "and dp.d_production_id = :productionId " +
//                "and dp.documentstatus = 'COM' " +
                "and dp.is_active = 'Y'";
        List<Map<String, Object>> results = null;
        String isEndProduct = null;
        String transactionType = null;
        String docType = "PRODUCTION";
        for(Integer productionId : transactionDto.getProductionIds()){
            try {

                String isStocked = null;


                results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("productionId", productionId)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);
                    isStocked = getProductIsStock(ParseHelper.INT.parse(row.get("d_product_id")));
                    if(isStocked != null && isStocked.equals("N")) {
                        continue;
                    }
                    isEndProduct = ParseHelper.STRING.parse(row.get("is_end_product"));
                    BigDecimal qty = ParseHelper.BIGDECIMAL.parse(row.get("used_qty"));
                    String result = null;
                    if(isEndProduct != null && isEndProduct.equals("Y")) {
                        transactionType = "POR";
                        Integer locationId = getLocatorIdDefaultByWarehouse(ParseHelper.INT.parse(row.get("d_warehouse_id")));
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                ParseHelper.INT.parse(row.get("d_product_id")),
                                qty,transactionType,ParseHelper.INT.parse(row.get("d_productionline_id")),
                                docType,ParseHelper.INT.parse(row.get("d_warehouse_id")),locationId,null);
                    }else{
                        transactionType = "GDI";
                        qty = qty.negate();
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                ParseHelper.INT.parse(row.get("d_product_id")),
                                qty,transactionType,ParseHelper.INT.parse(row.get("d_productionline_id")),
                                docType,null,null,null);
                    }

                    if(result != null && !result.equals("")) {
                        return result;
                    }


                }
            }catch (Exception e) {
                log.error("TransactionServiceImpl.createProductionTransaction() - Exception: {}", e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return null;
    }

    //Production
    public String createProductionTransactionWithBody(TransactionDto transactionDto) {


        List<Map<String, Object>> results = null;
        String isEndProduct = null;
        String transactionType = null;
        String docType = "PRODUCTION";
        for(ProductionDto dto : transactionDto.getProductionDtos()){
            try {

                String isStocked = null;

                Integer locationId = getLocatorIdDefaultByWarehouse(dto.getWarehouseId());
                for (ProductionLineDto line : dto.getProductionLines() ) {
                    log.info("Row: {}", line);
                    isStocked = getProductIsStock(line.getProductId());
                    if(isStocked != null && isStocked.equals("N")) {
                        continue;
                    }
                    isEndProduct = line.getIsEndProduct();
                    BigDecimal qty = line.getUsedQty();
                    String result = null;
                    if(isEndProduct != null && isEndProduct.equals("Y")) {
                        transactionType = "POR";
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                line.getProductId(),
                                qty,transactionType,line.getId(),
                                docType,dto.getWarehouseId(),locationId,null);
                    }else{
                        transactionType = "GDI";
                        qty = qty.negate();
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                line.getProductId(),
                                qty,transactionType,line.getId(),
                                docType,null,null,null);
                    }


                    if(result != null && !result.equals("")) {
                        return result;
                    }


                }
            }catch (Exception e) {
                log.error("TransactionServiceImpl.createProductionTransaction() - Exception: {}", e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return null;
    }



    //Pos Order
    public StockError createPosOrderTransaction(TransactionDto transactionDto) {

        String sql = "SELECT  " +
                "dpol.d_pos_orderline_id, dpol.d_pos_order_id, " +
                "dpol.d_product_id, " +
                "dp.name, " +
                "dp.is_stocked, " +
                "dpol.qty, dp.group_type " +
                "FROM pos.d_pos_order dpo " +
                "INNER JOIN pos.d_pos_orderline dpol ON dpo.d_pos_order_id = dpol.d_pos_order_id " +
                "INNER JOIN pos.d_product dp ON dp.d_product_id = dpol.d_product_id " +
                "WHERE dpo.d_tenant_id = :tenantId  " +
                "AND dpo.d_pos_order_id = :orderId " +
                "AND (\n" +
                "        dpol.d_kitchen_orderline_id IS NULL\n" +
                "        OR dp.is_topping = 'Y'\n" +
                "    )" +
                "AND dpol.is_active = 'Y' AND dp.is_stocked = 'Y'";

        List<Map<String, Object>> results = null;
        String transactionType = "GDI";
        String docType = "POSORDER";
        String groupType = null;
        String result = null;
        LotStockError dto = null;
        StockError stockError = null;
        for(Integer posOrderId : transactionDto.getPosOrderIds()){
            try {

                results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("orderId", posOrderId)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);
                    BigDecimal qty = ParseHelper.BIGDECIMAL.parse(row.get("qty"));

                    groupType = ParseHelper.STRING.parse(row.get("group_type"));

                    if(groupType != null && groupType.equals("BLE")) {
                        dto = getPosLot(transactionDto.getOrgId(),
                                ParseHelper.INT.parse(row.get("d_pos_order_id")),
                                ParseHelper.INT.parse(row.get("d_pos_orderline_id")),
                                ParseHelper.INT.parse(row.get("d_product_id")),
                                qty,transactionType,docType);
                    }else{
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                ParseHelper.INT.parse(row.get("d_product_id")),
                                qty.negate(),transactionType,
                                ParseHelper.INT.parse(row.get("d_pos_orderline_id")),
                                docType,null,null,null);

                        if(result != null && !result.equals("")){
                            stockError = StockError.builder()
                                    .message(result)
                                    .build();
                        }
                    }

                    if(dto != null ) {
                        return dto;
                    }

                    if (stockError != null) {
                        return stockError;
                    }

                }
            }catch (Exception e) {
                log.error("TransactionServiceImpl.createPosOrderTransaction() - Exception: {}", e.getMessage());
                e.printStackTrace();
                return LotStockError.builder()
                        .message(e.getMessage())
                        .build();
            }
        }
        return null;
    }


    //Return Order
    public StockError createReturnOrderTransaction(TransactionDto transactionDto) {

        String sql = "SELECT  " +
                "drol.d_return_orderline_id, drol.d_return_order_id, " +
                "drol.d_product_id, " +
                "dp.name, " +
                "dp.is_stocked, " +
                "drol.qty, dp.group_type " +
                "FROM pos.d_return_order dro " +
                "INNER JOIN pos.d_return_orderline drol ON dro.d_return_order_id = drol.d_return_order_id " +
                "INNER JOIN pos.d_product dp ON dp.d_product_id = drol.d_product_id " +
                "WHERE dro.d_tenant_id = :tenantId  " +
                "AND dro.d_return_order_id = :returnOrderId " +
                "AND drol.is_active = 'Y' AND dp.is_stocked = 'Y'";

        List<Map<String, Object>> results = null;
        String transactionType = "POR";
        String docType = "RETURN_ORDER";
        String groupType = null;
        String result = null;
        LotStockError dto = null;
        StockError stockError = null;
        for(Integer returnOrderId : transactionDto.getReturnOrderIds()){
            try {

                results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("returnOrderId", returnOrderId)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);
                    BigDecimal qty = ParseHelper.BIGDECIMAL.parse(row.get("qty"));
                    groupType = ParseHelper.STRING.parse(row.get("group_type"));

                    //Check Product is managed by Lot
                    if(groupType != null && groupType.equals("BLE")) {
                        dto = getROLot(transactionDto.getOrgId(),
                                ParseHelper.INT.parse(row.get("d_return_order_id")),
                                ParseHelper.INT.parse(row.get("d_return_orderline_id")),
                                ParseHelper.INT.parse(row.get("d_product_id")),
                                qty,transactionType,docType);
                    }else{
                        result = modifyStorageOnHand(transactionDto.getOrgId(),
                                ParseHelper.INT.parse(row.get("d_product_id")),
                                qty,transactionType,
                                ParseHelper.INT.parse(row.get("d_return_orderline_id")),
                                docType,null,null,null);
                        if(result != null && !result.equals("")){
                            stockError = StockError.builder()
                                    .message(result)
                                    .build();
                        }
                    }

                    if(dto != null ) {
                        return dto;
                    }

                    if (stockError != null) {
                        return stockError;
                    }

                }
            }catch (Exception e) {
                log.error("TransactionServiceImpl.createPosOrderTransaction() - Exception: {}", e.getMessage());
                e.printStackTrace();
                return LotStockError.builder()
                        .message(e.getMessage())
                        .build();

            }
        }
        return null;
    }

    //Product Location
    public String createProductLocationTransaction(TransactionDto transactionDto) {

        String sql = "SELECT dw.d_warehouse_id, dl.d_locator_id , dw.is_negative  " +
                    "FROM pos.d_warehouse  dw  " +
                    "INNER JOIN pos.d_locator dl  on dl.d_warehouse_id  = dw.d_warehouse_id  " +
                    "WHERE dw.d_tenant_id = :tenantId " +
                    "and dw.d_warehouse_id = :warehouseId " +
                    "and dl.is_default= 'Y' and dw.is_active = 'Y'";

        String sqlManageLocator = "SELECT dw.d_warehouse_id, dl.d_locator_id , dw.is_negative  " +
                "FROM pos.d_warehouse  dw  " +
                "INNER JOIN pos.d_locator dl  on dl.d_warehouse_id  = dw.d_warehouse_id  " +
                "WHERE dw.d_tenant_id = :tenantId " +
                "and dw.d_warehouse_id = :warehouseId " +
                "and dl.d_locator_id = :locatorId " +
                "and dl.is_active = 'Y' and dw.is_active = 'Y'";

        List<Map<String, Object>> results = null;
        Integer warehouseId = null;
        Integer locatorId = null;
        String isNegative = null;
        String transactionType = null;
        BigDecimal qtyTransaction = null;
        String isStocked = null;
        for(ProductLocationDto productLocationDto : transactionDto.getProductLocationDtos()){
            if(productLocationDto.getStockQty() == null) {
                continue;
            }

            isStocked = getProductIsStock(productLocationDto.getProductId());
            if(isStocked != null && isStocked.equals("N")) {
                continue;
            }


            try{

                //Khong quan ly Locator
                if(productLocationDto.getLocatorId() == null){
                    results = entityManager.createNativeQuery(sql)
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("warehouseId", productLocationDto.getWarehouseId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                }else{
                    results = entityManager.createNativeQuery(sqlManageLocator)
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("warehouseId", productLocationDto.getWarehouseId())
                            .setParameter("locatorId", productLocationDto.getLocatorId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                }

                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);

                    warehouseId = ParseHelper.INT.parse(row.get("d_warehouse_id"));
                    locatorId = ParseHelper.INT.parse(row.get("d_locator_id"));

                    Optional<StorageOnhand> storageOnHand = storageOnHandRepository
                            .findByProductIdAndWarehouseIdAndLocatorId(productLocationDto.getProductId(), warehouseId,
                                    locatorId);

                    if(storageOnHand.isPresent()) {

                        if(productLocationDto.getStockQty().compareTo(BigDecimal.ZERO) < 0) {
                            isNegative = ParseHelper.STRING.parse(row.get("is_negative"));
                            if(isNegative != null && isNegative.equals("N")) {
                                String productName = getProductName(productLocationDto.getProductId());
                                return productName != null ?  productName + " "
                                        + messageSource.getMessage("product.out.of.stock", null, LocaleContextHolder.getLocale()) :
                                        messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()) ;
                            }
                        }

                        StorageOnhand storageOnhand = storageOnHand.get();
                        BigDecimal currentQty = storageOnhand.getQty();
                        storageOnhand.setQty(productLocationDto.getStockQty());
                        storageOnhand.setMinQty(productLocationDto.getMinQty());
                        storageOnhand.setMaxQty(productLocationDto.getMaxQty());

                        storageOnHandRepository.save(storageOnhand);


//                        if(currentQty.compareTo(productLocationDto.getStockQty()) == 0){
//                            continue;
//                        }else if (currentQty.compareTo(productLocationDto.getStockQty()) < 0) {
//                            transactionType = "POR"; // Increase stock
//                        }else {
//                            transactionType = "GDI"; // Decrease stock
//                        }
                        if(currentQty.compareTo(productLocationDto.getStockQty()) == 0){
                            continue;
                        }
                        transactionType = "ADJ";
                        qtyTransaction = productLocationDto.getStockQty().subtract(currentQty);

                    }else{

                        if(productLocationDto.getStockQty().compareTo(BigDecimal.ZERO) < 0) {
                            isNegative = ParseHelper.STRING.parse(row.get("is_negative"));
                            if(isNegative != null && isNegative.equals("N")) {
                                String productName = getProductName(productLocationDto.getProductId());
                                return productName != null ?  productName + " "
                                        + messageSource.getMessage("product.out.of.stock", null, LocaleContextHolder.getLocale()) :
                                        messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()) ;
                            }
                        }

                        if(productLocationDto.getIsSysDefault() != null && productLocationDto.getIsSysDefault().equals("N")) {
                            transactionType = productLocationDto.getStockQty().compareTo(BigDecimal.ZERO) > 0 ? "POR" : "GDI";
                            qtyTransaction = productLocationDto.getStockQty();
                        }

                        StorageOnhand storageOnhand = StorageOnhand.builder()
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(productLocationDto.getOrgId())
                                .productId(productLocationDto.getProductId())
                                .warehouseId(warehouseId)
                                .locatorId(locatorId)
                                .qty(productLocationDto.getStockQty())
                                .minQty(productLocationDto.getMinQty())
                                .maxQty(productLocationDto.getMaxQty())
                                .build();
                        storageOnHandRepository.save(storageOnhand);
                    }

                    if(productLocationDto.getIsSysDefault() != null && productLocationDto.getIsSysDefault().equals("N")){
                        //Create transaction
                        Transaction transaction = Transaction.builder()
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(productLocationDto.getOrgId())
                                .transactionDate(Instant.now())
                                .warehouseId(warehouseId)
                                .locatorId(locatorId)
                                .productId(productLocationDto.getProductId())
                                .transactionType(transactionType)
                                .qty(qtyTransaction)
                                .build();
                        transactionRepository.save(transaction);
                    }

                }

            }catch (Exception e) {
                log.error("TransactionServiceImpl.createProductLocationTransaction() - Exception: {}", e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return null;
    }

    public String createKitchenOrderLineTransaction(TransactionDto transactionDto) {

        String sql = "SELECT  " +
                "dkol.d_kitchen_orderline_id, " +
                "dkol.d_product_id, " +
                "dp.name, " +
                "dp.is_stocked, " +
                "dkol.qty " +
                "FROM pos.d_kitchen_orderline dkol " +
                "INNER JOIN pos.d_product dp ON dkol.d_product_id = dp.d_product_id " +
                "WHERE dkol.d_tenant_id = :tenantId  " +
                "AND dkol.d_kitchen_orderline_id = :kitchenOrderLineId " +
                "AND dkol.is_active = 'Y' AND dp.is_stocked = 'Y'";

        List<Map<String, Object>> results = null;
        String transactionType = "GDI";
        String docType = "KITCHENORDERLINE";
        for(Integer kitchenOrderLineId : transactionDto.getKitchenOrderLineIds()){
            try {

                results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("kitchenOrderLineId", kitchenOrderLineId)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);
                    BigDecimal qty = ParseHelper.BIGDECIMAL.parse(row.get("qty"));
                    String result = modifyStorageOnHand(transactionDto.getOrgId(),
                            ParseHelper.INT.parse(row.get("d_product_id")),
                            qty.negate(),transactionType,
                            ParseHelper.INT.parse(row.get("d_kitchen_orderline_id")),
                            docType,null,null,null);

                    if(result != null && !result.equals("")) {
                        return result;
                    }

                }
            }catch (Exception e) {
                log.error("TransactionServiceImpl.createPosOrderTransaction() - Exception: {}", e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return null;
    }

    public String modifyStorageOnHand(Integer orgId,
                                      Integer productId,
                                      BigDecimal qty,
                                      String transactionType,
                                      Integer recordId,
                                      String docType,
                                      Integer warehouseId,
                                      Integer locatorId,
                                      String isNegative) {


        if(warehouseId == null){
            String sql = "SELECT dpl.d_warehouse_id, coalesce(dpl.d_locator_id,dl.d_locator_id) as d_locator_id  , " +
                    " dw.is_negative  " +
                    "FROM pos.d_product_location dpl " +
                    "INNER JOIN pos.d_warehouse  dw ON dpl.d_warehouse_id = dw.d_warehouse_id  " +
                    "inner join pos.d_locator dl  on dl.d_warehouse_id  = dw.d_warehouse_id  " +
                    "WHERE dpl.d_product_id  = :productId " +
                    " AND dpl.d_org_id = :orgId " +
                    " AND dpl.is_default = 'Y' " + //Default kitchen location
                    " AND dpl.is_active = 'Y' " ;
//                    " AND dl.is_default= 'Y'"; //Default locator

            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("productId", productId)
                    .setParameter("orgId", orgId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();


            if(results == null || results.isEmpty()) {
                String productName = getProductName(productId);
                return productName != null ?  productName + " "
                        + messageSource.getMessage("product.out.of.stock", null, LocaleContextHolder.getLocale()) :
                        messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()) ;
            }

            for ( Map<String, Object> row : results ) {
                log.info("Row: {}", row);
                isNegative = ParseHelper.STRING.parse(row.get("is_negative"));
                warehouseId = ParseHelper.INT.parse(row.get("d_warehouse_id"));
                locatorId = ParseHelper.INT.parse(row.get("d_locator_id"));
            }
        }

        Optional<StorageOnhand> storageOnHand = storageOnHandRepository
                .findByProductIdAndWarehouseIdAndLocatorId(productId, warehouseId, locatorId);
        if(storageOnHand.isPresent()) {
            BigDecimal currentQty = storageOnHand.get().getQty();
            currentQty = currentQty.add(qty);

            //Negative quantity check
            if(currentQty.compareTo(BigDecimal.ZERO) < 0){
                if(isNegative != null && isNegative.equals("N") ) {
                    String productName = getProductName(productId);
                    return productName != null ?  productName + " "
                            + messageSource.getMessage("product.out.of.stock", null, LocaleContextHolder.getLocale()) :
                            messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()) ;
                }
            }

            StorageOnhand storageOnhand = storageOnHand.get();
            storageOnhand.setQty(currentQty);
            storageOnHandRepository.save(storageOnhand);
        }else{

            //Negative quantity check
            if(qty.compareTo(BigDecimal.ZERO) < 0){
                if(isNegative != null && isNegative.equals("N") ) {
                    String productName = getProductName(productId);
                    return productName != null ?  productName + " "
                            + messageSource.getMessage("product.out.of.stock", null, LocaleContextHolder.getLocale()) :
                            messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()) ;
                }
            }

            StorageOnhand storageOnhand = StorageOnhand.builder()
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(orgId)
                    .productId(productId)
                    .warehouseId(warehouseId)
                    .locatorId(locatorId)
                    .qty(qty)
                    .build();
            storageOnHandRepository.save(storageOnhand);
        }

        //Create transaction
        Transaction transaction = Transaction.builder()
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .orgId(orgId)
                .transactionDate(DateHelper.toInstantNowUTC())
                .warehouseId(warehouseId)
                .locatorId(locatorId)
                .productId(productId)
                .transactionType(transactionType)
                .qty(qty)
                .build();

        if(docType.equals("POSORDER")) {
            transaction.setPosOrderLineId(recordId);
        }else if (docType.equals("PRODUCTION")) {
            transaction.setProductionLineId(recordId);
        }else if (docType.equals("KITCHENORDERLINE")) {
            transaction.setKitchenOrderLineId(recordId);
        }else if (docType.equals("PURCHASEORDER")) {
            transaction.setPurchaseOrderLineId(recordId);
        }else if (docType.equals("RETURN_ORDER")) {
            transaction.setReturnOrderLineId(recordId);
        }
        transactionRepository.save(transaction);

        return "";
    }


    public String modifyLotStorageOnHand(Integer orgId,
                                         Integer productId,
                                         Integer lotId,
                                         BigDecimal qty,
                                         String transactionType,
                                         Integer recordId,
                                         String docType,
                                         Integer warehouseId,
                                         Integer locatorId,
                                         String isNegative){

        Optional<StorageOnhand> storageOnHand = storageOnHandRepository
                .findByLotIdAndWarehouseIdAndLocatorId(lotId, warehouseId, locatorId);
        if(storageOnHand.isPresent()) {
            BigDecimal currentQty = storageOnHand.get().getQty();
            currentQty = currentQty.add(qty);

            //Negative quantity check
            if(currentQty.compareTo(BigDecimal.ZERO) < 0){
                if(isNegative != null && isNegative.equals("N") ) {
                    return getMessageErrorLot(lotId, "OUT_OF_STOCK");
                }
            }

            StorageOnhand storageOnhand = storageOnHand.get();
            storageOnhand.setQty(currentQty);
            storageOnHandRepository.saveAndFlush(storageOnhand);
        }else{

            //Negative quantity check
            if(qty.compareTo(BigDecimal.ZERO) < 0){
                if(isNegative != null && isNegative.equals("N") ) {
                    return getMessageErrorLot(lotId, "OUT_OF_STOCK");
                }
            }

            StorageOnhand storageOnhand = StorageOnhand.builder()
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(orgId)
                    .lotId(lotId)
                    .productId(productId)
                    .warehouseId(warehouseId)
                    .locatorId(locatorId)
                    .qty(qty)
                    .build();
            storageOnHandRepository.saveAndFlush(storageOnhand);
        }

        //Create transaction
        Transaction transaction = Transaction.builder()
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .orgId(orgId)
                .transactionDate(DateHelper.toInstantNowUTC())
                .warehouseId(warehouseId)
                .locatorId(locatorId)
                .productId(productId)
                .lotId(lotId)
                .transactionType(transactionType)
                .qty(qty)
                .build();

        if(docType.equals("POSORDER")) {
            transaction.setPosOrderLineId(recordId);
        }else if (docType.equals("PRODUCTION")) {
            transaction.setProductionLineId(recordId);
        }else if (docType.equals("KITCHENORDERLINE")) {
            transaction.setKitchenOrderLineId(recordId);
        }else if (docType.equals("PURCHASEORDER")) {
            transaction.setPurchaseOrderLineId(recordId);
        }else if (docType.equals("RETURN_ORDER")) {
            transaction.setReturnOrderLineId(recordId);
        }
        transactionRepository.saveAndFlush(transaction);

        return "";
    }

    public String getProductName(Integer productId){
        String sql1 = "select name from pos.d_product where d_product_id = :productId and d_tenant_id = :tenantId";
        String productName = ((String) entityManager.createNativeQuery(sql1)
                .setParameter("productId", productId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult()).toString();
        return productName;
    }

    public String getLotCode(Integer lotId){
        String sql1 = "select code from pos.d_lot where d_lot_id = :lotId and d_tenant_id = :tenantId";
        String productName = ((String) entityManager.createNativeQuery(sql1)
                .setParameter("lotId", lotId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult()).toString();
        return productName;
    }

    public String getProductIsStock(Integer productId) {
        log.info("tenant ID: "+AuditContext.getAuditInfo().getTenantId());
        log.info("product ID: "+productId);
        String sql1 = "select is_stocked from pos.d_product where d_product_id = :productId and d_tenant_id = :tenantId";
        List<String> isStocked =  entityManager.createNativeQuery(sql1)
                .setParameter("productId", productId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getResultList();
        if(isStocked.isEmpty()){
            return null;
        }
        return isStocked.get(0) ;
    }


    public Integer getLocatorIdDefaultByWarehouse(Integer warehouseId){
        String sql1 = "select d_locator_id from pos.d_locator where  d_tenant_id = :tenantId " +
                " and d_warehouse_id = :warehouseId and is_default = 'Y' and is_active = 'Y'";
        Integer locatorId = ((Number) entityManager.createNativeQuery(sql1)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("warehouseId", warehouseId)
                .getSingleResult()).intValue();
        return locatorId ;
    }

    public Integer checkExistedProductLocation(Integer warehouseId,Integer locatorId,Integer productId){
        String sql1 = "select count(1) from pos.d_product_location " +
                " where  d_warehouse_id = :warehouseId " +
                " and d_locator_id = :locatorId " +
                " and d_product_id = :productId ";
        Integer result = ((Number) entityManager.createNativeQuery(sql1)
                .setParameter("warehouseId", warehouseId)
                .setParameter("locatorId", locatorId)
                .setParameter("productId", productId)
                .getSingleResult()).intValue();
        return result ;
    }

    public String isProductLocationDefault(Integer orgId,Integer productId){
        String sql1 = "select count(1) from pos.d_product_location " +
                " where  d_org_id = :orgId " +
                " and d_product_id = :productId and is_default = 'Y' and is_active = 'Y' ";
        Integer result = ((Number) entityManager.createNativeQuery(sql1)
                .setParameter("orgId", orgId)
                .setParameter("productId", productId)
                .getSingleResult()).intValue();
        return result == 0 ? "Y" : "N";
    }


    public String processCreateProductLocation(Integer orgId,
                                             Integer warehouseId,
                                             Integer locatorId,
                                             Integer productId,
                                             BigDecimal stockQty
                                            ){

        SaveProductLocationDto saveProductLocationDto = SaveProductLocationDto.builder()
                .productId(productId)
                .isFirstCreation("N")
                .build();
        ProductLocationDto productLocationDto = ProductLocationDto.builder()
                .orgId(orgId)
                .warehouseId(warehouseId)
                .locatorId(locatorId)
                .stockQty(stockQty)
                .isDefault(isProductLocationDefault(orgId,productId))
                .isActive("Y")
                .build();

        saveProductLocationDto.setListProductLocation(List.of(productLocationDto));

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());

        HttpEntity<SaveProductLocationDto> requestEntity = new HttpEntity<>(saveProductLocationDto, headers);
        GlobalReponse responseTransaction = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_CREATE_PRODUCT_LOCATION_URL ,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();
        if(responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
            return responseTransaction.getMessage();
        }
        return null;
    }


    public LotStockError getPosLot(Integer orgId,
                                   Integer orderId,
                                   Integer orderLineId,
                                   Integer productId, BigDecimal qty,
                                   String transactionType, String docType){

        LotStockError dto = null;
        try {
            String sql = "SELECT dpl.d_lot_id, " +
                    "dpl.qty, dl.expiry_date,dl.code as lot_code,dl.d_product_id, " +
                    "CASE WHEN dl.expiry_date >= CURRENT_DATE THEN 1 ELSE 0 END AS check_expiry_date " +
                    "FROM pos.d_pos_lot dpl " +
                    "INNER JOIN pos.d_lot dl ON dpl.d_lot_id = dl.d_lot_id " +
                    "WHERE dpl.d_pos_order_id = :orderId " +
                    "AND dpl.d_pos_orderline_id = :orderLineId " +
                    "AND dpl.is_active = 'Y'";

            String result= null;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("orderId",orderId)
                    .setParameter("orderLineId", orderLineId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            if(results != null && !results.isEmpty()){

                Integer checkExpiryDate = null;
                BigDecimal trxQty = BigDecimal.ZERO;

                if(qty.compareTo(getTotalPosLotQty(orderId,orderLineId)) != 0){
                    return LotStockError.builder()
                            .productId(productId)
                            .message( "Số lượng lô hàng không đủ")
                            .build();
                }
                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);

                    checkExpiryDate = ParseHelper.INT.parse(row.get("check_expiry_date"));
                    if(checkExpiryDate != null && checkExpiryDate == 0){
                        return LotStockError.builder()
                                .productId(productId)
                                .message(getMessageErrorLot(ParseHelper.INT.parse(row.get("d_lot_id")), "EXPIRED"))
                                .build();
                    }
                    trxQty = ParseHelper.BIGDECIMAL.parse(row.get("qty"));
                    dto = processLot(orgId,
                            ParseHelper.INT.parse(row.get("d_lot_id")),
                            orderLineId,
                            trxQty.negate(),
                            transactionType,
                            docType);
                    if(dto != null ) {
                        return dto;
                    }
                }
            }else{

                if(qty.compareTo(getTotalDefaultLotQty(orgId,productId)) > 0){
                    return LotStockError.builder()
                            .productId(productId)
                            .message( "Số lượng lô hàng không đủ")
                            .build();
                }

                dto = processLotAutomation(orgId,productId,orderLineId,qty,transactionType,docType);
                if(dto != null ) {
                    return dto;
                }
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return LotStockError.builder()
                    .productId(productId)
                    .message( e.getMessage())
                    .build();
        }

        return null;
    }


    public LotStockError getROLot(Integer orgId,
                                  Integer returnOrderId,
                                  Integer returnOrderLineId,
                                  Integer productId, BigDecimal qty,
                                  String transactionType, String docType){

        LotStockError dto = null;

        try {
            String sql = "SELECT drl.d_lot_id, " +
                    "drl.qty, dl.expiry_date,dl.code as lot_code, " +
                    "CASE WHEN dl.expiry_date >= CURRENT_DATE THEN 1 ELSE 0 END AS check_expiry_date " +
                    "FROM pos.d_ro_lot drl " +
                    "INNER JOIN pos.d_lot dl ON drl.d_lot_id = dl.d_lot_id " +
                    "WHERE drl.d_return_order_id = :returnOrderId " +
                    "AND drl.d_return_orderline_id = :returnOrderLineId " +
                    "AND drl.is_active = 'Y'";

            String result= null;
            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("returnOrderId",returnOrderId)
                    .setParameter("returnOrderLineId", returnOrderLineId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            if(results != null && !results.isEmpty()){

                Integer checkExpiryDate = null;

                if(qty.compareTo(getTotalROLotQty(returnOrderId,returnOrderLineId)) < 0){
                    return LotStockError.builder()
                            .productId(productId)
                            .message( "Số lượng lô hàng không đủ")
                            .build();
                }
                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);

                    checkExpiryDate = ParseHelper.INT.parse(row.get("check_expiry_date"));
                    if(checkExpiryDate != null && checkExpiryDate == 0){
                        return LotStockError.builder()
                                .productId(productId)
                                .message(getMessageErrorLot(ParseHelper.INT.parse(row.get("d_lot_id")), "EXPIRED"))
                                .build();
                    }
                    dto = processLot(orgId,
                            ParseHelper.INT.parse(row.get("d_lot_id")),
                            returnOrderLineId,
                            ParseHelper.BIGDECIMAL.parse(row.get("qty")),
                            transactionType,
                            docType);
                    if(dto != null ) {
                        return dto;
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return LotStockError.builder()
                    .productId(productId)
                    .message( e.getMessage())
                    .build();
        }

        return null;
    }

    public BigDecimal getTotalPosLotQty(Integer orderId, Integer orderLineId){
        String sql1 = "select coalesce(sum(qty),0) from pos.d_pos_lot " +
                " where  d_pos_order_id = :orderId " +
                " and d_pos_orderline_id = :orderLineId and is_active = 'Y' ";

        Object num = entityManager.createNativeQuery(sql1)
                .setParameter("orderId", orderId)
                .setParameter("orderLineId", orderLineId)
                .getSingleResult();
        return num == null ? BigDecimal.ZERO : ParseHelper.BIGDECIMAL.parse(num);
    }

    public BigDecimal getTotalROLotQty(Integer returnOrderId, Integer returnOrderLineId){
        String sql1 = "select coalesce(sum(qty),0) from pos.d_ro_lot " +
                " where  d_return_order_id = :returnOrderId " +
                " and d_return_orderline_id = :returnOrderLineId and is_active = 'Y' ";

        Object num = entityManager.createNativeQuery(sql1)
                .setParameter("returnOrderId", returnOrderId)
                .setParameter("returnOrderLineId", returnOrderLineId)
                .getSingleResult();
        return num == null ? BigDecimal.ZERO : ParseHelper.BIGDECIMAL.parse(num);
    }

    public BigDecimal getTotalDefaultLotQty(Integer orgId,
                                            Integer productId){
        String sql1 = "select coalesce(sum(onhand_qty),0) " +
                " from pos.d_lot_v " +
                " where  d_product_id = :productId " +
                " and d_org_id  = :orgId " +
                " and is_active = 'Y' " +
                " and expiry_date >= current_date and onhand_qty > 0 ";
        Object num = entityManager.createNativeQuery(sql1)
                .setParameter("productId", productId)
                .setParameter("orgId", orgId)
                .getSingleResult();
        return num == null ? BigDecimal.ZERO : ParseHelper.BIGDECIMAL.parse(num);
    }


    public LotStockError processLotAutomation(Integer orgId,
                                              Integer productId,
                                              Integer recordId,
                                              BigDecimal qty,
                                              String transactionType,
                                              String docType){

        try {
            String sql = "SELECT d_lot_id, " +
                    "           d_product_id, " +
                    "           d_warehouse_id, " +
                    "           d_locator_id, " +
                    "           is_negative, " +
                    "           onhand_qty " +
                    " FROM pos.d_lot_v WHERE 1 = 1 " +
                    " AND d_org_id = :orgId " +
                    " AND d_product_id = :productId " +
                    " AND expiry_date >= CURRENT_DATE" +
                    " AND onhand_qty > 0 "  +
                    " AND is_active = 'Y' " +
                    " order by expiry_date asc " ;

            String result = null;
            Integer warehouseId = null;
            Integer locatorId = null;
            Integer lotId = null;
            String isNegative = null;
            BigDecimal currentQty = qty;
            BigDecimal onHandQty = null;
            BigDecimal trxQty = null;
            boolean isDone = false;
            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("orgId",orgId)
                    .setParameter("productId", productId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                warehouseId = ParseHelper.INT.parse(row.get("d_warehouse_id"));
                locatorId = ParseHelper.INT.parse(row.get("d_locator_id"));
                lotId = ParseHelper.INT.parse(row.get("d_lot_id"));
                isNegative = ParseHelper.STRING.parse(row.get("is_negative"));
                onHandQty = ParseHelper.BIGDECIMAL.parse(row.get("onhand_qty"));

                if(currentQty.compareTo(onHandQty) > 0) {
                    currentQty = currentQty.subtract(onHandQty);
                    trxQty = onHandQty;
                }else{
                    trxQty = currentQty;
                    isDone = true;
                }
                trxQty = trxQty.negate();


                result = modifyLotStorageOnHand(orgId,
                        productId,
                        lotId,
                        trxQty,transactionType,
                        recordId,
                        docType,warehouseId,locatorId,isNegative);

                if(result != null && !result.equals("")) {
                    return LotStockError.builder().productId(productId)
                            .message(result)
                            .build();
                }
                if(isDone) return null;
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return LotStockError.builder().productId(productId)
                    .message(e.getMessage())
                    .build();
        }

        return null;

    }


    public LotStockError processLot(Integer orgId,
                                    Integer lotId,
                                    Integer recordId,
                                    BigDecimal qty,
                                    String transactionType,
                                    String docType){

        try {
            String sql = "SELECT d_lot_id, " +
                    "           d_product_id, " +
                    "           d_warehouse_id, " +
                    "           d_locator_id, " +
                    "           is_negative, " +
                    "           onhand_qty " +
                    " FROM pos.d_lot_v WHERE d_lot_id = :lotId " +
                    " AND d_org_id = :orgId " +
//                    " AND onhand_qty > 0 "  +
                    " AND is_active = 'Y' " ;
//                    " order by in_date asc " ;

            String result = null;
            Integer productId = null;
            Integer warehouseId = null;
            Integer locatorId = null;
            String isNegative = null;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("orgId",orgId)
                    .setParameter("lotId", lotId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                productId = ParseHelper.INT.parse(row.get("d_product_id"));
                warehouseId = ParseHelper.INT.parse(row.get("d_warehouse_id"));
                locatorId = ParseHelper.INT.parse(row.get("d_locator_id"));
                isNegative = ParseHelper.STRING.parse(row.get("is_negative"));

                result = modifyLotStorageOnHand(orgId,
                        productId,
                        lotId,
                        qty,transactionType,
                        recordId,
                        docType,warehouseId,locatorId,isNegative);

                if(result != null && !result.equals("")) {
                    LotStockError dto = LotStockError.builder()
                            .productId(productId)
                            .message(result)
                            .build();
                    return dto;
                }
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return LotStockError.builder()
                    .message(e.getMessage())
                    .build();
        }

        return null;

    }

    public String getMessageErrorLot(Integer lotId,String messageType) {

        String messageError = null;
        try {
            String sql = "SELECT dl.code as lot_code, dp.name as product_name " +
                    " FROM pos.d_lot dl " +
                    " INNER JOIN pos.d_product dp ON dp.d_product_id = dl.d_product_id " +
                    " WHERE dl.d_lot_id = :lotId " ;


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("lotId", lotId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();


            if(messageType.equals("EXPIRED")){
                messageError = messageSource.getMessage("lot.expired", null, LocaleContextHolder.getLocale());
            }else if (messageType.equals("OUT_OF_STOCK")){
                messageError = messageSource.getMessage("lot.out.of.stock", null, LocaleContextHolder.getLocale());
            }

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                messageError = messageError.replace("@LOT_CODE@", ParseHelper.STRING.parse(row.get("lot_code")));
                messageError = messageError.replace("@PRODUCT_NAME@", ParseHelper.STRING.parse(row.get("product_name")));

            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
        return messageError;
    }
}
