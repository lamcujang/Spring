package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.domain.view.GetKcDocNoV;
import com.dbiz.app.orderservice.domain.view.GetKolSameProductV;
import com.dbiz.app.orderservice.domain.view.KitchenOrderGetAllV;
import com.dbiz.app.orderservice.helper.KitchenOrderMapper;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.service.KitchenOrderLineService;
import com.dbiz.app.orderservice.service.KitchenOrderService;
import com.dbiz.app.orderservice.specification.GetKolSameProductVSpecification;
import com.dbiz.app.orderservice.specification.KitchenOrderGetAllVSpecification;
import com.dbiz.app.orderservice.specification.KitchenOrderSpecification;
import com.dbiz.app.orderservice.specification.WDSDetaiHistorySpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.orderDto.dtoView.*;
import org.common.dbiz.dto.orderDto.response.*;
import org.common.dbiz.dto.paymentDto.PaymentMethodDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.TotalPosOrderCostDto;
import org.common.dbiz.dto.systemDto.ReferenceListDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.GetKolSameProductVRequest;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.common.dbiz.request.orderRequest.SendNotifycationRq;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class KitchenOrderServiceImpl implements KitchenOrderService {

    private final RequestParamsUtils requestParamsUtils;

    private final KitchenOrderRepository entityRepository;

    private final KitchenOrderMapper kitchenMapper;

    private final ModelMapper modelMapper;

    private final KitchenOrderlineRepository kitchenOrderlineRepository;

    private final EntityManager entityManager;

    private final RestTemplate restTemplate;

    private final FloorRepository floorRepository;

    private final TableRepository tableRepository;

    private final KitchenOrderByStatusVRepository kitchenOrderByStatusVRepository;

    private final KitchenOrderLineViewRepository kitchenOrderLineViewRepository;

    private final DoctypeRepository doctypeRepository;

    private final MessageSource messageSource;

    private final ProductionRepository productionRepository;

    private final ProductionLineRepository productionLineRepository;

    private final KitchenOrderGetAllVRepository kitchenOrderGetAllVRepository;

    private final GetKcDocNoVRepository getKcDocNoVRepository;

    private final PosOrderRepository posOrderRepository;

    private final PosOrderlineRepository posOrderlineRepository;

    private final KitchenOrderLineService kitchenOrderLineService;
    private StringBuilder sql;

    private final WDSHeaderRepository wdsHeaderRepository;

    private final WDSDetailRepository wdsDetailRepository;

    private final WDSDetailHistoryRepository wdsDetailHistoryRepository;

    @Override
    public GlobalReponsePagination findAll(KitchenOrderRequest request) {
        log.info("*** KitchenOrder List, service; fetch all KitchenOrder *");
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Specification<KitchenOrderGetAllV> spec = KitchenOrderGetAllVSpecification.getEntity(request);
        Page<KitchenOrderGetAllV> kitchenOrders = kitchenOrderGetAllVRepository.findAll(spec, pageable);
        List<KitchenOrderGetAllVDto> listData = new ArrayList<>();
        kitchenOrders.getContent().forEach((item) -> {
            KitchenOrderGetAllVDto dto = modelMapper.map(item, KitchenOrderGetAllVDto.class);
            dto.setDateordered(DateHelper.fromInstantUTC(item.getDateordered()));

            List<KitchenOrderLineDetailViewDto> kitchenOrderlineDtos = kitchenOrderLineViewRepository.findByKitchenOrderId(item.getId(), null).stream()
                    .map(line -> {
                        KitchenOrderLineDetailViewDto lineDto = modelMapper.map(line, KitchenOrderLineDetailViewDto.class);
                        lineDto.setCreated(DateHelper.fromInstantUTC(line.getCreated()));
                        ProductVDto product = modelMapper.map(line.getProduct(), ProductVDto.class);
                        product.setCode(line.getProduct().getProductCode());
                        lineDto.setProduct(product);
                        return lineDto;
                    })
                    .collect(Collectors.toList());

            if (!kitchenOrderlineDtos.isEmpty()) {

                dto.setListKitchenOrderline(mapParentProductsToToppingsForFindAll(kitchenOrderlineDtos, item.getId()));
            }
            listData.add(dto);
        });

        return GlobalReponsePagination.builder()
                .data(listData)
                .currentPage(kitchenOrders.getNumber())
                .pageSize(kitchenOrders.getSize())
                .totalPages(kitchenOrders.getTotalPages())
                .totalItems(kitchenOrders.getTotalElements())
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse save(KitchenOrderDto kitchenOrderDto) {
        log.info("*** KitchenOrder, service; save KitchenOrder ***");
        log.info("params: {}", kitchenOrderDto);
        Map<Integer, List<KitchenOrderLine>> mapCreate = new HashMap<>();
        KitchenOrderDto responseData = new KitchenOrderDto();
        mapCreate = this.groupProductByWarehouse(kitchenOrderDto);
        log.info(mapCreate.toString());
        List<PrinterDto> listProductPrinter = new ArrayList<>();
        List<KitchenOrderlineDto> kitchenOrderlineList = new ArrayList<>();

        for (Map.Entry<Integer, List<KitchenOrderLine>> item : mapCreate.entrySet()) {
            if(item.getKey() == null)
                throw new PosException(messageSource.getMessage("product.not.assign.location", null, LocaleContextHolder.getLocale()));
            KitchenOrder entity = new KitchenOrder();
            entity.setDateordered(DateHelper.toInstantNowUTC());
            entity.setWarehouseId(item.getKey());
            entity.setPosOrderId(kitchenOrderDto.getPosOrderId());
            entity.setOrderStatus(AppConstant.KitchenStatus.KITCHEN_WAITING);
            entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entity.setOrgId(kitchenOrderDto.getOrgId());
            entity.setDocumentno("KDS" + (this.entityRepository.getMaxKitchenOrderId() + 1));
            entity.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.KITCHEN_ORDER).getId());
            entity.setTableId(kitchenOrderDto.getTableId());
            entity.setFloorId(kitchenOrderDto.getFloorId());
            entity.setUserId(kitchenOrderDto.getUserId());
            entity.setPosTerminalId(kitchenOrderDto.getPosTerminalId());
            entity.setDescription(kitchenOrderDto.getDescription());
            if(kitchenOrderDto.getIsSyncErp() == null) entity.setIsSyncErp("N");
            entity = this.entityRepository.save(entity);
            PosOrder posOrderUpdate = this.posOrderRepository.findById(kitchenOrderDto.getPosOrderId()).orElseThrow(() -> new ObjectNotFoundException("PosOrder not found"));
            posOrderUpdate.setKitchenOrderId(entity.getId());
            posOrderRepository.save(posOrderUpdate);
            // send notification
            List<Tuple> checkNotifi = entityManager.createNativeQuery("select is_notify_bill from d_pos_terminal where d_pos_terminal_id = ?",Tuple.class)
                    .setParameter(1, kitchenOrderDto.getPosTerminalId())
                    .getResultList();

            String tableName = "";
            String floorName = "";

            tableName = tableRepository.findNameById(entity.getTableId());
            floorName = floorRepository.getNameFloorById(entity.getFloorId());

            if(!checkNotifi.isEmpty() && checkNotifi.get(0).get("is_notify_bill").equals("Y")) {

                SendNotifycationRq sendNotifycationRq = new SendNotifycationRq();
                sendNotifycationRq.setTableName(tableName);
                sendNotifycationRq.setFloorName(floorName);
                sendNotifycationRq.setKitchenOrderId(entity.getId());
                sendNotifycationRq.setNotifyType("SEND_KDS");
                this.kitchenOrderLineService.sendNotify(sendNotifycationRq);
            }
            KitchenOrder finalEntity = entity;
            Integer kitchenOrderId = entity.getId();

            // Setup thông tin in bill
            String sql = "SELECT " +
                    " printer_product_id, " +
                    " printer_vendor_id, " +
                    " printer_name, " +
                    " printer_page_size, " +
                    " printer_page_so_qty, " +
                    " printer_page_temp_qty, " +
                    " printer_type,printer_port, printer_ip, is_merge_item, name" +
                    " FROM pos.d_warehouse WHERE d_tenant_id = :tenantId " +
                    " AND d_warehouse_id = :warehouseId ";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("warehouseId", item.getKey())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
//            List<PosReceiptOtherDto> posReceiptOtherList = new ArrayList<>();

            // Tao bill cha
            PrinterDto productPrinterDto = new PrinterDto();
            productPrinterDto.setDocumentNo(finalEntity.getDocumentno());
            productPrinterDto.setOrderDate(DateHelper.fromInstantUTC(finalEntity.getDateordered()));
            productPrinterDto.setLinePrinters(new ArrayList<>());

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                productPrinterDto.setPrinterIp(ParseHelper.STRING.parse(row.get("printer_ip")));
                productPrinterDto.setPrinterPort(ParseHelper.BIGDECIMAL.parse(row.get("printer_port")));
                productPrinterDto.setPrinterProductId(ParseHelper.STRING.parse(row.get("printer_product_id")));
                productPrinterDto.setPrinterVendorId(ParseHelper.STRING.parse(row.get("printer_vendor_id")));
                productPrinterDto.setPrinterName(ParseHelper.STRING.parse(row.get("printer_name")));
                productPrinterDto.setPrinterPageSize(ParseHelper.INT.parse(row.get("printer_page_size")));
                productPrinterDto.setPrinterPageSoQty(ParseHelper.INT.parse(row.get("printer_page_so_qty")));
                productPrinterDto.setPrinterPageTempQty(ParseHelper.INT.parse(row.get("printer_page_temp_qty")));
                productPrinterDto.setPrinterType(ParseHelper.STRING.parse(row.get("printer_type")));
                productPrinterDto.setIsMergeItem(ParseHelper.STRING.parse(row.get("is_merge_item")));
                productPrinterDto.setWarehouseName(ParseHelper.STRING.parse(row.get("name")));
            }

            item.getValue().forEach(line -> {

                // Tao bill con
                PrinterDto.PrinterLineDto printerLineDto = new PrinterDto.PrinterLineDto();

                // Lay ten cua mon che bien
                StringBuilder sqlProduct = new StringBuilder("select name from d_product where d_product_id = ? ");
                StringBuilder sqlProductPrimary = new StringBuilder("select dp.name as product_name, du.name as uom_name  from d_product dp \n" +
                        "join d_uom du on dp.d_uom_id  = du.d_uom_id  where dp.d_product_id = :productId ");

                List<Map<String, Object>> resultProduct = entityManager.createNativeQuery(sqlProductPrimary.toString())
                        .setParameter("productId", line.getProductId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                String name = null;
                String uomName = null;
                for(Map<String, Object> row : resultProduct) {
                    name = ParseHelper.STRING.parse(row.get("product_name"));
                    uomName = ParseHelper.STRING.parse(row.get("uom_name"));
                }

                // Set thong tin mon che bien cho bill con
                printerLineDto.setProductName(name);
                printerLineDto.setQty(line.getQty());
                printerLineDto.setUomName(uomName);
                printerLineDto.setDescription(line.getDescription());

                if(line.getLineDetails() != null && !line.getLineDetails().isEmpty()) {

                    printerLineDto.setLineDetailPrinter(new ArrayList<>());

                    for(KitchenOrderlineDetailDto lineTopping : line.getLineDetails()) {

                        checkValidExtraTopping(lineTopping);
                        lineTopping.setKitchenOrderId(kitchenOrderId);
                        kitchenOrderlineRepository.save(modelMapper.map(lineTopping, KitchenOrderLine.class));

                        PosOrderline lineUpdate = this.posOrderlineRepository.findById(lineTopping.getPosOrderLineId()).orElseThrow(() -> new ObjectNotFoundException("PosOrderLine not found"));
                        lineUpdate.setKitchenOrderLineId(lineTopping.getId());
                        posOrderlineRepository.save(lineUpdate);

                        // Lay ten cua mon them di kem mon che bien
                        Query toppingQuery = entityManager.createNativeQuery(sqlProduct.toString());
                        toppingQuery.setParameter(1, lineTopping.getProductId());
                        String toppingName = ParseHelper.STRING.parse(toppingQuery.getSingleResult());

                        printerLineDto.getLineDetailPrinter().add(new PrinterDto.PrinterLineDetailDto(toppingName, lineTopping.getQty()));
                    }
                }

                checkValidExtraTopping(modelMapper.map(line, KitchenOrderlineDetailDto.class));

                line.setKitchenOrderId(kitchenOrderId);
                kitchenOrderlineRepository.save(line);

                kitchenOrderlineList.add(modelMapper.map(line, KitchenOrderlineDto.class));

                PosOrderline lineUpdate = this.posOrderlineRepository.findById(line.getPosOrderLineId()).orElseThrow(() -> new ObjectNotFoundException("PosOrderLine not found"));
                lineUpdate.setKitchenOrderLineId(line.getId());
                posOrderlineRepository.save(lineUpdate);

//                StringBuilder sqlWarehouse = new StringBuilder("select printer_ip  from d_warehouse where d_warehouse_id = ? ");
//                log.info("warehouseId: {}", item.getKey());
//                Query queryWarehosue = entityManager.createNativeQuery(sqlWarehouse.toString());
//                queryWarehosue.setParameter(1, item.getKey());
//                String printerIp = ParseHelper.STRING.parse(queryWarehosue.getSingleResult());
//
//                PrinterDto productPrinterDto = new PrinterDto();
//                productPrinterDto.setProductName(name);
//                productPrinterDto.setQty(line.getQty() );
//                productPrinterDto.setDescription(line.getNote());
//                productPrinterDto.setPrinterIp(printerIp);
//                productPrinterDto.setDocumentNo(finalEntity.getDocumentno());
//                productPrinterDto.setOrderDate(DateHelper.fromInstant(finalEntity.getDateordered()));
//
//                listProductPrinter.add(productPrinterDto);

                // Neu kho hang quy dinh gop bill
                if ("Y".equals(productPrinterDto.getIsMergeItem())) {

                    // Add bill con vao bill cha
                    productPrinterDto.getLinePrinters().add(printerLineDto);
                } else {

                    // Add bill moi vao danh sach tra ve
                    PrinterDto productPrinterSplitDto = new PrinterDto();

                    modelMapper.map(productPrinterDto, productPrinterSplitDto);

                    productPrinterSplitDto.setProductName(printerLineDto.getProductName());
                    productPrinterSplitDto.setQty(printerLineDto.getQty());
                    productPrinterSplitDto.setUomName(uomName);
                    productPrinterSplitDto.setDescription(printerLineDto.getDescription());

                    productPrinterSplitDto.setLineDetailPrinter(printerLineDto.getLineDetailPrinter());

                    listProductPrinter.add(productPrinterSplitDto);
                }
            });

            // Neu kho hang quy dinh gop bill
            if ("Y".equals(productPrinterDto.getIsMergeItem())) {

                // Add bill cha vao danh sach tra ve
                listProductPrinter.add(productPrinterDto);
            }

            responseData = kitchenMapper.toKitchenOrderDto(entity);

            responseData.setListKitchenOrderline(kitchenOrderlineList);
            responseData.setNameTable(tableName);
            responseData.setNameFloor(floorName);
        }

        responseData.setPrinterDto(listProductPrinter);

        String sqlUser = "select full_name from pos.d_user where d_user_id = ? ";
        Query query = entityManager.createNativeQuery(sqlUser.toString());
        query.setParameter(1, kitchenOrderDto.getUserId());
        String fullName = ParseHelper.STRING.parse(query.getSingleResult());
        responseData.setUserDto(UserDto.builder()
                .fullName(fullName)
                .build()
        );

        String sqlOrderGuests = "select coalesce(order_guests, 0) from pos.d_pos_order where d_pos_order_id = ?";
        Query queryGuest = entityManager.createNativeQuery(sqlOrderGuests);
        queryGuest.setParameter(1, kitchenOrderDto.getPosOrderId());
        Integer orderGuest = ((Number) queryGuest.getSingleResult()).intValue();
        log.info("orderGuets {}", kitchenOrderDto.getPosOrderId() );
        log.info("orderGuets {}", orderGuest );

        responseData.setOrderGuest(orderGuest);
        responseData.setUserDto(UserDto.builder()
                .fullName(fullName)
                .build()
        );
        return GlobalReponse.builder()
                .data(responseData)
                .message(messageSource.getMessage("send_kitchen_success", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value()).build();

//        if (entitySave != null) // update
//        {
//            kitchenMapper.updateEntity(kitchenOrderDto, entitySave);
//            this.entityRepository.save(entitySave);
//
//            // xu ly line
//            Map<Integer, BigDecimal> createProduction = new HashMap<>();
//            Map<Integer, Integer> updateProduction = new HashMap<>();
//
//            Integer id = entitySave.getId();
//            List<KitchenOrderLine> kitchenOrderLines = kitchenOrderDto.getListKitchenOrderline().stream()
//                    .map(element -> {
//                        // check tao production
//                        KitchenOrderLine kitchenOrderLine = kitchenOrderlineRepository.findByPosOrderLineId(element.getPosOrderLineId()).orElse(null);
//                        if (kitchenOrderLine == null) {
//                            // tao line moi
//                            kitchenOrderLine = modelMapper.map(element, KitchenOrderLine.class);
//                            kitchenOrderLine.setKitchenOrderId(id);
//                            kitchenOrderLine.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                            return kitchenOrderlineRepository.save(kitchenOrderLine);
//                        } else {
//                            // sum (qty) cac line co cung  product_id de tao production neu dang che bien
//                            if (element.getOrderlineStatus().equals("BPR")) {
//                                Integer productId = element.getProductId();
//                                BigDecimal qty = element.getQty();
//                                createProduction.merge(productId, qty, BigDecimal::add);
//                            } else if (element.getOrderlineStatus().equals("PRD")) {
//                                Integer productId = element.getProductId();
//                                Integer productionId = element.getProductionId();
//                                updateProduction.put(productId, productionId);
//                            }
//
//
//                            modelMapper.map(element, kitchenOrderLine);
//                            return kitchenOrderlineRepository.save(kitchenOrderLine);
//                        }
//
//                    })
//                    .collect(Collectors.toList());
//
//            if (!createProduction.isEmpty()) {
//                // tao production
//                Map<Integer, Integer> productAndProductionIdM = new HashMap<>();
//                createProduction.entrySet()
//                        .forEach(entry -> {
//                            Integer productId = entry.getKey();
//                            Production production = Production.builder()
//                                    .productionQty(entry.getValue())
//                                    .productId(entry.getKey())
//                                    .doctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.PRODUCTION).getId())
//                                    .documentStatus("DRA")
//                                    .documentno("PRO" + (productionRepository.getMaxId() + 1))
//                                    .isProcessed("N")
//                                    .isSyncErp("N")
//                                    .build();
//                            production = productionRepository.save(production);
//                            Integer productionId = production.getId();
//                            productAndProductionIdM.put(productId, production.getId());
//
//                            // tao line production
//                            GlobalReponse exService = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_FIND_PRODUCTCOMBO_BYPRODUCID + "/" + productId, GlobalReponse.class);
//                            List<ProductComboDto> productComboDtos = new ArrayList<>();
//                            Object data = exService.getData();
//
//                            if (data instanceof List<?>) {
//                                List<?> dataList = (List<?>) data;
//                                productComboDtos = dataList.stream()
//                                        .map(item -> modelMapper.map(item, ProductComboDto.class))
//                                        .collect(Collectors.toList());
//                            }
//                            if (productComboDtos.isEmpty())
//
//                                throw new PosException(messageSource.getMessage("production_not_setup", null, LocaleContextHolder.getLocale()));
//                            ProductionLine productionLine = ProductionLine.builder()
//                                    .productionId(productionId)
//                                    .productId(productId)
//                                    .isEndProduct("Y")
//                                    .build();
//                            productComboDtos.forEach(item -> {
//                                ProductionLine line = ProductionLine.builder()
//                                        .productionId(productionId)
//                                        .productId(item.getComponent().getId())
//                                        .isEndProduct("N")
//                                        .plannedQty(item.getQty())
//                                        .build();
//                                productionLineRepository.save(line);
//                            });
//
//                            //gan production_id lai cho kitchen order line
//                            kitchenOrderLines.stream().filter(line -> line.getProductId().equals(productId))
//                                    .forEach(line -> {
//                                        line.setProductionId(productionId);
//                                        kitchenOrderlineRepository.save(line);
//                                    });
//                        });
//            }
//
//            if (!updateProduction.isEmpty()) {
//                updateProduction.entrySet()
//                        .forEach(entry -> {
//                            Integer productId = entry.getKey();
//                            Integer productionId = entry.getValue();
//                            Production production = productionRepository.findById(productionId).orElseThrow(() -> new ObjectNotFoundException("Production not found"));
//                            production.setDocumentStatus("COM");
//                            productionRepository.save(production);
//                        });
//            }
//
//            // chuyen ve dto
//            kitchenOrderDto = kitchenMapper.toKitchenOrderDto(entitySave);
//            List<KitchenOrderlineDto> kitchenOrderlineDtos = kitchenOrderLines.stream()
//                    .map(line -> modelMapper.map(line, KitchenOrderlineDto.class))
//                    .collect(Collectors.toList());
//
//            kitchenOrderDto = kitchenMapper.toKitchenOrderDto(entitySave);
//            kitchenOrderDto.setListKitchenOrderline(kitchenOrderlineDtos);
//            response.setData(kitchenOrderDto);
//            response.setMessage(messageSource.getMessage("kitchen_order_update", null, LocaleContextHolder.getLocale()));
//        } else { // insert
//            // group product have same warehouse id
//            entitySave = kitchenMapper.toKitchenOrder(kitchenOrderDto);
//            entitySave.setDateordered(DateHelper.toInstantDateAndTime(kitchenOrderDto.getDateordered()));
//            String docNo = "KDS" + (this.entityRepository.getMaxKitchenOrderId() + 1);
//            entitySave.setDocumentno(docNo);
//            Doctype doctype = doctypeRepository.findByCode(AppConstant.DocTypeCode.KITCHEN_ORDER);
//            entitySave.setDoctypeId(doctype.getId());
//            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
//            entitySave = this.entityRepository.save(entitySave);
//            Integer id = entitySave.getId();
//
//            List<KitchenOrderLine> kitchenOrderLines = kitchenOrderDto.getListKitchenOrderline().stream()
//                    .map(element -> {
//                        KitchenOrderLine line = modelMapper.map(element, KitchenOrderLine.class);
//                        line.setKitchenOrderId(id);
//                        line.setTenantId(AuditContext.getAuditInfo().getTenantId());
//                        return kitchenOrderlineRepository.save(line);
//                    })
//                    .collect(Collectors.toList());
//
//            // chuyen ve dto
//            kitchenOrderDto = kitchenMapper.toKitchenOrderDto(entitySave);
//            List<KitchenOrderlineDto> kitchenOrderlineDtos = kitchenOrderLines.stream()
//                    .map(line -> modelMapper.map(line, KitchenOrderlineDto.class))
//                    .collect(Collectors.toList());
//
//            kitchenOrderDto.setListKitchenOrderline(kitchenOrderlineDtos);
//            response.setData(kitchenOrderDto);
//        }
//        response.setMessage(messageSource.getMessage("kitchen_order_create", null, LocaleContextHolder.getLocale()));
//        response.setStatus(HttpStatus.OK.value());
//        log.info("KitchenOrder saved successfully with ID: {}", entitySave.getId());
//        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete KitchenOrder by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<KitchenOrder> entityDelete = this.entityRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage("floor not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        this.entityRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** KitchenOrder, service; fetch KitchenOrder by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(kitchenMapper.toKitchenOrderDto(this.entityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %d not found", id)))));
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponsePagination findKOrderByStatus(KitchenOrderRequest request) {
        log.info("*** KitchenOrder, service; fetch KitchenOrder by status *");
        request.setOrder("asc");
        if(request.getOrderStatus()[0].equals("PRD"))
            request.setSortBy("updated");
        else
            request.setSortBy("created");

        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<KitchenOrder> spec = KitchenOrderSpecification.getEntity(request);
        GlobalReponsePagination response = new GlobalReponsePagination();
        Integer warehouseId = null;
        if (request.getRole().equals("KST"))
            warehouseId = request.getWarehouseId();
        Page<KitchenOrderGetAllV> kitchenOrders = kitchenOrderGetAllVRepository.findKitchenByOrderLineStatus(request.getOrderStatus(), AuditContext.getAuditInfo().getTenantId(), request.getOrgId(), warehouseId, pageable);

        List<KitchenOrderGetAllVDto> listData = new ArrayList<>();
        for (KitchenOrderGetAllV item : kitchenOrders.getContent()) {

            KitchenOrderGetAllVDto dto = modelMapper.map(item, KitchenOrderGetAllVDto.class);
            dto.setDateordered(item.getDateordered().toString());

            Duration duration = Duration.between(item.getCreated(), DateHelper.toInstantNowUTC());
            dto.setTime(duration.toMinutes() + "");

            List<KitchenOrderLineDetailByStatusVDto> kitchenOrderlineDtos = kitchenOrderByStatusVRepository.findByStatusValueInAndKitchenOrderId(request.getOrderStatus(), item.getId(), null).stream()
                    .map(line -> modelMapper.map(line, KitchenOrderLineDetailByStatusVDto.class))
                    .collect(Collectors.toList());

            if (!kitchenOrderlineDtos.isEmpty()) {

                dto.setListKitchenOrderlineV(mapParentProductsToToppingsForFindKOrderByStatus(kitchenOrderlineDtos, request.getOrderStatus(), item.getId()));
            }
            listData.add(dto);
        }

        response.setData(listData);
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setCurrentPage(kitchenOrders.getNumber());
        response.setPageSize(kitchenOrders.getSize());
        response.setTotalPages(kitchenOrders.getTotalPages());
        response.setTotalItems(kitchenOrders.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponsePagination findKOrderGroupByOrder(KitchenOrderRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        log.info("*** KitchenOrder, service; fetch KitchenOrder by status *");
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        StringBuilder sql = new StringBuilder("select kol.d_production_id,kol.d_product_id from pos.d_kitchen_orderline kol join pos.d_kitchen_order ko on kol.d_kitchen_order_id = ko.d_kitchen_order_id join d_production dp on kol.d_production_id = dp.d_production_id" +
                " where kol.d_tenant_id = ? and kol.d_org_id = ? and kol.orderline_status = ?" +
                "   ");
        if (request.getRole().equals("KST"))
            sql.append(" and ko.d_warehouse_id = ? ");
        sql.append(" group by kol.d_production_id,kol.d_product_id ");
        if(request.getOrderStatus()[0].equals("PRD"))
            sql.append(",dp.updated order by dp.updated asc");
        else
            sql.append(",dp.created order by dp.created asc");
        List<KitchenOrderGroupProduct> responseData = new ArrayList<>();
        Query query = entityManager.createNativeQuery(
                sql.toString());
        query.setParameter(1, AuditContext.getAuditInfo().getTenantId());
        query.setParameter(2, request.getOrgId());
        query.setParameter(3, request.getOrderStatus()[0]);
        if (request.getRole().equals("KST"))
            query.setParameter(4, request.getWarehouseId());
        List<Object[]> resultList = query.getResultList();
        GlobalReponse serviceDto;
        for (Object[] obj : resultList) {
            KitchenOrderGroupProduct groupProduct = new KitchenOrderGroupProduct();
            List<KitchenOrderGroupProductLine> groupProductLines = new ArrayList<>();
            serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_URL + "/" + obj[1], HttpMethod.GET,entityHeader , GlobalReponse.class).getBody();
            groupProduct.setProductDto(modelMapper.map(serviceDto.getData(), ProductDto.class));
            groupProduct.setProductionId(Integer.parseInt(obj[0].toString()));


            // lay danh sach kitchen order line theo production va product
            List<KitchenOrderLine> kitchenOrderLines = kitchenOrderlineRepository.findALlProductIdAndProductionId(
                    AuditContext.getAuditInfo().getTenantId(), request.getOrgId(), Integer.valueOf(obj[0].toString()), Integer.valueOf(obj[1].toString()));

            for (KitchenOrderLine line : kitchenOrderLines) {
                KitchenOrderGroupProductLine groupProductLine = new KitchenOrderGroupProductLine();
                KitchenOrderGetAllV kitchenOrder = kitchenOrderGetAllVRepository.findById(line.getKitchenOrderId()).get();

                List<ProductToppingViewKDSProjection> projections = kitchenOrderlineRepository.findProductToppingByParentId(line.getId());

                List<ProductToppingViewKDSDto> toppingDtos = projections.stream()
                        .map(p -> ProductToppingViewKDSDto.builder()
                                .id(p.getId())
                                .code(p.getCode())
                                .name(p.getName())
                                .qty(p.getQty())
                                .kitchenOrderLineId(p.getKitchenOrderLineId())
                                .parentId(p.getParentId())
                                .build())
                        .collect(Collectors.toList());

                groupProductLine.setTopping(toppingDtos);
                groupProductLine.setKitchenOrderLineId(line.getId());
                groupProductLine.setKitchenOrderId(line.getKitchenOrderId());
                groupProductLine.setNote(line.getNote());
                groupProductLine.setQty(line.getQty());
                FloorKcVDto floorKcVDto = modelMapper.map(kitchenOrder.getFloor(), FloorKcVDto.class);
                TableKcVDto tableKcVDto = modelMapper.map(kitchenOrder.getTable(), TableKcVDto.class);
                groupProductLine.setFloor(floorKcVDto);
                groupProductLine.setTable(tableKcVDto);

                groupProductLines.add(groupProductLine);
            }
            groupProduct.setKitchenOrderGroupProductLines(groupProductLines);

            Production production = productionRepository.findById(Integer.valueOf(obj[0].toString())).orElseThrow(() -> new ObjectNotFoundException("Production not found"));
            Duration duration = Duration.between(production.getCreated(), DateHelper.toInstantNowUTC());

            groupProduct.setTime(duration.toMinutes() + "");
            responseData.add(groupProduct);
        }
        GlobalReponsePagination reponse = new GlobalReponsePagination();
        reponse.setData(responseData);
        reponse.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        reponse.setTotalPages(0);
        reponse.setPageSize(0);
        reponse.setCurrentPage(0);
        reponse.setTotalItems(0l);
        return reponse;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAllDocNo(KitchenOrderRequest request) {

        log.info("*** KitchenOrder, service; fetch all KitchenOrder *");
        Pageable page = requestParamsUtils.getPageRequest(request);
        Specification<GetKcDocNoV> spec = Specification.where(
                (root, query, cb) -> cb.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId())

        );

        spec = spec.and((root, query, cb) -> request.getOrgId() == null ? cb.conjunction() : cb.equal(root.get("orgId"), request.getOrgId()));
        spec = spec.and((root, query, cb) -> request.getDocumentno() == null ? cb.conjunction() : cb.like(cb.lower(root.get("documentNo")), "%" + request.getDocumentno().toLowerCase() + "%"));
        Page<GetKcDocNoV> resultPage = getKcDocNoVRepository.findAll(spec, page);
        List<GetKcDocNoV> resultData = resultPage.getContent();

        return GlobalReponsePagination.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(resultData)
                .currentPage(resultPage.getNumber())
                .totalItems(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .build();
    }

    @Override
    public GlobalReponse intSave(List<KitchenOrderDto> dto) {

        if (dto != null && !dto.isEmpty()) {
            dto.stream().forEach(item -> {
                KitchenOrder entitySave = this.entityRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
                modelMapper.map(item, entitySave);
                this.entityRepository.saveAndFlush(entitySave);
            });
        }
        return GlobalReponse.builder()
                .message("success")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getLimitedKitchenOrderStatus() {

        List<ReferenceListDto>resultDto  = new ArrayList<>();

        String sql = "SELECT name, value FROM pos.d_reference_get_v " +
                " WHERE name_reference = 'Order Status' and value in ('PAI','DMC') order by lineno asc";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        for (Map<String, Object> row : results) {
            ReferenceListDto item = ReferenceListDto.builder()
                    .name(ParseHelper.STRING.parse(row.get("name")))
                    .value(ParseHelper.STRING.parse(row.get("value")))
                    .build();
            resultDto.add(item);
        }
        GlobalReponse response = new GlobalReponse();
//        List<ReferenceGetV> entity = referenceGetVRepository.findByNameReference(nameReference);
        response.setData(resultDto);
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }

    @Override
    public GlobalReponsePagination getWDSList(KitchenOrderRequest request) {

        log.info("*** Kitchen Order service; fetch WDS List *");
        request.setOrder("asc");
        request.setSortBy("created");


        Pageable pageable = requestParamsUtils.getPageRequest(request);
        GlobalReponsePagination response = new GlobalReponsePagination();
        Integer warehouseId = null;
        if (request.getRole().equals("KST"))
            warehouseId = request.getWarehouseId();
        Page<WDSHeader> wdsHeaders = wdsHeaderRepository.findWDSHeaders(request.getOrderStatus(),
                request.getOrgId(),
                warehouseId, pageable);

        List<KitchenOrderGetAllVDto> listData = new ArrayList<>();
        for (WDSHeader item : wdsHeaders.getContent()) {
            KitchenOrderGetAllVDto dto = modelMapper.map(item, KitchenOrderGetAllVDto.class);
            dto.setDateordered(item.getDateordered().toString());
            Duration duration = Duration.between(item.getCreated(), DateHelper.toInstantNowUTC());
            dto.setTime(duration.toMinutes() + "");
            List<KitchenOrderLineByStatusVDto> wdsDetailDtos = wdsDetailRepository.findWDSDetails(request.getOrderStatus(), item.getId()).stream()
                    .map(line -> modelMapper.map(line, KitchenOrderLineByStatusVDto.class))
                    .collect(Collectors.toList());
            dto.setListKitchenOrderlineV(wdsDetailDtos);
            listData.add(dto);
        }
        response.setData(listData);
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setCurrentPage(wdsHeaders.getNumber());
        response.setPageSize(wdsHeaders.getSize());
        response.setTotalPages(wdsHeaders.getTotalPages());
        response.setTotalItems(wdsHeaders.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse sendWDS(KitchenOrderDto dto) {

        log.info("*** KitchenOrder, service; send WDS ***");
        Map<Integer, List<KitchenOrderLine>> mapCreate = new HashMap<>();
        KitchenOrderDto responseData = new KitchenOrderDto();
        mapCreate = this.groupProductByWarehouseRetail(dto);
        log.info(mapCreate.toString());
        List<PrinterDto> listProductPrinter = new ArrayList<>();

        for (Map.Entry<Integer, List<KitchenOrderLine>> item : mapCreate.entrySet()) {
            if(item.getKey() == null)
                throw new PosException(messageSource.getMessage("product.not.assign.location", null, LocaleContextHolder.getLocale()));
            KitchenOrder entity = new KitchenOrder();
            entity.setDateordered(DateHelper.toInstantNowUTC());
            entity.setWarehouseId(item.getKey());
            entity.setPosOrderId(dto.getPosOrderId());
            entity.setOrderStatus(AppConstant.WDSStatus.ADJUSTMENT_PENDING);
            entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entity.setOrgId(dto.getOrgId());
            entity.setDocumentno("WDS" + (this.entityRepository.getMaxKitchenOrderId() + 1));
            entity.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.KITCHEN_ORDER).getId());
            entity.setUserId(dto.getUserId());
            entity.setPosTerminalId(dto.getPosTerminalId());
            entity.setDescription(dto.getDescription());
            if(dto.getIsSyncErp() == null) entity.setIsSyncErp("N");
            entity = this.entityRepository.save(entity);
            PosOrder posOrderUpdate = this.posOrderRepository.findById(dto.getPosOrderId()).orElseThrow(() -> new ObjectNotFoundException("PosOrder not found"));
            posOrderUpdate.setKitchenOrderId(entity.getId());
            posOrderRepository.save(posOrderUpdate);
            // send notification
            List<Tuple> checkNotifi = entityManager.createNativeQuery("select is_notify_bill from d_pos_terminal where d_pos_terminal_id = ?",Tuple.class)
                    .setParameter(1, dto.getPosTerminalId())
                    .getResultList();

            if(!checkNotifi.isEmpty() && checkNotifi.get(0).get("is_notify_bill").equals("Y")) {
                log.info("send notify");
                SendNotifycationRq sendNotifycationRq = new SendNotifycationRq();
                sendNotifycationRq.setKitchenOrderId(entity.getId());
                sendNotifycationRq.setNotifyType("SEND_WDS");
                this.kitchenOrderLineService.sendNotify(sendNotifycationRq);
            }


            KitchenOrder finalEntity = entity;
            Integer kitchenOrderId = entity.getId();
            item.getValue().forEach(line -> {
                line.setKitchenOrderId(kitchenOrderId);
                kitchenOrderlineRepository.save(line);

                PosOrderline lineUpdate = this.posOrderlineRepository.findById(line.getPosOrderLineId()).orElseThrow(() -> new ObjectNotFoundException("PosOrderLine not found"));
                lineUpdate.setKitchenOrderLineId(line.getId());
                posOrderlineRepository.save(lineUpdate);

                StringBuilder sqlProduct = new StringBuilder("select name  from d_product where d_product_id = ? ");
                Query query = entityManager.createNativeQuery(sqlProduct.toString());
                query.setParameter(1, line.getProductId());
                String name = ParseHelper.STRING.parse(query.getSingleResult());


                String sql = "SELECT " +
                        " printer_product_id, " +
                        " printer_vendor_id, " +
                        " printer_name, " +
                        " printer_page_size, " +
                        " printer_page_so_qty, " +
                        " printer_page_temp_qty, " +
                        " printer_type,printer_port, printer_ip" +
                        " FROM pos.d_warehouse WHERE d_tenant_id = :tenantId " +
                        " AND d_warehouse_id = :warehouseId ";


                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("warehouseId", item.getKey())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();


                PrinterDto productPrinterDto = new PrinterDto();
                productPrinterDto.setProductName(name);
                productPrinterDto.setQty(line.getQty() );
                productPrinterDto.setDescription(line.getNote());
                productPrinterDto.setDocumentNo(finalEntity.getDocumentno());
                productPrinterDto.setOrderDate(DateHelper.fromInstant(finalEntity.getDateordered()));



                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);

                    productPrinterDto.setPrinterIp(ParseHelper.STRING.parse(row.get("printer_ip")));
                    productPrinterDto.setPrinterPort(ParseHelper.BIGDECIMAL.parse(row.get("printer_port")));
                    productPrinterDto.setPrinterProductId(ParseHelper.STRING.parse(row.get("printer_product_id")));
                    productPrinterDto.setPrinterVendorId(ParseHelper.STRING.parse(row.get("printer_vendor_id")));
                    productPrinterDto.setPrinterName(ParseHelper.STRING.parse(row.get("printer_name")));
                    productPrinterDto.setPrinterPageSize(ParseHelper.INT.parse(row.get("printer_page_size")));
                    productPrinterDto.setPrinterPageSoQty(ParseHelper.INT.parse(row.get("printer_page_so_qty")));
                    productPrinterDto.setPrinterPageTempQty(ParseHelper.INT.parse(row.get("printer_page_temp_qty")));
                    productPrinterDto.setPrinterType(ParseHelper.STRING.parse(row.get("printer_type")));
                }
                listProductPrinter.add(productPrinterDto);


            });

            responseData = kitchenMapper.toKitchenOrderDto(entity);
        }
        responseData.setPrinterDto(listProductPrinter);
        return GlobalReponse.builder()
                .data(responseData)
                .message(messageSource.getMessage("send_kitchen_success", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value()).build();
    }

    @Override
    public GlobalReponse getWDSDetailHistory(GetKolSameProductVRequest dto) {

        Specification<WDSDetailHistory> spec = WDSDetaiHistorySpecification.getEntity(dto);

        List<WDSDetailHistory> results = wdsDetailHistoryRepository.findAll(spec);
        List<GetKolSameProductVDto> resultsDto = results.stream()
                .map(item -> {
                    GetKolSameProductVDto itemDto = modelMapper.map(item, GetKolSameProductVDto.class);
                    itemDto.setCreated(DateHelper.fromInstantUTC(item.getCreated()));
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


    private Map<Integer, List<KitchenOrderLine>> groupProductByWarehouse(KitchenOrderDto params) {
        // map all product same warehouse to list
        Map<Integer, List<KitchenOrderLine>> mapProduct = new HashMap<>();

//        sql = new StringBuilder("select\n" +
//                "    coalesce(\n" +
//                "    (select d_warehouse_id from d_product_location dpl\n" +
//                "                                    join d_product dp on dpl.d_product_id = dp.d_product_id\n" +
//                "                                    join d_assign_org_product daop on dp.d_product_id = daop.d_product_id\n" +
//                "     where daop.d_org_id = :orgId and dpl.d_org_id = :orgId \n" +
//                "       and dpl.d_tenant_id = :tenantId \n" +
//                "       and dp.group_type in ('PRD', 'CBP')\n" +
//                "       and dp.d_product_id = :productId \n" +
//                "     and dpl.d_pos_terminal_id = :posTerminalId)\n" +
//                "    , (select d_warehouse_id from d_product_location dpl\n" +
//                "                                      join d_product dp on dpl.d_product_id = dp.d_product_id\n" +
//                "                                      join d_assign_org_product daop on dp.d_product_id = daop.d_product_id\n" +
//                "       where daop.d_org_id = :orgId and dpl.d_org_id =:orgId \n" +
//                "         and dpl.d_tenant_id = :tenantId \n" +
//                "         and dp.group_type in ('PRD', 'CBP')\n" +
//                "         and dp.d_product_id = :productId  and dpl.d_pos_terminal_id is null )\n" +
//                "    ) as warehouse_id , d_product_id ,group_type,name  from d_product where d_product_id = :productId");

        sql = new StringBuilder("select\n" +
                "     (select d_warehouse_id from d_product_location dpl\n" +
                "                                      join d_product dp on dpl.d_product_id = dp.d_product_id\n" +
                "                                      join d_assign_org_product daop on dp.d_product_id = daop.d_product_id\n" +
                "       where daop.d_org_id = :orgId and dpl.d_org_id =:orgId \n" +
                "         and dpl.d_tenant_id = :tenantId \n" +
                "         and dp.group_type in ('PRD', 'CBP')\n" +
                "         and dp.d_product_id = :productId and dpl.is_active = 'Y' and dpl.is_default = 'Y' and daop.is_active = 'Y' )\n" +
                "     as warehouse_id, d_product_id, group_type, name, is_topping from d_product where d_product_id = :productId");
        for (KitchenOrderlineDto item : params.getListKitchenOrderline()) {

            List<KitchenOrderLine> kitchenLine = kitchenOrderlineRepository.findAllByPosOrderLineId(item.getPosOrderLineId());
            if (!kitchenLine.isEmpty())
                continue;
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orgId", item.getOrgId());
            parameters.put("tenantId", AuditContext.getAuditInfo().getTenantId());
            parameters.put("productId", item.getProductId());
            List<Map<String, Object>> results = excuteQuery(sql.toString(), parameters);
            for (Map<String, Object> result : results) {
                // neu la product combo thi lay cac component cua productcombo
                if (ParseHelper.STRING.parse(result.get("group_type")).equals("CBP")) {
                    StringBuilder sqlCombo = new StringBuilder("select d_product_component_id " +
                            " from d_product_combo dpc " +
                            " inner join d_product dp on dpc.d_product_component_id = dp.d_product_id " +
                            " where dpc.d_product_id = :productId and dpc.d_tenant_id = :tenantId " +
                            " and dp.group_type = 'PRD'" +
                            " and dpc.is_item = 'Y'");
                    Query query = entityManager.createNativeQuery(sqlCombo.toString());
                    query.setParameter("productId", item.getProductId());
                    query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
                    List<BigDecimal> productIds = query.getResultList();
                    List<Integer> productIdsCast = productIds.stream().map(BigDecimal::intValue).collect(Collectors.toList());
                    for (Integer productId : productIdsCast) {
                        Map<String, Object> parameters1 = new HashMap<>();
                        parameters1.put("orgId", item.getOrgId());
                        parameters1.put("tenantId", AuditContext.getAuditInfo().getTenantId());
                        parameters1.put("productId", productId);
//                        parameters1.put("posTerminalId", params.getPosTerminalId());
                        List<Map<String, Object>> results1 = excuteQuery(sql.toString(), parameters1);

                        for (Map<String, Object> result1 : results1) {
                            Integer warehouseId = ParseHelper.INT.parse(result1.get("warehouse_id"));
                            KitchenOrderLine line = modelMapper.map(item, KitchenOrderLine.class);
                            line.setPosOrderLineId(item.getPosOrderLineId());
                            line.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            line.setProductId(productId);
                            line.setQty(item.getQty());
                            line = kitchenOrderlineRepository.save(line);
                            if (mapProduct.containsKey(warehouseId)) {
                                mapProduct.get(warehouseId).add(line);
                            } else {
                                List<KitchenOrderLine> list = new ArrayList<>();
                                list.add(line);
                                mapProduct.put(warehouseId, list);
                            }
                        }
                    }

                }
                else if(ParseHelper.STRING.parse(result.get("group_type")).equals("PRD") || "Y".equals(ParseHelper.STRING.parse(result.get("is_topping")))){
                    Integer warehouseId = ParseHelper.INT.parse(result.get("warehouse_id"));
                    KitchenOrderLine line = modelMapper.map(item, KitchenOrderLine.class);
                    line.setPosOrderLineId(item.getPosOrderLineId());
                    line.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    line = kitchenOrderlineRepository.save(line);

                    if(item.getLineDetails() != null && !item.getLineDetails().isEmpty()) {

                        for (KitchenOrderlineDetailDto itemTopping : item.getLineDetails()) {

                            itemTopping.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            itemTopping.setParentId(line.getId());
                            KitchenOrderLine lineTopping = modelMapper.map(itemTopping, KitchenOrderLine.class);
                            lineTopping = kitchenOrderlineRepository.save(lineTopping);
                            itemTopping.setId(lineTopping.getId());
                        }
                    }
                    if (mapProduct.containsKey(warehouseId)) {

                        mapProduct.get(warehouseId).add(line);
                    } else {

                        List<KitchenOrderLine> list = new ArrayList<>();
                        list.add(line);
                        mapProduct.put(warehouseId, list);
                    }
                }

            }
        }

        return mapProduct;
    }

    private List<Map<String, Object>> excuteQuery(String sql, Map<String, Object> parameters) {
        Query query = entityManager.createNativeQuery(sql);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> results = query.getResultList();
        return results;
    }

    private Map<Integer, List<KitchenOrderLine>> groupProductByWarehouseRetail(KitchenOrderDto params) {
        // map all product same warehouse to list
        Map<Integer, List<KitchenOrderLine>> mapProduct = new HashMap<>();

        sql = new StringBuilder("select\n" +
                "     (select d_warehouse_id from d_product_location dpl\n" +
                "                                      join d_product dp on dpl.d_product_id = dp.d_product_id\n" +
                "                                      join d_assign_org_product daop on dp.d_product_id = daop.d_product_id\n" +
                "       where daop.d_org_id = :orgId and dpl.d_org_id =:orgId \n" +
                "         and dpl.d_tenant_id = :tenantId \n" +
//                "         and dp.group_type in ('PRD', 'CBP')\n" +
                "         and dp.d_product_id = :productId  and dpl.is_active = 'Y' and dpl.is_default = 'Y' and daop.is_active = 'Y' )\n" +
                "     as warehouse_id , d_product_id ,group_type,name  from d_product where d_product_id = :productId");
        for (KitchenOrderlineDto item : params.getListKitchenOrderline()) {

            List<KitchenOrderLine> kitchenLine = kitchenOrderlineRepository.findAllByPosOrderLineId(item.getPosOrderLineId());
            if (!kitchenLine.isEmpty())
                continue;
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orgId", item.getOrgId());
            parameters.put("tenantId", AuditContext.getAuditInfo().getTenantId());
            parameters.put("productId", item.getProductId());
            List<Map<String, Object>> results = excuteQuery(sql.toString(), parameters);
            for (Map<String, Object> result : results) {
                // neu la product combo thi lay cac component cua productcombo
                if (ParseHelper.STRING.parse(result.get("group_type")).equals("CBP")) {
                    StringBuilder sqlCombo = new StringBuilder("select d_product_component_id " +
                            " from d_product_combo dpc " +
                            " inner join d_product dp on dpc.d_product_component_id = dp.d_product_id " +
                            " where dpc.d_product_id = :productId and dpc.d_tenant_id = :tenantId " +
//                            " and dp.group_type = 'PRD'" +
                            " and dpc.is_item = 'Y'");
                    Query query = entityManager.createNativeQuery(sqlCombo.toString());
                    query.setParameter("productId", item.getProductId());
                    query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
                    List<BigDecimal> productIds = query.getResultList();
                    List<Integer> productIdsCast = productIds.stream().map(BigDecimal::intValue).collect(Collectors.toList());
                    for (Integer productId : productIdsCast) {
                        Map<String, Object> parameters1 = new HashMap<>();
                        parameters1.put("orgId", item.getOrgId());
                        parameters1.put("tenantId", AuditContext.getAuditInfo().getTenantId());
                        parameters1.put("productId", productId);
//                        parameters1.put("posTerminalId", params.getPosTerminalId());
                        List<Map<String, Object>> results1 = excuteQuery(sql.toString(), parameters1);

                        for (Map<String, Object> result1 : results1) {
                            Integer warehouseId = ParseHelper.INT.parse(result1.get("warehouse_id"));
                            KitchenOrderLine line = modelMapper.map(item, KitchenOrderLine.class);
                            line.setPosOrderLineId(item.getPosOrderLineId());
                            line.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            line.setProductId(productId);
                            line.setQty(item.getQty());
                            line.setOrderlineStatus(AppConstant.WDSStatus.ADJUSTMENT_PENDING);
                            line = kitchenOrderlineRepository.save(line);
                            if (mapProduct.containsKey(warehouseId)) {
                                mapProduct.get(warehouseId).add(line);
                            } else {
                                List<KitchenOrderLine> list = new ArrayList<>();
                                list.add(line);
                                mapProduct.put(warehouseId, list);
                            }
                        }
                    }

                }
//                else if(ParseHelper.STRING.parse(result.get("group_type")).equals("PRD")){
                else {
                    Integer warehouseId = ParseHelper.INT.parse(result.get("warehouse_id"));
                    KitchenOrderLine line = modelMapper.map(item, KitchenOrderLine.class);
                    line.setPosOrderLineId(item.getPosOrderLineId());
                    line.setOrderlineStatus(AppConstant.WDSStatus.ADJUSTMENT_PENDING);
                    line.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    line = kitchenOrderlineRepository.save(line);
                    if (mapProduct.containsKey(warehouseId)) {
                        mapProduct.get(warehouseId).add(line);
                    } else {
                        List<KitchenOrderLine> list = new ArrayList<>();
                        list.add(line);
                        mapProduct.put(warehouseId, list);
                    }
                }

            }
        }

        return mapProduct;
    }

    private void checkValidExtraTopping(KitchenOrderlineDetailDto dto){
        String sql = "select is_topping, is_active, name from d_product where d_product_id = :product_id ";

        String isTopping = null,
               isActive = null,
               name = null;

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("product_id", dto.getProductId())
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

        if(dto.getParentId() != null) {

            if(!"Y".equals(isTopping) || !"Y".equals(isActive)) {

                throw new PosException(messageSource.getMessage("kitchen_order_line_invalid", new Object[]{name}, LocaleContextHolder.getLocale()));

            }
        } else {

            if("Y".equals(isTopping) && !"Y".equals(isActive)) {

                throw new PosException(messageSource.getMessage("kitchen_order_line_invalid", new Object[]{name}, LocaleContextHolder.getLocale()));

            }
        }
    }

    public List<KitchenOrderLineViewDto> mapParentProductsToToppingsForFindAll(List<KitchenOrderLineDetailViewDto> kitchenOrderlineDtos, Integer kitchenOrderId){

        Map<Integer, KitchenOrderLineViewDto> dtoMap = new HashMap<>();
        for(KitchenOrderLineDetailViewDto detail : kitchenOrderlineDtos){

            KitchenOrderLineViewDto parentDto = modelMapper.map(detail, KitchenOrderLineViewDto.class);
            List<KitchenOrderLineDetailViewDto> lineDetail = kitchenOrderLineViewRepository.findByKitchenOrderId(kitchenOrderId, detail.getKitchenOrderLineId()).stream()
                    .map(line -> {
                        KitchenOrderLineDetailViewDto lineDto = modelMapper.map(line, KitchenOrderLineDetailViewDto.class);
                        lineDto.setCreated(DateHelper.fromInstantUTC(line.getCreated()));
                        ProductVDto product = modelMapper.map(line.getProduct(), ProductVDto.class);
                        product.setCode(line.getProduct().getProductCode());
                        lineDto.setProduct(product);
                        return lineDto;
                    })
                    .collect(Collectors.toList());

            parentDto.setLineDetails(lineDetail);
            dtoMap.put(detail.getKitchenOrderLineId(), parentDto);
        }
        return new ArrayList<>(dtoMap.values());
    }

    public List<KitchenOrderLineByStatusVDto> mapParentProductsToToppingsForFindKOrderByStatus(List<KitchenOrderLineDetailByStatusVDto> kitchenOrderlineDtos, String[] orderStatus, Integer kitchenOrderId){

        Map<Integer, KitchenOrderLineByStatusVDto> dtoMap = new HashMap<>();
        for(KitchenOrderLineDetailByStatusVDto detail : kitchenOrderlineDtos){

            KitchenOrderLineByStatusVDto parentDto = modelMapper.map(detail, KitchenOrderLineByStatusVDto.class);
            List<KitchenOrderLineDetailByStatusVDto> lineDetail = kitchenOrderByStatusVRepository.findByStatusValueInAndKitchenOrderId(orderStatus, kitchenOrderId, detail.getId()).stream()
                    .map(line -> modelMapper.map(line, KitchenOrderLineDetailByStatusVDto.class))
                    .collect(Collectors.toList());

            parentDto.setLineDetails(lineDetail);
            dtoMap.put(detail.getId(), parentDto);
        }
        return new ArrayList<>(dtoMap.values());
    }
}
