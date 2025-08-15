package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.domain.view.GetKolSameProductV;
import com.dbiz.app.orderservice.domain.view.KitchenOrderLineView;
import org.apache.http.protocol.HTTP;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.orderDto.InvoiceDto;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDetailDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.helper.KitchenOrderLineMapper;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.service.KitchenOrderLineService;
import com.dbiz.app.orderservice.specification.GetKolSameProductVSpecification;
import com.dbiz.app.orderservice.specification.KitchenOrderLineSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.SyncOrderInfoDto;
import org.common.dbiz.dto.orderDto.KitchenOrderHistory;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;

import org.common.dbiz.dto.productDto.ProductComboDto;

import org.common.dbiz.dto.orderDto.dtoView.GetKolSameProductVDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transaction;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class KitchenOrderLineServiceImpl implements KitchenOrderLineService {

    private final RequestParamsUtils requestParamsUtils;

    private final KitchenOrderlineRepository entityRepository;

    private final CancelReasonRepository cancelReasonRepository;

    private final KitchenOrderLineMapper kitchenOrderLineMapper;

    private final FloorRepository floorRepository;

    private final MessageSource messageSource;

    private final RestTemplate restTemplate;

    private final DoctypeRepository doctypeRepository;

    private final ProductionRepository productionRepository;

    private final ModelMapper modelMapper;

    private final GetKolSameProductVRepository getKolSameProductVRepository;

    private final KitchenOrderLineViewRepository kitchenOrderLineViewRepository;

    private final EntityManager entityManager;

    private final KitchenOrderRepository kitchenOrderRepository;

    private final TableRepository tableRepository;


    private StringBuilder sql;

    private final ProductionLineRepository productionLineRepository;

    private final static String TOPIC = "sync-kitchen-order";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public GlobalReponsePagination findAll(KitchenOrderLineRequest request) {
        log.info("*** kitchen order List, service; fetch all products *");


        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<KitchenOrderLine> spec = KitchenOrderLineSpecification.getEntity(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<KitchenOrderLine> kitchenOrders = entityRepository.findAll(spec, pageable);
        List<KitchenOrderlineDto> listData = new ArrayList<>();
        for (KitchenOrderLine item : kitchenOrders.getContent()) {
            listData.add(kitchenOrderLineMapper.toKitchenOrderLineDto(item));
        }
        response.setData(listData);
        response.setCurrentPage(kitchenOrders.getNumber());
        response.setPageSize(kitchenOrders.getSize());
        response.setTotalPages(kitchenOrders.getTotalPages());
        response.setTotalItems(kitchenOrders.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(KitchenOrderlineDto kitchenOrderDto) {
        log.info("*** warehouse, service; save warehouse ***");
        GlobalReponse response = new GlobalReponse();
        KitchenOrderLine entitySave = kitchenOrderLineMapper.toKitchenOrderLine(kitchenOrderDto);

        if (entitySave.getId() != null) // update
        {
            entitySave = this.entityRepository.findById(kitchenOrderDto.getId()).orElseThrow(() -> new ObjectNotFoundException("Floor not found"));

            kitchenOrderLineMapper.updateEntity(kitchenOrderDto, entitySave);

            this.entityRepository.save(entitySave);
        } else {
            entitySave = this.entityRepository.save(entitySave);
        }

        response.setData(kitchenOrderLineMapper.toKitchenOrderLineDto(entitySave));
        response.setMessage("Floor saved successfully");
        response.setStatus(HttpStatus.OK.value());
        log.info("Floor saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete floor by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<KitchenOrderLine> entityDelete = this.entityRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage("floor not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.entityRepository.delete(entityDelete.get());
        response.setMessage("floor deleted successfully");
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** WarehouseDto, service; fetch warehouse by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(kitchenOrderLineMapper.toKitchenOrderLineDto(this.entityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %d not found", id)))));
        response.setMessage("Product fetched successfully");
        return response;
    }


    @Override
    public GlobalReponse cancelQty(KitchenOrderLineCancelRq request) {
        GlobalReponse response = new GlobalReponse();
        CancelReason cancelReason = this.cancelReasonRepository.findById(request.getCancelReasonId()).orElseThrow(() ->
                new ObjectNotFoundException(messageSource.getMessage("cancel_reason_notFound", null, LocaleContextHolder.getLocale())));
        KitchenOrderLine line = this.entityRepository.findById(request.getKitchenOrderLineId()).orElseThrow(() ->
                new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));
        if (line.getOrderlineStatus().equals("5")) // update
        {
            response.setData(null);
            response.setMessage(messageSource.getMessage("kitchen_order_voided", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            return response;
        }
        if (line.getQty().equals(BigDecimal.valueOf(request.getCancelQty()))) // update
        {
            line.setOrderlineStatus("5");
            line.setQty(BigDecimal.ZERO);
            line.setNote(request.getNote());
        } else {
            line.setQty(line.getQty().subtract(BigDecimal.valueOf(request.getCancelQty())));
        }
        line.setCancelReasonId(request.getCancelReasonId());
        line = this.entityRepository.save(line);
        response.setData(kitchenOrderLineMapper.toKitchenOrderLineDto(line));

        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));

        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse updateAllById(UpdateAllKitchenLineByIdRequest request) {
        log.info("*** KitchenOrderLine, service; update KitchenOrderLine *");
        log.info("request: {}", request.toString());
        log.info("orgId : " + request.getOrgId());
        log.info("userId : " + request.getUserId());
        Integer orgId = 0;
        Map<Integer, BigDecimal> createProduction = new HashMap<>();
        Map<Integer, Integer> updateProduction = new HashMap<>();
        Map<Integer ,Integer> mapProduct_Warehouse= new HashMap<>();
        HashSet<Integer> kitchenOrderIds = new HashSet<>();
        List<Integer> moveByKitchenOrderLineIds = new ArrayList<>();
        for (UpdateAllKitchenLineByIdRequest.KitchenOrderLineList itemKitchenLine : request.getKitchenOrderLineList()) {

            KitchenOrderLine line = this.entityRepository.findById(itemKitchenLine.getKitchenOrderLineId()).orElseThrow(() ->
                    new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));

            KitchenOrder order = this.kitchenOrderRepository.findById(line.getKitchenOrderId()).orElseThrow(() ->
                    new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));

            line.setProductId(itemKitchenLine.getProductId());
            line.setOrderlineStatus(itemKitchenLine.getStatus());
            line.setCancelReasonId(itemKitchenLine.getCancelReasonId() != null ? itemKitchenLine.getCancelReasonId() : null);
            this.entityRepository.saveAndFlush(line);

            if(itemKitchenLine.getKitchenOrderLineDetailList() != null && !itemKitchenLine.getKitchenOrderLineDetailList().isEmpty()) {

                for (UpdateAllKitchenLineByIdRequest.KitchenOrderLineList.KitchenOrderLineDetailList kitchenOrderLineDetailList : itemKitchenLine.getKitchenOrderLineDetailList()) {

                    KitchenOrderLine lineDetail = this.entityRepository.findById(kitchenOrderLineDetailList.getKitchenOrderLineId()).orElseThrow(() ->
                            new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));

                    lineDetail.setProductId(kitchenOrderLineDetailList.getProductId());
                    lineDetail.setOrderlineStatus(kitchenOrderLineDetailList.getStatus());
                    lineDetail.setCancelReasonId(kitchenOrderLineDetailList.getCancelReasonId() != null ? kitchenOrderLineDetailList.getCancelReasonId() : null);
                    this.entityRepository.saveAndFlush(lineDetail);
                }
            }

            if (itemKitchenLine.getStatus().equals("BPR")) {

                mapProduct_Warehouse.put(line.getProductId(),order.getWarehouseId());

                if (!checkIsValidTopping(line)) {

                    createProduction.merge(line.getProductId(), line.getQty(), BigDecimal::add);
                }
            } else if (itemKitchenLine.getStatus().equals("PRD")) {

                if (!checkIsValidTopping(line)) {

                    updateProduction.put(line.getProductId(), line.getProductionId());
                }

                // send notification
                KitchenOrder entityHeader = kitchenOrderRepository.findById(line.getKitchenOrderId()).orElseThrow(() ->
                        new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));

                List<Tuple> checkNotifi = entityManager.createNativeQuery("select is_notify_bill from d_pos_terminal where d_pos_terminal_id = ?", Tuple.class)
                        .setParameter(1, entityHeader.getPosTerminalId())
                        .getResultList();
                if (!checkNotifi.isEmpty() && checkNotifi.get(0).get("is_notify_bill").equals("Y")) {
                    this.sendNotify(SendNotifycationRq.builder()
                            .kitchenOrderLineId(itemKitchenLine.getKitchenOrderLineId())
                            .notifyType("COOKING_SUCCESS")
                            .build());

                    if (!checkIsValidTopping(line)) {

                        updateProduction.put(line.getProductId(), line.getProductionId());
                    }
                }
            }else if (itemKitchenLine.getStatus().equals("PAI")){

                moveByKitchenOrderLineIds.add(itemKitchenLine.getKitchenOrderLineId());
            }
            orgId = line.getOrgId();

            Query queryProcedure = entityManager.createNativeQuery("SELECT pos.update_kitchen_order_status(:kitchenOrderLineId)");
            queryProcedure.setParameter("kitchenOrderLineId", itemKitchenLine.getKitchenOrderLineId());
            Object result = queryProcedure.getSingleResult(); // Assuming the function returns a single value
            if (itemKitchenLine.getStatus().equals("PAI"))
                kitchenOrderIds.add(line.getKitchenOrderId());
        }

        Integer orgIdFinal = orgId;
        if (!createProduction.isEmpty()) {
            // tao production
            Map<Integer, Integer> productAndProductionIdM = new HashMap<>();
            createProduction.entrySet()
                    .forEach(entry -> {
                        Integer productId = entry.getKey();
                        Optional<Integer> warehouseId = mapProduct_Warehouse.entrySet().stream()
                                .filter(entryProduct -> entryProduct.getKey() == productId)
                                .map(Map.Entry::getValue)
                                .findFirst();
                        List<Tuple> rsGetWarehouse = entityManager.createNativeQuery("select * from d_warehouse where d_warehouse_id = :warehouseId",Tuple.class)
                                .setParameter("warehouseId", warehouseId.get())
                                .getResultList();

                        Production production = Production.builder()
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(orgIdFinal)
                                .productionQty(entry.getValue())
                                .productId(entry.getKey())
                                .doctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.PRODUCTION).getId())
                                .documentStatus("DRA")
                                .documentno("KCB" + (productionRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth())
                                .isProcessed("N")
                                .isSyncErp("N")
                                .warehouseId(ParseHelper.INT.parse(rsGetWarehouse.get(0).get("d_warehouse_id")))
                                .movementDate(Instant.now())
                                .name( rsGetWarehouse.get(0).get("name",String.class) + " - " + DateHelper.fromInstant(Instant.now()))
                                .build();
                        production = productionRepository.save(production);
                        Integer productionId = production.getId();
                        productAndProductionIdM.put(productId, production.getId());

				        // tao line production
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());

                        HttpEntity<String> entity = new HttpEntity<>(headers);

                        GlobalReponse exService = restTemplate.exchange(
                                AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_FIND_PRODUCTISCOMBO_BYPRODUCID + "/" + productId,
                                HttpMethod.GET,
                                entity,
                                GlobalReponse.class
                        ).getBody();

                        List<ProductComboDto> productComboDtos = new ArrayList<>();
                        Object data = exService.getData();

                        if (data instanceof List<?>) {
                            List<?> dataList = (List<?>) data;
                            productComboDtos = dataList.stream()
                                    .map(item -> modelMapper.map(item, ProductComboDto.class))
                                    .collect(Collectors.toList());
                        }

                        //End Product
                        ProductionLine productionLine = ProductionLine.builder()
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(orgIdFinal)
                                .productionId(productionId)
                                .productId(productId)
                                .plannedQty(entry.getValue())
                                .usedQty(entry.getValue())
                                .isEndProduct("Y")
                                .lineNo(10)
                                .build();
                        productionLineRepository.save(productionLine);
                        final Integer[] lineNo = {20};
                        if (!productComboDtos.isEmpty()) {

                            productComboDtos.forEach(item -> {
                                ProductionLine line = ProductionLine.builder()
                                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                                        .orgId(orgIdFinal)
                                        .productionId(productionId)
                                        .productId(item.getComponent().getId())
                                        .usedQty(item.getQty().multiply(entry.getValue()))
                                        .isEndProduct("N")
                                        .plannedQty(item.getQty().multiply(entry.getValue()))
                                        .lineNo(lineNo[0])
                                        .build();
                                productionLineRepository.save(line);
                                lineNo[0] += 10;
                            });
                        }
                        //gan production_id lai cho kitchen order line
                        request.getKitchenOrderLineList().stream().filter(line -> line.getProductId().equals(productId))
                                .forEach(line -> {
                                    KitchenOrderLine update = entityRepository.findById(line.getKitchenOrderLineId()).get();
                                    update.setProductionId(productionId);
                                    entityRepository.save(update);
                                });
                    });
        }
        List<Integer> tranProductionIds = new ArrayList<>();

        if (!updateProduction.isEmpty()) {
            updateProduction.entrySet()
                    .forEach(entry -> {
                        Integer productId = entry.getKey();
                        Integer productionId = entry.getValue();
                        Production production = productionRepository.findById(productionId).orElseThrow(() -> new ObjectNotFoundException("Production not found"));
                        if(!(production.getDocumentStatus() != null && production.getDocumentStatus().equals("COM"))) {
                            log.info("tran productionId: " + productionId);
                            tranProductionIds.add(productionId);
                            production.setDocumentStatus("COM");
                            production.setIsProcessed("Y");
                            productionRepository.save(production);
                        }

                    });
        }
        productionRepository.flush();
        entityRepository.flush();
        kitchenOrderRepository.flush();
        productionLineRepository.flush();

        if((tranProductionIds != null && tranProductionIds.size() > 0) || ( moveByKitchenOrderLineIds != null
                && moveByKitchenOrderLineIds.size() > 0)){
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
            headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
            TransactionDto transactionDto = TransactionDto.builder()
                    .orgId(request.getOrgId())
//                    .productionIds(tranProductionIds)
                    .build();
            if (tranProductionIds != null && tranProductionIds.size() > 0) {
                transactionDto.setProductionIds(tranProductionIds);
            }else{
                transactionDto.setKitchenOrderLineIds(moveByKitchenOrderLineIds);
            }
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

//        if(moveByKitchenOrderLineIds !=null && moveByKitchenOrderLineIds.size() > 0){
//            // Create headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
//// For example, adding an Authorization header
//            headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
//            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
//            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
//            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
//            TransactionDto transactionDto = TransactionDto.builder()
//                    .orgId(request.getOrgId())
//                    .productionIds(tranProductionIds)
//                    .build();
//
//            HttpEntity<TransactionDto> requestEntity = new HttpEntity<>(transactionDto, headers);
//            GlobalReponse responseTransaction = this.restTemplate
//                    .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION ,
//                            requestEntity,
//                            GlobalReponse.class)
//                    .getBody();
//            if(responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
//                    && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
//                throw new RuntimeException(responseTransaction.getMessage());
//            }
//        }

        log.info("******************************");
        for (Integer kitchenOrderId : kitchenOrderIds) {
            String sqlCheck = "select orderline_status from d_kitchen_orderline where d_kitchen_order_id = :kitchenOrderId";
            List<String> orderLineStatus = entityManager.createNativeQuery(sqlCheck)
                    .setParameter("kitchenOrderId", kitchenOrderId)
                    .unwrap(NativeQuery.class)
                    .addScalar("orderline_status", StandardBasicTypes.STRING)
                    .getResultList();
            if (orderLineStatus != null && !orderLineStatus.isEmpty()) {
                log.info("orderLineStatus: " + orderLineStatus);
            }
        }

        for (Integer kitchenOrderId : kitchenOrderIds) {
            if (request.getOrgId() == null || kitchenOrderId == null)
                continue;
            try {
                SyncOrderInfoDto syncOrderInfoDto = SyncOrderInfoDto.builder()
                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                        .orgId(request.getOrgId())
                        .kitchenOrderId(kitchenOrderId)
                        .userId(request.getUserId())
                        .build();
                kafkaTemplate.send(TOPIC, syncOrderInfoDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GlobalReponse response = new GlobalReponse();
        response.setData(request);
        response.setMessage(messageSource.getMessage("kitchen_order_update_status", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");

        return response;
    }

    @Override
    public GlobalReponse findAllProductSameSelected(GetKolSameProductVRequest request) {
        log.info("*** KitchenOrderLine, service; find all product same selected *");

        log.info("request: {}", request);
        log.info("request: {}", AuditContext.getAuditInfo().getTenantId());

        Specification<GetKolSameProductV> spec = GetKolSameProductVSpecification.getEntity(request);

        List<GetKolSameProductV> results = getKolSameProductVRepository.findAll(spec);
        List<GetKolSameProductVDto> resultsDto = results.stream()
                .map(item -> {
                    GetKolSameProductVDto itemDto = modelMapper.map(item, GetKolSameProductVDto.class);
                    itemDto.setCreated(DateHelper.fromInstant(item.getCreated()));
                    return itemDto;
                })
                .collect(Collectors.toList());

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .errors("")
                .data(resultsDto)

                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getHistoryKichen(KitchenOrderRequest request) {
        log.info("*** KitchenOrderLine, service; get history kitchen *");
        String sql = "SELECT kol.created, kol.d_product_id, kol.d_pos_order_id, kol.name, kol.qty, kol.full_name ,kol.description, kol.note " +
                "FROM pos.kitchen_order_line_v kol " +
                "WHERE kol.d_org_id = :orgId " +
                "AND kol.d_tenant_id = :tenantId " +
                "AND kol.d_pos_order_id = :posOrderId " +
                "GROUP BY kol.created, kol.d_product_id, kol.d_pos_order_id, kol.name, kol.qty, kol.full_name ,kol.description, kol.note " +
                "ORDER BY kol.created DESC";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("orgId", request.getOrgId());
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        query.setParameter("posOrderId", request.getPosOrderId());

        List<Object[]> results = query.getResultList();

        Map<String, List<KitchenOrderHistory.item>> groupedResults = new HashMap<>();


        for (Object[] row : results) {
            String created = DateHelper.fromTimeStamp((Timestamp) row[0]);
            KitchenOrderHistory.item item = new KitchenOrderHistory.item(((BigDecimal) row[1]).intValue(), ((BigDecimal) row[2]).intValue(), (String) row[3], ((BigDecimal) row[4]).intValue(), (String) row[5], (String) row[6], (String) row[7]);
            groupedResults.computeIfAbsent(created, k -> new ArrayList<>()).add(item);
        }

        return GlobalReponse.builder()
                .data(groupedResults)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse sendNotify(SendNotifycationRq request) {
        log.info("*** KitchenOrderLine, service; send notify *");
        log.info("*** request: {}", request.toString());
        log.info("*** request: {}", request.getKitchenOrderLineId());
        log.info("*** request: {}", request.getPriorityLevel()); 
        KitchenOrderLine kol = null;
        KitchenOrder ko = null;
        KitchenOrderLineView kolv = null;
        if (!request.getNotifyType().equals("SEND_KDS") && !request.getNotifyType().equals("SEND_WDS")) {
            kol = entityRepository.findById(request.getKitchenOrderLineId()).orElseThrow(() ->
                    new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));

            ko = kitchenOrderRepository.findById(kol.getKitchenOrderId()).orElseThrow(() ->
                    new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));

            kolv = kitchenOrderLineViewRepository.findById(kol.getId()).orElseThrow(() ->
                    new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));
        } else {
            ko = kitchenOrderRepository.findById(request.getKitchenOrderId()).orElseThrow(() ->
                    new ObjectNotFoundException(messageSource.getMessage("kitchen_order_notFound", null, LocaleContextHolder.getLocale())));
        }


        String NOTIFICATION_TITLE = "";
        String NOTIFICATION_BODY = "";
        String MSG_SPEAK = "";
        String Router = "NOTIFI_KDS";
        LocalDateTime currenTime = LocalDateTime.now();
        LocalDateTime timeOrder = LocalDateTime.ofInstant(ko.getDateordered(), ZoneId.of("Asia/Ho_Chi_Minh"));

        // get message title + body
        switch (request.getNotifyType()) {
            case "COOKING_SUCCESS":
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_Cooking_Success_Title);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_Cooking_Success_Body);
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@product_name@", kolv.getProduct().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@ticket_id@", tableRepository.findById(ko.getTableId()).get().getName());
                Router = "KITCHEN_SUCCESS";
                break;
            case "SEND_KDS":
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_Send_KDS_Title);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_Send_KDS_Body); // Có món mới đang chờ chế biến từ @Table@ - Khu vực @Floor@ : @Product_Name@ - Số lượng: @QTY@
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Table@", tableRepository.findById(ko.getTableId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Floor@", floorRepository.findById(ko.getFloorId()).get().getName());
                MSG_SPEAK = getMessage(AppConstant.speak_SendKDS);
                break;
            case "REMIND_KDS":
                log.info(kolv.toString());
                log.info("product Name : " + kolv.getProduct().getName());
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_Remind_Title);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_Remind_Body);//[Bàn @Table@ - Khu vực @Floor@] Nhắc chế biến món: @Product_Name@ - SL: @QTY@ @UOM@ - Thời gian đã đợi: @TIME_WAITING@ phút - Mức độ ưu tiên: @PRIORITY@ - Ghi chú: @NOTE@.
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Table@", tableRepository.findById(ko.getTableId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Floor@", floorRepository.findById(ko.getFloorId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Product_Name@", kolv.getProduct().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@QTY@", kol.getQty().toString());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@UOM@", kolv.getUomName() != null ? kolv.getUomName() : "");
                String timeWaiting = String.valueOf(ChronoUnit.MINUTES.between(timeOrder, currenTime));
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@TIME_WAITING@", timeWaiting);
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@PRIORITY@", request.getPriorityLevel());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@NOTE@", request.getNote());
                String speak = getMessage(AppConstant.speak_RemindKDS);
                speak = speak.replace("@Product_Name@", kolv.getProduct().getName());
                MSG_SPEAK = speak;
                kol.setPriority(request.getPriorityLevel());
                kol.setNote(request.getNote());
                entityRepository.save(kol);
                break;
            case "REMIND_PRETIME":
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_RemindPreTime_Title);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_RemindPreTime_Body); // [Bàn: @Table@ - Khu vực: @Floor@] Nhắc chế biến món: @Product_Name@ @UOM@ - SL: @QTY@  - Thời gian đã đợi:  @TIME_WAITING@
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Table@", tableRepository.findById(ko.getTableId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Floor@", floorRepository.findById(ko.getFloorId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Product_Name@", kolv.getProduct().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@UOM@", kolv.getUomName() != null ? kolv.getUomName() : "");
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@QTY@", kol.getQty().toString());
                String timeWaitingPre = String.valueOf(ChronoUnit.MINUTES.between(timeOrder, currenTime));
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@TIME_WAITING@", timeWaitingPre);
//                MSG_SPEAK = getMessage(AppConstant.speak_RemindKDS);
                break;
            case "REMIND_COOKTIME":
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_RemindCookTime_Title);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_RemindCookTime_Body);//[Bàn @Table@ - Khu vực @Floor@] Nhắc hoàn thành món: @Product_Name@ - SL: @QTY@ [@UOM@] - Thời gian vượt quá:  @TIME_WAITING@ - @Cooking_Time@ phút
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Table@", tableRepository.findById(ko.getTableId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Floor@", floorRepository.findById(ko.getFloorId()).get().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Product_Name@", kolv.getProduct().getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@QTY@", kol.getQty().toString());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@UOM@", kolv.getUomName() != null ? kolv.getUomName() : "");
                String timeWaitingCook = String.valueOf(ChronoUnit.MINUTES.between(timeOrder, currenTime));
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@TIME_WAITING@", timeWaitingCook);
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@Cooking_Time@", request.getCookingTime());
//                MSG_SPEAK = getMessage(AppConstant.speak_RemindKDS);
                break;
            case "SEND_WDS":
                setLocale("vi");
                NOTIFICATION_TITLE =  messageSource.getMessage("wds.title", null, LocaleContextHolder.getLocale());
//                NOTIFICATION_TITLE = "SEND_WDS";
                NOTIFICATION_BODY = messageSource.getMessage("wds.send.notify", null, LocaleContextHolder.getLocale());
                MSG_SPEAK = NOTIFICATION_BODY;
                Router = "NOTIFY_WDS";
                break;
            default:
                break;
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
        // send notify
        switch (request.getNotifyType()) {
            case "COOKING_SUCCESS":
                String token = getAllUserDeviceToken(ko);
                if (getAllUserDeviceToken(ko) != null) {
                    SendNotification send = SendNotification.builder()
                            .title(NOTIFICATION_TITLE)
                            .body(NOTIFICATION_BODY)
                            .code(null)
                            .deviceToken(token)
                            .router(Router)
                            .type("ANN")
                            .build();
                    HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
                    exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);
                }
                break;
            case "SEND_KDS":
            case "REMIND_KDS":
            case "REMIND_PRETIME":
            case "REMIND_COOKTIME":
                List<String> deviceToken = getAllWarehouseDeviceToken(kol, ko);
                if (deviceToken != null) {
                    SendNotification send = SendNotification.builder()
                            .title(NOTIFICATION_TITLE)
                            .body(NOTIFICATION_BODY)
                            .code(null)
                            .deviceTokens(deviceToken)
                            .router(Router)
                            .type("ANN")
                            .speak(MSG_SPEAK)
                            .build();
                    HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
                    exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);
                }
                break;
            case "SEND_WDS":
                List<String> deviceToken2 = getAllWarehouseDeviceToken(kol, ko);
                log.info("notifu send wds");
                if (deviceToken2 != null) {
                    SendNotification send = SendNotification.builder()
                            .title(NOTIFICATION_TITLE)
                            .body(NOTIFICATION_BODY)
                            .deviceTokens(deviceToken2)
                            .router(Router)
                            .type("ANN")
                            .speak(MSG_SPEAK)
                            .build();
                    HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
                    exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);
                }
                break;

        }
        boolean isNotify = false;
        if (exService != null)
            isNotify = exService.getStatus() == HttpStatus.OK.value();
        return GlobalReponse.builder()
                .data(isNotify)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .build();
    }

    /**
     *
     * @param productId
     * @return
     */
    @Override
    public GlobalReponse getProductComboTest(Integer productId) {
        List<ProductComboDto> productComboDtos = new ArrayList<>();
        StringBuilder sqlGetProductCombo = new StringBuilder("select d_product_component_id,d_product_id,qty from d_product_combo where d_product_id = :productId and d_tenant_id = :tenantId");

        List<Tuple> results = entityManager.createNativeQuery(sqlGetProductCombo.toString(), Tuple.class)
                .setParameter("productId", productId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getResultList();

        for (Tuple tuple : results) {
            ProductComboDto item = ProductComboDto.builder()
                    .productId(ParseHelper.INT.parse(tuple.get("d_product_id")))
                    .productComponentId(ParseHelper.INT.parse(tuple.get("d_product_component_id")))
                    .qty(ParseHelper.BIGDECIMAL.parse(tuple.get("qty")))
                    .build();
            productComboDtos.add(item);
        }
        return GlobalReponse.builder().data(productComboDtos).message("success").status(HttpStatus.OK.value()).build();
    }

    public String getMessage(String value) {
        String sql = "SELECT msg_text FROM pos.d_message WHERE value = :value";

        Map<String, Object> result = (Map<String, Object>) entityManager.createNativeQuery(sql)
                .setParameter("value", value)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getSingleResult();

        return ParseHelper.STRING.parse(result.get("msg_text"));

    }

    public String getAllUserDeviceToken(KitchenOrder rq) {
        String result = null;
        String sql = "\n" +
                "select du.device_token from d_kitchen_order dko left join d_user  du on dko.d_user_id = du.d_user_id where dko.d_kitchen_order_id = :kitchenOrderId and du.device_token is not null";
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("kitchenOrderId", rq.getId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        for (Map<String, Object> row : results) {
            result = ParseHelper.STRING.parse(row.get("device_token"));
        }
        return result;
    }

    public List<String> getAllWarehouseDeviceToken(KitchenOrderLine rq, KitchenOrder kitchenOrder) {
        List<String> result = new ArrayList<>();
        String sql;
        List<Map<String, Object>> results = new ArrayList<>();
        if (rq != null) {
            sql = "select dw.device_token from d_warehouse dw join d_product_location dpl on dw.d_warehouse_id = dpl.d_warehouse_id where dpl.d_product_id =:productId and dpl.d_tenant_id =:tenantId and dpl.d_org_id = :orgId and dw.device_token is not null";

            results = entityManager.createNativeQuery(sql)
                    .setParameter("productId", rq.getProductId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orgId", rq.getOrgId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
        } else {
            sql = "select dw.device_token from d_warehouse dw   where  dw.d_tenant_id =:tenantId and dw.d_warehouse_id = :warehouseId and dw.device_token is not null";

            results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("warehouseId", kitchenOrder.getWarehouseId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
        }

        for (Map<String, Object> row : results) {
            result.add(ParseHelper.STRING.parse(row.get("device_token")));
        }
        return result;
    }

    public List<Map<String, Object>> excuteQuery(String sql, Map<String, Object> parameters) {
        Query query = entityManager.createNativeQuery(sql);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> results = query.getResultList();
        return results;
    }

    public void setLocale(String lang) {
        Locale locale = Locale.lookup(Locale.LanguageRange.parse(lang), Arrays.asList(Locale.getAvailableLocales()));
        LocaleContextHolder.setLocale(locale);
    }

    private Boolean checkIsValidTopping(KitchenOrderLine entity){
        String sql = "select is_topping, is_active, name from d_product where d_product_id = :product_id ";

        String isTopping = null,
                isActive = null,
                name = null;

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("product_id", entity.getProductId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        if(results != null && !results.isEmpty()) {

            for ( Map<String, Object> row : results ) {

                log.info("Row: {}", row);

                isTopping = ParseHelper.STRING.parse(row.get("is_topping"));
                isActive = ParseHelper.STRING.parse(row.get("is_active"));
                name = ParseHelper.STRING.parse(row.get("name"));
            }
        }

        if ("Y".equals(isTopping)) {

            if ("Y".equals(isActive)) {

                return true;
            } else {

                throw new PosException(messageSource.getMessage("kitchen_order_line_invalid", new Object[]{name}, LocaleContextHolder.getLocale()));
            }
        }
        return false;
    }

}
