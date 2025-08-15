package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.domain.view.PODetailV;
import com.dbiz.app.orderservice.domain.view.POHeaderV;
import com.dbiz.app.orderservice.domain.view.WarehousePOV;
import com.dbiz.app.orderservice.repository.ReturnOrderLineRepository;
import com.dbiz.app.orderservice.repository.ReturnOrderRepository;
import com.dbiz.app.orderservice.repository.RoLotRepository;
import com.dbiz.app.orderservice.service.ReturnOrderService;
import com.dbiz.app.orderservice.specification.PODetailVSpecification;
import com.dbiz.app.orderservice.specification.POHeaderVSpecification;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.orderDto.dtoView.*;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.dto.orderDto.response.PosOrderLineResDto;
import org.common.dbiz.dto.orderDto.response.ProductDto;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
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
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReturnOrderServiceImpl implements ReturnOrderService {
    private final ReturnOrderRepository returnOrderRepository;
    private final ReturnOrderLineRepository returnOrderLineRepository;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;
    private final RestTemplate restTemplate;
    private final RoLotRepository roLotRepository;
    private final QueryEngine queryEngine;

    @Override
    public GlobalReponsePagination findAll(ReturnOrderRequest request) {
        Parameter parameter = new Parameter();
        parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("order_date", Param.getBetweenParam(request.getDateFrom(),request.getDateTo()), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        parameter.add("order_status", request.getOrderStatus(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("payment_method", request.getPaymentMethod(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_user_id ", request.getUserSell(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("doc_base_type", request.getDoctypeCode(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("document_no", request.getDocumentNo(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("reference_no", request.getPoDocumentNo(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecords("pos.d_ro_header_v", parameter, request);
        try {
            List<ROHeaderVDto> data = new ArrayList<>();
            while (rs.next()) {
                ROHeaderVDto returnOrderDto = ROHeaderVDto.builder()
                        .id(rs.getInt("d_return_order_id"))
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .fullName(rs.getString("full_name"))
                                .userName(rs.getString("user_name"))
                                .build())
                        .returnReason(ReturnReasonDto.builder()
                                .id( rs.getInt("d_return_reason_id"))
                                .name(rs.getString("return_reason_name"))
                                .build())
                        .orderStatus(rs.getString("order_status"))
                        .orderDate(rs.getObject("order_date") != null
                                ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                : null)//
                        .posOrderId(rs.getInt("d_pos_order_id"))
                        .purchaseOrderId(rs.getInt("d_purchase_order_id"))
                        .documentNo(rs.getString("document_no"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .returnFee(rs.getBigDecimal("return_fee"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .receiptOtherAmount(rs.getBigDecimal("receipt_other_amount"))
                        .flatAmt(rs.getBigDecimal("flat_amt"))
                        .flatDiscount(rs.getBigDecimal("flat_discount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .netReturnAmount(rs.getBigDecimal("net_return_amount"))
                        .referenceNo(rs.getString("reference_no"))
                        .doctypeId(rs.getInt("d_doctype_id"))
                        .docBaseType(rs.getString("doc_base_type"))
                        .docTypeName(rs.getString("doctype_name"))
                        .description(rs.getString("description"))
                        .tenantId(rs.getInt("d_tenant_id"))
                        .org(OrgDto.builder()
                                .id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .build())
                        .priceList(PricelistDto.builder()
                                .id(rs.getInt("d_pricelist_id"))
                                .name(rs.getString("price_name"))
                                    .build())
                        .customer(CustomerVDto.builder()
                                .id(rs.getInt("d_customer_id"))
                                .name(rs.getString("customer_name"))
                                .build())
                        .vendor(VendorPOVDto.builder()
                                .vendorId(rs.getInt("d_vendor_id"))
                                .vendorName(rs.getString("vendor_name"))
                                .build())
                        .payment(PaymentDto.builder()
                                .documentNo(rs.getString("payment_no"))
                                .paymentStatus(rs.getString("payment_status"))
                                .paymentAmount(rs.getBigDecimal("payment_amount"))
                                .paymentMethod(rs.getString("payment_method"))
                                .userId(rs.getInt("user_create_payment"))
                                .paymentDate(rs.getString("payment_date"))
                                .build())
                        .build();
                returnOrderDto.getPayment().setId(rs.getInt("d_payment_id"));
                if(returnOrderDto.getPosOrderId() != null){
                    returnOrderDto.setReceiptOthers(getPosReceiptOther(returnOrderDto.getPosOrderId()));
                }
                data.add(returnOrderDto);
            }

//            request.setOrder(null);
//            request.setSortBy(null);
            Pagination pagination = queryEngine.getPagination("pos.d_ro_header_v", parameter, request);
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        ReturnOrder returnOrder = returnOrderRepository.findById(integer).orElseThrow(
                () -> new ObjectNotFoundException("Looix")
        );
        ReturnOrderDto returnOrderDto = modelMapper.map(returnOrder, ReturnOrderDto.class);

        List<ReturnOrderLine> returnOrderLines = returnOrderLineRepository.findAllByReturnOrderId(returnOrder.getId());
        List<ReturnOrderLineDto> returnOrderLinesdto = new ArrayList<>();
        returnOrderLines.forEach(item -> {
            ReturnOrderLineDto returnOrderLineDto = modelMapper.map(item, ReturnOrderLineDto.class);
            returnOrderLinesdto.add(returnOrderLineDto);
        });
        returnOrderDto.setReturnOrderLines(returnOrderLinesdto);
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(returnOrderDto)
                .build();
    }

    @Override
    public GlobalReponse save(ReturnOrderDto entityDto) {
        log.info("*** Purchase Order, service; save Purchase Order ***" + entityDto);
        GlobalReponse response = new GlobalReponse();


        ReturnOrder entitySave;
        if(entityDto.getId() != null) { // Chỉnh sửa
            entitySave = this.returnOrderRepository.findById(entityDto.getId())
                    .orElseThrow(()-> new ObjectNotFoundException(messageSource.getMessage("return.order.not.found", null, LocaleContextHolder.getLocale())));

            if(entitySave.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)){
                throw new PosException( messageSource.getMessage("ro.already.complete", null, LocaleContextHolder.getLocale()));
            }

            if(entitySave.getOrderStatus().equals(AppConstant.DOC_STATUS_DRAFT)){
                modelMapper.map(entityDto, entitySave);
            }else {
                entitySave.setOrderStatus(AppConstant.DOC_STATUS_VOID);
            }

            entitySave  = returnOrderRepository.save(entitySave);
            response.setStatus(HttpStatus.OK.value());
        }else{ // Add
            entitySave = modelMapper.map(entityDto, ReturnOrder.class);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave.setOrgId(entityDto.getOrgId());

            String doctypeCode = null;
            if(entityDto.getDoctype().equals("RCO")){
                doctypeCode = AppConstant.DocTypeCode.RETURN_CUSTOMER_ORDER;
            }else if(entityDto.getDoctype().equals("RVO")){
                doctypeCode = AppConstant.DocTypeCode.RETURN_VENDOR_ORDER;
            }

            Integer doctypeId = null;

            if(doctypeCode != null){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
                HttpEntity<String> entityHeader = new HttpEntity<>(headers);
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE + "/"
                        + doctypeCode, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();

                doctypeId = modelMapper.map(exRes.getData(), Integer.class);
                entitySave.setDoctypeId(doctypeId);
            }

            if (entityDto.getDocumentNo() == null){
                do{
                    Integer maxId = returnOrderRepository.getMaxId() + 1;
                    String docNo = DocHelper.generateDocNo( entityDto.getDoctype().equals("RCO") ? "THB" : "THN",maxId) ;
                    entitySave.setDocumentNo(docNo);
                }while (checkExistedDocumentNo(entitySave.getDocumentNo(), doctypeId).equals("FAI"));
            }else{
                String checkExistedDocumentNo = checkExistedDocumentNo(entityDto.getDocumentNo(), doctypeId);
                if(checkExistedDocumentNo.equals("FAI")){
                    throw new PosException(messageSource.getMessage("document.no.exists", null, LocaleContextHolder.getLocale()));
                }
            }

            if(entityDto.getOrderStatus() == null){
                entitySave.setOrderStatus(AppConstant.DOC_STATUS_DRAFT); //Moiws tao la DRA
            }

            if(entityDto.getOrderDate() != null){
                entitySave.setOrderDate(DateHelper.toInstantUTC(entityDto.getOrderDate()));
            }else {
                entitySave.setOrderDate(DateHelper.toInstantNowUTC());
            }

            entitySave  = returnOrderRepository.save(entitySave);
            entityDto.setId(entitySave.getId());
            response.setStatus(HttpStatus.CREATED.value());
        }

        Integer id = entitySave.getId();
        if(entityDto.getReturnOrderLines() != null && !entityDto.getReturnOrderLines().isEmpty()) {
            List<ProductDto> listProduct = new ArrayList<>();
            ProductDto pr = new ProductDto();
            entityDto.getReturnOrderLines().forEach(returnOrderLineDto -> {
                ReturnOrderLine returnOrderLine =  saveReturnOrderLine(returnOrderLineDto, id);
                saveLineProductLot(returnOrderLineDto, returnOrderLine, id);
            });
        }

        //Tạo chứng tuwf
        if (entitySave.getOrderStatus() != null && entitySave.getOrderStatus()
                .equals(AppConstant.DOC_STATUS_COMPLETED)) {
            returnOrderRepository.flush();
            returnOrderLineRepository.flush();
            createPayment(entitySave, entityDto);
            GlobalReponse trx =  createInvTrx(entitySave);
            if(trx != null){
                return trx;
            }
        }

        response.setData(entityDto);
        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
//        log.info("Warehouse saved successfully with ID: {}", entitySave.getId());

        return response;
    }

    public String checkExistedDocumentNo(String documentNo, Integer doctypeId) {
        log.info("param: " + documentNo + doctypeId);
        String sql = "SELECT count(1) FROM pos.d_return_order WHERE document_no = :documentNo and d_doctype_id= :doctypeId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("doctypeId", doctypeId);
        query = query.setParameter("documentNo", documentNo);

        log.info("SQL:" + sql);
        Long count = ((Number) query.getSingleResult()).longValue();
        if (count > 0) {
            return "FAI";
        }
        return "COM";
    }

    private void createPayment(ReturnOrder entitySave, ReturnOrderDto entityDto){

            // Create headers

            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
            org.common.dbiz.dto.paymentDto.PaymentDto paymentDto = org.common.dbiz.dto.paymentDto.PaymentDto.builder()
                    .docType("APP")
                    .paymentAmount(entitySave.getTotalAmount())
                    .paymentMethod(entityDto.getPaymentMethod())
                    .paymentStatus("COM")
                    .posTerminalId(entitySave.getPosTerminalId())
//                    .paymentDate(entityDto.getPaymentDate())//
                    .returnOrderId(entitySave.getId())
                    .build();
            paymentDto.setOrgId(entitySave.getOrgId());

            log.info("PaymentDTO: "  +  paymentDto);
            if(entitySave.getVendorId() != null){
                paymentDto.setResponsibleUserId(entitySave.getVendorId());
                paymentDto.setUserGroup("VEN");
            }else {
                paymentDto.setResponsibleUserId(entitySave.getCustomerId());
                paymentDto.setUserGroup("CUS");
            }

            HttpEntity<org.common.dbiz.dto.paymentDto.PaymentDto> requestEntity2 = new HttpEntity<>(paymentDto, headers);
            GlobalReponse responseTransaction = this.restTemplate
                    .postForEntity(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_API_URL,
                            requestEntity2,
                            GlobalReponse.class)
                    .getBody();

            if (responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                    && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
                throw new RuntimeException(responseTransaction.getMessage());
            }

    }

    private GlobalReponse createInvTrx(ReturnOrder entitySave){

        // Create headers

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());


        TransactionDto transactionDto = TransactionDto.builder()
                .orgId(entitySave.getOrgId())
                .returnOrderIds(List.of(entitySave.getId()))
                .build();

        HttpEntity<TransactionDto> requestEntity2 = new HttpEntity<>(transactionDto, headers);
        GlobalReponse responseTransaction = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION,
                        requestEntity2,
                        GlobalReponse.class)
                .getBody();
        if (responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
            return responseTransaction;
        }

        return null;
    }

    private void saveLineProductLot(ReturnOrderLineDto element,ReturnOrderLine returnOrderLine, Integer returnOrderId) {
        if (element.getLots() != null  && !element.getLots().isEmpty()) {
            Integer returnOrderlineId = returnOrderLine.getId();
            Integer orgId = returnOrderLine.getOrgId();

            element.getLots().forEach(item -> {
                RoLot itemSave;
                if(item.getId() != null){
                    itemSave = roLotRepository.findById(item.getId()).orElseThrow(
                            () -> new ObjectNotFoundException(messageSource.getMessage("rolot.not.found", null, LocaleContextHolder.getLocale())));
                    modelMapper.map(item, itemSave);
                }else{
                    itemSave = modelMapper.map(item, RoLot.class);
                    itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                }
                itemSave.setOrgId(orgId);
                itemSave.setReturnOrderId(returnOrderId);
                itemSave.setReturnOrderlineLineId(returnOrderlineId);
                itemSave = roLotRepository.saveAndFlush(itemSave);
                item = modelMapper.map(itemSave, RoLotDto.class);
                item.setId(itemSave.getId());
            });
        }
    }

    private ReturnOrderLine saveReturnOrderLine(ReturnOrderLineDto returnOrderLineDto, Integer id){
        returnOrderLineDto.setReturnOrderId(id);
        ReturnOrderLine returnOrderLine = modelMapper.map(returnOrderLineDto, ReturnOrderLine.class);
        returnOrderLine.setTenantId(AuditContext.getAuditInfo().getTenantId());
        returnOrderLine.setOrgId(returnOrderLineDto.getOrgId());
        return returnOrderLineRepository.save(returnOrderLine);
    }


    public ROHeaderVDto getROHeader(Integer id) {
        Parameter parameter = new Parameter();
        parameter.add("d_return_order_id", id, Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_ro_header_v", parameter);
        ROHeaderVDto data = null;
        try {

            while (rs.next()) {
                data = ROHeaderVDto.builder()
                        .id(rs.getInt("d_return_order_id"))
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .fullName(rs.getString("full_name"))
                                .userName(rs.getString("user_name"))
                                .build())
                        .returnReason(ReturnReasonDto.builder()
                                .id( rs.getInt("d_return_reason_id"))
                                .name(rs.getString("return_reason_name"))
                                .build())
                        .orderStatus(rs.getString("order_status"))
                        .orderDate(rs.getObject("order_date") != null
                                ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                : null)
                        .posOrderId(rs.getInt("d_pos_order_id"))
                        .purchaseOrderId(rs.getInt("d_purchase_order_id"))
                        .documentNo(rs.getString("document_no"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .returnFee(rs.getBigDecimal("return_fee"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .receiptOtherAmount(rs.getBigDecimal("receipt_other_amount"))
                        .flatAmt(rs.getBigDecimal("flat_amt"))
                        .flatDiscount(rs.getBigDecimal("flat_discount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .netReturnAmount(rs.getBigDecimal("net_return_amount"))
                        .referenceNo(rs.getString("reference_no"))
                        .doctypeId(rs.getInt("d_doctype_id"))
                        .docBaseType(rs.getString("doc_base_type"))
                        .docTypeName(rs.getString("doctype_name"))
                        .description(rs.getString("description"))
                        .tenantId(rs.getInt("d_tenant_id"))
                        .org(OrgDto.builder()
                                .id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .build())
                        .priceList(PricelistDto.builder()
                                .id(rs.getInt("d_pricelist_id"))
                                .name(rs.getString("price_name"))
                                .build())
                        .customer(CustomerVDto.builder()
                                .id(rs.getInt("d_customer_id"))
                                .name(rs.getString("customer_name"))
                                .build())
                        .vendor(VendorPOVDto.builder()
                                .vendorId(rs.getInt("d_vendor_id"))
                                .vendorName(rs.getString("vendor_name"))
                                .build())
                        .payment(PaymentDto.builder()
                                .documentNo(rs.getString("payment_no"))
                                .paymentStatus(rs.getString("payment_status"))
                                .paymentAmount(rs.getBigDecimal("payment_amount"))
                                .paymentMethod(rs.getString("payment_method"))
                                .userId(rs.getInt("user_create_payment"))
                                .paymentDate(rs.getString("payment_date"))
                                .build())
                        .build();
                data.getPayment().setId(rs.getInt("d_payment_id"));

                if(data.getPosOrderId() != null){
                    data.setReceiptOthers(getPosReceiptOther(data.getPosOrderId()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public GlobalReponse  findAllDetail(RODetailVRequest request){
        Parameter parameter = new Parameter();
        parameter.add("d_return_order_id", request.getReturnOrderId(), Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_ro_detail_v", parameter);
        try {

            ROHeaderVDto returnOrderDto = getROHeader(request.getReturnOrderId());
            List<RODetailVDto> data = new ArrayList<>();
            while (rs.next()) {
                RODetailVDto roDetailVDto = RODetailVDto.builder()
                        .id(rs.getInt("d_return_orderline_id"))
                        .qty(rs.getBigDecimal("qty"))
                        .remainQty(rs.getBigDecimal("remain_qty"))
                        .returnOrderId(rs.getInt("d_return_order_id"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .totalDiscountAmount(rs.getBigDecimal("total_discount_amount"))
                        .priceDiscount(rs.getBigDecimal("price_discount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .netReturnAmount(rs.getBigDecimal("net_return_amount"))
                        .returnPrice(rs.getBigDecimal("return_price"))
                        .salePrice(rs.getBigDecimal("sale_price"))
                        .posOrderLineId(rs.getInt("d_pos_orderline_id"))
                        .purchaseOrderLineId(rs.getInt("d_purchase_orderline_id"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .description(rs.getString("description"))
                        .productDto(ProductPOVDto.builder()
                                .productId(rs.getInt("d_product_id"))
                                .productCode(rs.getString("product_code"))
                                .productName(rs.getString("product_name"))
                                .build())
                        .imageDto(ImageDto.builder()
                                .id(rs.getInt("d_image_id"))
                                .imageUrl(rs.getString("image_url"))
                                .build())
                        .uom(UomDto.builder()
                                .id(rs.getInt("d_uom_id"))
                                .code(rs.getString("uom_code"))
                                .name(rs.getString("uom_name"))
                                .build())
                        .tax(TaxDto.builder()
                                .id(rs.getInt("d_tax_id"))
                                .taxRate(rs.getBigDecimal("tax_rate"))
                                .name(rs.getString("tax_name"))
                                .build())
                        .lots(getROLOtDto(rs.getInt("d_return_orderline_id")))
                        .build();

//                        if(roDetailVDto.getPosOrderLineId() != null){
//                            roDetailVDto.setReceiptOthers(getPosReceiptOther(roDetailVDto.getPosOrderLineId()));
//                        }
                data.add(roDetailVDto);
            }
            returnOrderDto.setLines(data);
            log.info("Load pagination...");
            return GlobalReponse.builder()
                    .data(returnOrderDto)
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .status(HttpStatus.OK.value())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<RoLotDto> getROLOtDto(Integer returnOrderLineId){

        try {
            String sql = "SELECT " +
                    " d_ro_lot_id," +
                    " d_return_order_id, " +
                    " d_return_orderline_id, " +
                    " d_lot_id ," +
                    " qty, " +
                    " is_active, " +
                    " lot_code, " +
                    " expiry_date " +
                    " FROM pos.d_ro_lot_v WHERE 1 = 1 " +
                    " AND d_return_orderline_id = :returnOrderLineId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("returnOrderLineId", returnOrderLineId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<RoLotDto> data = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                RoLotDto dto = RoLotDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_ro_lot_id")))
                        .requestOrderId(ParseHelper.INT.parse(row.get("d_return_order_id")))
                        .requestOrderLineId(ParseHelper.INT.parse(row.get("d_return_orderline_id")))
                        .lotId(ParseHelper.INT.parse(row.get("d_lot_id")))
                        .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                        .lotCode(ParseHelper.STRING.parse(row.get("lot_code")))
                        .expirationDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("expiry_date"))))
                        .build();
                data.add(dto);
            }


            return data;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        ReturnOrder returnOrder = returnOrderRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException(
                                messageSource.getMessage("return.order.not.found", null, LocaleContextHolder.getLocale())
                        )
                );

        GlobalReponse response = new GlobalReponse();

        if(returnOrder.getOrderStatus().equals("DRA") ){  //Draft
            returnOrder.setOrderStatus("VOD"); //Chuyen trang thai huy
            returnOrderRepository.save(returnOrder);
        }else {
            throw new RuntimeException(
                    messageSource.getMessage("ro.already.complete", null, LocaleContextHolder.getLocale())
            );
        }
        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }


    @Override
    public GlobalReponsePagination findPosOrdersRetail(PosOrderListQueryRequest request) {

        Parameter parameter = new Parameter();
        parameter.add("order_status", request.getOrderStatus(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("document_no", request.getDocumentNo(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("customer_code", request.getCustomerKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("customer_name", request.getCustomerKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("phone1", request.getCustomerKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.END);
        parameter.add("remain_qty", BigDecimal.ZERO, Param.Logical.GREATER_THAN, Param.Relational.AND, Param.NONE);
        parameter.add("order_date",Param.getBetweenParam(request.getDateFrom(),request.getDateTo()),Param.Logical.BETWEEN,Param.Relational.NONE,Param.NONE);
        request.setSortBy("order_date");
        ResultSet rs = queryEngine.getRecords( "d_ro_pos_order_retail_v",
                parameter, request);

        List<PosOrderRetailDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                PosOrderRetailDto dto = PosOrderRetailDto.builder()
                        .id(rs.getInt("d_pos_order_id"))
                        .documentNo(rs.getString("document_no"))
                        .billNo(rs.getString("document_no"))
                        .orderDate(rs.getObject("order_date") != null
                                ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                : null)
                        .description(rs.getString("description"))
                        .shiftControlId(rs.getInt("d_shift_control_id"))
                        .orderStatus(rs.getString("order_status"))
                        .orderStatusName(rs.getString("order_status_name"))
                        .org(OrgDto.builder()
                                .id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .address(rs.getString("address"))
                                .build())
                        .posTerminal(PosTerminalDto.builder()
                                .id(rs.getInt("d_pos_terminal_id"))
                                .name(rs.getString("terminal_name"))
                                .printerProductId(rs.getString("printer_product_id"))
                                .printerVendorId(rs.getString("printer_vendor_id"))
                                .printerName(rs.getString("printer_name"))
                                .printerPageSize(rs.getInt("printer_page_size"))
                                .printerPageSoQty(rs.getInt("printer_page_so_qty"))
                                .printerPageTempQty(rs.getInt("printer_page_temp_qty"))
                                .printerType(rs.getString("printer_type"))
                                .printerPort(rs.getBigDecimal("printer_port"))
                                .printerIp(rs.getString("printer_ip"))
                                .build())
                        .customer(org.common.dbiz.dto.userDto.CustomerDto.builder()
                                .id(rs.getInt("d_customer_id"))
                                .code(rs.getString("customer_code"))
                                .name(rs.getString("customer_name"))
                                .phone1(rs.getString("phone1"))
                                .build())
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .userName(rs.getString("user_name"))
                                .fullName(rs.getString("user_full_name"))
                                .build())
                        .priceList(PricelistDto.builder()
                                .id(rs.getInt("d_pricelist_id"))
                                .name(rs.getString("pricelist_name"))
                                .build())
                        .flatDiscount(rs.getBigDecimal("flat_discount"))
                        .totalLine(rs.getBigDecimal("total_line"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .flatAmount(rs.getBigDecimal("flat_amt"))
                        .receiptOtherAmount(rs.getBigDecimal("receipt_other_amt"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .build();
                data.add(dto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_ro_pos_order_retail_v", parameter, request);
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


    @Override
    public GlobalReponse findPosOrderRetailById(PosOrderReqDto dto) {


        PosOrderRetailDto data = null;

        try {
            if(dto.getIsBillInfo() != null && dto.getIsBillInfo().equals("Y")){
                Parameter parameter = new Parameter();
                parameter.add("d_pos_order_id", dto.getId(), Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);
                ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_ro_pos_order_retail_v",parameter);
                while (rs.next()) {
                    data = PosOrderRetailDto.builder()
                            .id(rs.getInt("d_pos_order_id"))
                            .documentNo(rs.getString("document_no"))
                            .billNo(rs.getString("document_no"))
                            .orderDate(rs.getObject("order_date") != null
                                    ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                    : null)
                            .description(rs.getString("description"))
                            .shiftControlId(rs.getInt("d_shift_control_id"))
                            .orderStatus(rs.getString("order_status"))
                            .orderStatusName(rs.getString("order_status_name"))
                            .org(OrgDto.builder()
                                    .id(rs.getInt("d_org_id"))
                                    .name(rs.getString("org_name"))
                                    .address(rs.getString("address"))
                                    .build())
                            .posTerminal(PosTerminalDto.builder()
                                    .id(rs.getInt("d_pos_terminal_id"))
                                    .name(rs.getString("terminal_name"))
                                    .printerProductId(rs.getString("printer_product_id"))
                                    .printerVendorId(rs.getString("printer_vendor_id"))
                                    .printerName(rs.getString("printer_name"))
                                    .printerPageSize(rs.getInt("printer_page_size"))
                                    .printerPageSoQty(rs.getInt("printer_page_so_qty"))
                                    .printerPageTempQty(rs.getInt("printer_page_temp_qty"))
                                    .printerType(rs.getString("printer_type"))
                                    .printerPort(rs.getBigDecimal("printer_port"))
                                    .printerIp(rs.getString("printer_ip"))
                                    .build())
                            .customer(CustomerDto.builder()
                                    .id(rs.getInt("d_customer_id"))
                                    .name(rs.getString("customer_name"))
                                    .phone1(rs.getString("phone1"))
                                    .build())
                            .user(UserDto.builder()
                                    .userId(rs.getInt("d_user_id"))
                                    .userName(rs.getString("user_name"))
                                    .fullName(rs.getString("user_full_name"))
                                    .build())
                            .priceList(PricelistDto.builder()
                                    .id(rs.getInt("d_pricelist_id"))
                                    .name(rs.getString("pricelist_name"))
                                    .build())
                            .flatDiscount(rs.getBigDecimal("flat_discount"))
                            .totalLine(rs.getBigDecimal("total_line"))
                            .totalAmount(rs.getBigDecimal("total_amount"))
                            .flatAmount(rs.getBigDecimal("flat_amt"))
                            .receiptOtherAmount(rs.getBigDecimal("receipt_other_amt"))
                            .taxAmount(rs.getBigDecimal("tax_amount"))
                            .build();
                    //Total QTY Order Line
                    String sqlQtySum = "SELECT round(sum(qty),2) as sum_tax_amount FROM pos.d_pos_orderline WHERE d_tenant_id = :tenantId " +
                            " AND d_pos_order_id = :orderId ";

                    BigDecimal sumQtyAmount = (BigDecimal) entityManager.createNativeQuery(sqlQtySum)
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("orderId", dto.getId())
                            .getSingleResult();

                    data.setTotalQty(sumQtyAmount == null ? BigDecimal.ZERO : sumQtyAmount);
                }
            }else{
                data = PosOrderRetailDto.builder().build();
            }

            if(data != null) {
                data.setReceiptOther(getPosReceiptOther(dto.getId()));
                data.setPosOrderLine(getPosOrderLine(dto));
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    public List<PosReceiptOtherDto> getPosReceiptOther(Integer posOrderId) {
        try {
            String sql = "SELECT " +
                    "d_receipt_other_id, " +
                    "receipt_name, " +
                    "code, " +
                    "amount_or_percent " +
                    " FROM pos.d_pos_receipt_other_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId AND is_cal = 'Y' " +
                    " GROUP BY d_receipt_other_id, receipt_name, code, amount_or_percent";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", posOrderId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosReceiptOtherDto> posReceiptOtherList = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosReceiptOtherDto posReceiptOtherDto = PosReceiptOtherDto.builder()
                        .receiptName(ParseHelper.STRING.parse(row.get("receipt_name")))
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .amountOrPercent(ParseHelper.BIGDECIMAL.parse(row.get("amount_or_percent")))
                        .build();
                posReceiptOtherList.add(posReceiptOtherDto);
            }
            return posReceiptOtherList;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PosOrderLineResDto> getPosOrderLine(PosOrderReqDto dto) {
        try {
            String sql = "SELECT " +
                    "d_pos_order_id , d_pos_orderline_id, d_product_id, product_code, " +
                    "product_name, " +
                    "product_wtax_name, " +
                    "salesprice,qty," +
                    "price_discount, " +
                    "discount_percent, " +
                    "discount_amount, " +
                    "tax_amount, " +
                    "linenet_amt," +
                    "grand_total, d_tax_id, tax_name, tax_rate,d_uom_id,uom_name,uom_code,d_image_id, image_url,remain_qty " +
                    " FROM pos.d_ro_pos_orderline_retail_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId and remain_qty > 0";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosOrderLineResDto> posOrderLineRespDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosOrderLineResDto posOrderLineRespDto1 = PosOrderLineResDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_orderline_id")))
                        .product(org.common.dbiz.dto.productDto.ProductDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_product_id")))
                                .code(ParseHelper.STRING.parse(row.get("product_code")))
                                .name(ParseHelper.STRING.parse(row.get("product_name")))
                                .nameWithTax(ParseHelper.STRING.parse(row.get("product_wtax_name")))
                                .uom(UomDto.builder()
                                        .id(ParseHelper.INT.parse(row.get("d_uom_id")))
                                        .name(ParseHelper.STRING.parse(row.get("uom_name")))
                                        .code(ParseHelper.STRING.parse(row.get("uom_code")))
                                        .build())
                                .image(ImageDto.builder()
                                        .id(ParseHelper.INT.parse(row.get("d_image_id")))
                                        .imageUrl(ParseHelper.STRING.parse(row.get("image_url")))
                                        .build())
                                .build())
                        .tax(TaxDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_tax_id")))
                                .name(ParseHelper.STRING.parse(row.get("tax_name")))
                                .taxRate(ParseHelper.BIGDECIMAL.parse(row.get("tax_rate")))
                                .build())
                        .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .remainQty(ParseHelper.BIGDECIMAL.parse(row.get("remain_qty")))
                        .salePrice(ParseHelper.BIGDECIMAL.parse(row.get("salesprice")))
                        .priceDiscount(ParseHelper.BIGDECIMAL.parse(row.get("price_discount")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .discountAmount(ParseHelper.BIGDECIMAL.parse(row.get("discount_amount")))
                        .discountPercent(ParseHelper.BIGDECIMAL.parse(row.get("discount_percent")))
                        .lineNetAmount(ParseHelper.BIGDECIMAL.parse(row.get("linenet_amt")))
                        .grandTotal(ParseHelper.BIGDECIMAL.parse(row.get("grand_total")))
                        .lots(getPosLOtDto(ParseHelper.INT.parse(row.get("d_pos_orderline_id"))))
                        .build();
                posOrderLineRespDto.add(posOrderLineRespDto1);
            }

            return posOrderLineRespDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PosLotDto> getPosLOtDto(Integer posOrderLineId) {

        try {
            String sql = "SELECT " +
                    " d_pos_lot_id," +
                    " d_pos_order_id, " +
                    " d_pos_orderline_id, " +
                    " d_lot_id ," +
                    " qty, " +
                    " is_active, " +
                    " lot_code, " +
                    " expiry_date " +
                    " FROM pos.d_pos_lot_v WHERE 1 = 1 " +
                    " AND d_pos_orderline_id = :posOrderLineId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("posOrderLineId", posOrderLineId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosLotDto> data = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosLotDto dto = PosLotDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_lot_id")))
                        .posOrderId(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                        .posOrderLineId(ParseHelper.INT.parse(row.get("d_pos_orderline_id")))
                        .lotId(ParseHelper.INT.parse(row.get("d_lot_id")))
//                        .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .qty(BigDecimal.ZERO)
                        .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                        .lotCode(ParseHelper.STRING.parse(row.get("lot_code")))
                        .expirationDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("expiry_date"))))
                        .build();
                data.add(dto);
            }


            return data;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    @Override
    public GlobalReponsePagination findPOAll(POHeaderVRequest request) {
        log.info("*** PosOrder List, service; fetch all PosOrder *");

        Parameter parameter = new Parameter();
        parameter.add("order_status", request.getOrderStatus(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("documentno", request.getDocumentNo(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("vendorcode", request.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("vendorname", request.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("vendorphone", request.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.END);
        parameter.add("remain_qty", BigDecimal.ZERO, Param.Logical.GREATER_THAN, Param.Relational.AND, Param.NONE);
        parameter.add("order_date",Param.getBetweenParam(request.getDateFrom(),request.getDateTo()),Param.Logical.BETWEEN,Param.Relational.NONE,Param.NONE);
        request.setSortBy("order_date");
        ResultSet rs = queryEngine.getRecords( "d_ro_po_header_v",
                parameter, request);

        List<POHeaderVDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                POHeaderVDto dto = POHeaderVDto.builder()
                        .id(rs.getInt("d_purchase_order_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .documentNo(rs.getString("documentno"))
                        .orderDate(rs.getObject("order_date") != null
                                ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                : null)

                        .orderStatus(rs.getString("order_status"))
                        .nameReference(rs.getString("name_reference"))
                        .totalQty(rs.getBigDecimal("totalqty"))
                        .totalProduct(rs.getBigDecimal("totalproduct"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .isActive(rs.getString("is_active"))
                        .vendorDto(VendorPOVDto.builder()
                                .vendorId(rs.getInt("d_vendor_id"))
                                .vendorCode(rs.getString("vendorcode"))
                                .vendorName(rs.getString("vendorname"))
                                .vendorPhone(rs.getString("vendorphone"))
                                .vendorPaid(rs.getBigDecimal("vendorpaid"))
                                .vendorDebt(rs.getBigDecimal("vendordebt"))
                                .build())
                        .userDto(UserPOVDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .fullName(rs.getString("full_name"))
                                .build())
                        .warehouseDto(WarehousePOVDto.builder()
                                .warehouseId(rs.getInt("d_warehouse_id"))
                                .warehouseName(rs.getString("warehouse_name"))
                                .build())
                        .build();

                data.add(dto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_ro_po_header_v", parameter, request);
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

    @Override
    public GlobalReponse findPODetail(PODetailVRequest request) {
        log.info("*** Purchase Order Detail List, service; fetch all Purchase Order Detail *");

        Parameter parameter = new Parameter();
        parameter.add("d_purchase_order_id",
                request.getPurchaseOrderId(),
                Param.Logical.EQUAL,
                Param.Relational.AND,
                Param.NONE);
        parameter.add("remain_qty", BigDecimal.ZERO, Param.Logical.GREATER_THAN, Param.Relational.NONE,
                Param.NONE );
        ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_ro_po_header_v",
                parameter);

        POHeaderVDto dto = null;
        try {
            while (rs.next()) {
                dto = POHeaderVDto.builder()
                        .id(rs.getInt("d_purchase_order_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .documentNo(rs.getString("documentno"))
                        .orderDate(rs.getObject("order_date") != null
                                ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                : null)
                        .orderStatus(rs.getString("order_status"))
                        .nameReference(rs.getString("name_reference"))
                        .totalQty(rs.getBigDecimal("totalqty"))
                        .totalProduct(rs.getBigDecimal("totalproduct"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .isActive(rs.getString("is_active"))
                        .vendorDto(VendorPOVDto.builder()
                                .vendorId(rs.getInt("d_vendor_id"))
                                .vendorCode(rs.getString("vendorcode"))
                                .vendorName(rs.getString("vendorname"))
                                .vendorPhone(rs.getString("vendorphone"))
                                .vendorPaid(rs.getBigDecimal("vendorpaid"))
                                .vendorDebt(rs.getBigDecimal("vendordebt"))
                                .build())
                        .userDto(UserPOVDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .fullName(rs.getString("full_name"))
                                .build())
                        .warehouseDto(WarehousePOVDto.builder()
                                .warehouseId(rs.getInt("d_warehouse_id"))
                                .warehouseName(rs.getString("warehouse_name"))
                                .build())
                        .build();
            }

            if(dto != null){
                dto.setLines(getPODetail(dto.getId()));
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .data(dto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }


    public List<PODetailVDto> getPODetail(Integer purchaseOrderId){


        Parameter parameter = new Parameter();
        parameter.add("d_purchase_order_id",
                purchaseOrderId,
                Param.Logical.EQUAL,
                Param.Relational.AND,
                Param.NONE);
        parameter.add("is_active",
                "Y",
                Param.Logical.EQUAL,
                Param.Relational.AND,
                Param.NONE);
        parameter.add("remain_qty",
                BigDecimal.ZERO,
                Param.Logical.GREATER_THAN,
                Param.Relational.NONE,
                Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_ro_po_detail_v",
                parameter);

        List<PODetailVDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                PODetailVDto dto = PODetailVDto.builder()
                        .id(rs.getInt("d_purchase_orderline_id"))
                        .qty(rs.getBigDecimal("qty"))
                        .priceEntered(rs.getBigDecimal("priceentered"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .priceDiscount(rs.getBigDecimal("price_discount"))
                        .totalDiscountAmount(rs.getBigDecimal("total_discount_amount"))
                        .description(rs.getString("description"))
                        .remainQty(rs.getBigDecimal("remain_qty"))
                        .isActive(rs.getString("is_active"))
                        .productDto(ProductPOVDto.builder()
                                .productId(rs.getInt("d_product_id"))
                                .productCode(rs.getString("product_code"))
                                .productName(rs.getString("product_name"))
                                .build())
                        .uom(UomDto.builder()
                                .id(rs.getInt("d_uom_id"))
                                .code(rs.getString("uom_code"))
                                .name(rs.getString("uom_name"))
                                .build())
                        .tax(TaxDto.builder()
                                .id(rs.getInt("d_tax_id"))
                                .taxRate(rs.getBigDecimal("tax_rate"))
                                .name("tax_name")
                                .build())
                        .lot(PosLotDto.builder()
                                .lotId(rs.getInt("d_lot_id"))
                                .lotCode(rs.getString("lot_code"))
                                .expirationDate(rs.getString("expiry_date"))
                                .build())
                        .build();
                data.add(dto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
        return data;
    }
}
