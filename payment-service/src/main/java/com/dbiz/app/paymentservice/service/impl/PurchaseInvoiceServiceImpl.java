package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.*;
import com.dbiz.app.paymentservice.repository.PurchaseInvoiceLineDetailRepository;
import com.dbiz.app.paymentservice.repository.PurchaseInvoiceRepository;
import com.dbiz.app.paymentservice.repository.PurchaseInvoicelineRepository;
import com.dbiz.app.paymentservice.service.PaymentService;
import com.dbiz.app.paymentservice.service.PurchaseInvoiceService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.dto.orderDto.PurchaseOrderLineDto;
import org.common.dbiz.dto.orderDto.dtoView.*;
import org.common.dbiz.dto.orderDto.response.UserDto;
import org.common.dbiz.dto.paymentDto.*;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.common.dbiz.request.paymentRequest.PurchaseInvoiceQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final PurchaseInvoicelineRepository purchaseInvoicelineRepository;
    private final PurchaseInvoiceLineDetailRepository purchaseInvoiceLineDetailRepository;

    private final PaymentService paymentService;
    private final RestTemplate restTemplate;
    private final EntityManager entityManager;
    private final MessageSource messageSource;
    private final QueryEngine queryEngine;
    private final ModelMapper modelMapper;

    @Override
    public GlobalReponsePagination findAll(Object queryRequest) {
        PurchaseInvoiceQueryRequest req = (PurchaseInvoiceQueryRequest) queryRequest;

        Parameter parameter = new Parameter();
        parameter.add("d_purchase_invoice_id", req.getId(), Param.Logical.EQUAL, Param.Relational.AND,
                Param.NONE );
        parameter.add("document_no", req.getDocumentNo(), Param.Logical.LIKE, Param.Relational.AND,
                Param.NONE );
        parameter.add("invoice_status", req.getInvoiceStatus(), Param.Logical.EQUAL, Param.Relational.AND,
                Param.NONE );
        parameter.add("date_invoiced", Param.getBetweenParam(req.getDateFrom(), req.getDateTo()), Param.Logical.BETWEEN, Param.Relational.AND,
                Param.NONE );
        parameter.add("vendor_name", req.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.OR,
                Param.START );
        parameter.add("vendor_code", req.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.NONE,
                Param.END );

        ResultSet rs = queryEngine.getRecords( "d_purchase_invoice_v",
                parameter, req);

        List<PurchaseInvoiceViewDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                PurchaseInvoiceViewDto purchaseInvoiceViewDto = PurchaseInvoiceViewDto.builder()
                        .dateInvoiced(rs.getString("date_invoiced"))
                        .documentNo(rs.getString("document_no"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .invoiceStatus(rs.getString("invoice_status"))
                        .invoiceForm(rs.getString("invoice_form"))
                        .invoiceSign(rs.getString("invoice_sign"))
                        .invoiceNo(rs.getString("invoice_no"))
                        .searchCode(rs.getString("search_code"))
                        .searchLink(rs.getString("search_link"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .discountType(rs.getString("discount_type"))
                        .issuedDate(rs.getString("issued_date"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .vendor(VendorDto.builder()
                                .id(rs.getInt("d_vendor_id"))
                                .taxCode(rs.getString("tax_code"))
                                .name(rs.getString("vendor_name"))
                                .address1(rs.getString("address"))
                                .build())
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .fullName(rs.getString("full_name"))
                                .build())
                       // .invoiceLines(getPurchaseInvoiceLine(rs.getInt("d_purchase_invoice_id")))
                        .build();
                purchaseInvoiceViewDto.setId(rs.getInt("d_purchase_invoice_id"));
                data.add(purchaseInvoiceViewDto);
            }
        }catch(Exception e){
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_purchase_invoice_v", parameter, req);
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
    public GlobalReponse save(Object Dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        PurchaseInvoiceDto purchaseInvoiceDto = (PurchaseInvoiceDto) Dto;
//        //Tinh tien giam gia
//        if(purchaseInvoiceDto.getDiscountAmount() != null && purchaseInvoiceDto.getDiscountPercent() == null){
//            BigDecimal discountAmount = purchaseInvoiceDto.getDiscountAmount(); // ví dụ: 1_000_000
//            BigDecimal totalAmount = purchaseInvoiceDto.getTotalAmount();       // ví dụ: 15_000_000
//            BigDecimal discountPercent = BigDecimal.ZERO;
//            if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) != 0) {
//                discountPercent = discountAmount
//                        .divide(totalAmount, 2, RoundingMode.HALF_UP) // chia lấy 4 chữ số thập phân
//                        .multiply(BigDecimal.valueOf(100));           // nhân 100 để ra phần trăm
//            }
//
//            purchaseInvoiceDto.setDiscountPercent(discountPercent);
//        }else if (purchaseInvoiceDto.getDiscountAmount() == null && purchaseInvoiceDto.getDiscountPercent() != null) {
//            BigDecimal discountPercent = purchaseInvoiceDto.getDiscountPercent();
//            BigDecimal totalAmount = purchaseInvoiceDto.getTotalAmount();
//            BigDecimal discountAmount = BigDecimal.ZERO;
//
//            if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) != 0) {
//                discountAmount = totalAmount
//                        .multiply(discountPercent)
//                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
//            }
//
//            purchaseInvoiceDto.setDiscountAmount(discountAmount);
//        }

        log.info("*** Invoice service; save Invoice ***");
        GlobalReponse response = new GlobalReponse();

        //save info Purchase Invoice
        PurchaseInvoice purchaseInvoice;
        if(purchaseInvoiceDto.getId() != null){ // case update
            purchaseInvoice = purchaseInvoiceRepository.findById(purchaseInvoiceDto.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("purchase_invoice.not_found", null, LocaleContextHolder.getLocale())));
            //Cap nhat hoa don
            if(purchaseInvoiceDto.getDocumentNo() != null){
                if(purchaseInvoiceRepository.existsByDocumentNo(purchaseInvoiceDto.getDocumentNo())){
                    throw new PosException(messageSource.getMessage("document.no.exists", null, LocaleContextHolder.getLocale()));
                }
            }

            modelMapper.map(purchaseInvoiceDto, purchaseInvoice);

            purchaseInvoice = purchaseInvoiceRepository.save(purchaseInvoice);

            //save info purchase order
            if(purchaseInvoiceDto.getPurchaseOrders() != null ) {
                purchaseInvoicelineRepository.updateIsActiveByPurchaseInvoiceId(purchaseInvoice.getId(), "N");
                for (PurchaseInvoiceLineDto item : purchaseInvoiceDto.getPurchaseOrders()) {
                    PurchaseInvoiceLine line =  purchaseInvoicelineRepository.findByPurchaseOrderIdAndPurchaseInvoiceId(item.getPurchaseOrderId(), purchaseInvoice.getId());

                    if(line != null) {
                        modelMapper.map(item, line);
                        line.setPurchaseInvoiceId(purchaseInvoice.getId());
                        line.setIsActive("Y");
                    }else{
                        line = modelMapper.map(item, PurchaseInvoiceLine.class);
                        line.setPurchaseInvoiceId(purchaseInvoice.getId());
                        line.setOrgId(purchaseInvoice.getOrgId());
                        line.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    }

                    purchaseInvoicelineRepository.save(line);
                    item.setId(line.getId());
                }
            }

            //save info line in purchase invoice
            if(purchaseInvoiceDto.getLines() != null) {
                purchaseInvoiceLineDetailRepository.updateIsActiveByPurchaseInvoiceId(purchaseInvoice.getId(), "N");
                for (PurchaseInvoiceLineDetailDto item : purchaseInvoiceDto.getLines()) {
                    if (item.getProductId() == null && item.getProductName() != null) {
                        item.setProductId(getProductByName(item.getProductName()).getId());
                        item.setProductName(getProductByName(item.getProductName()).getName());
                    }
                    PurchaseInvoiceLineDetail line;

                    if(item.getId() != null){//Dong nay da ton tai truoc do
                        line  =  purchaseInvoiceLineDetailRepository.findById(item.getId()).orElseThrow();
                        modelMapper.map(item, line);
                        line.setPurchaseInvoiceId(purchaseInvoice.getId());
                        line.setIsActive("Y");
                    }else{
                        line = modelMapper.map(item, PurchaseInvoiceLineDetail.class);
                        line.setPurchaseInvoiceId(purchaseInvoice.getId());
                        line.setOrgId(purchaseInvoice.getOrgId());
                        line.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    }

                    purchaseInvoiceLineDetailRepository.save(line);
                }
            }

        }else{ // Case theem
            if(purchaseInvoiceDto.getDocumentNo() != null){//Check tồn tại document No
                if(purchaseInvoiceRepository.existsByDocumentNo(purchaseInvoiceDto.getDocumentNo())){
                    throw new PosException(messageSource.getMessage("document.no.exists", null, LocaleContextHolder.getLocale()));
                }
            }

            purchaseInvoice = modelMapper.map(purchaseInvoiceDto, PurchaseInvoice.class);

            if (purchaseInvoice.getDocumentNo() == null){
                Integer maxId = purchaseInvoiceRepository.getMaxId();
                String docNo = DocHelper.generateDocNo("AP",maxId + 1) ;
                purchaseInvoice.setDocumentNo(docNo);
            }

            GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE + "/"
                    + AppConstant.DocTypeCode.AP_INVOICE, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
            Integer posTerminalDto = modelMapper.map(exRes.getData(), Integer.class);
            purchaseInvoice.setDoctypeId(posTerminalDto);
            purchaseInvoice.setCurrencyId(13);
            purchaseInvoice.setUserId(purchaseInvoiceDto.getUserId());

            purchaseInvoice.setDateInvoiced(DateHelper.toInstantNowUTC());
            purchaseInvoice.setInvoiceStatus(AppConstant.DOC_STATUS_COMPLETED);
            purchaseInvoice.setTenantId(AuditContext.getAuditInfo().getTenantId());

            purchaseInvoice = purchaseInvoiceRepository.save(purchaseInvoice);
            purchaseInvoiceDto.setId(purchaseInvoice.getId());

            //save info purchase order
            if(purchaseInvoiceDto.getPurchaseOrders() != null && !purchaseInvoiceDto.getPurchaseOrders().isEmpty()) {
                for (PurchaseInvoiceLineDto item : purchaseInvoiceDto.getPurchaseOrders()) {
                    PurchaseInvoiceLine purchaseInvoiceLine = modelMapper.map(item, PurchaseInvoiceLine.class);
                    purchaseInvoiceLine.setPurchaseOrderId(item.getPurchaseOrderId());
                    purchaseInvoiceLine.setPurchaseInvoiceId(purchaseInvoice.getId());
                    purchaseInvoiceLine.setOrgId(purchaseInvoice.getOrgId());
                    purchaseInvoiceLine.setTenantId(AuditContext.getAuditInfo().getTenantId());

                    purchaseInvoicelineRepository.save(purchaseInvoiceLine);
                    item.setId(purchaseInvoiceLine.getId());
                }
            }

            //save info line in purchase invoice
            if(purchaseInvoiceDto.getLines() != null && !purchaseInvoiceDto.getLines().isEmpty()) {
                for (PurchaseInvoiceLineDetailDto item : purchaseInvoiceDto.getLines()) {
                    if (item.getProductId() == null && item.getProductName() != null) {
                        item.setProductId(getProductByName(item.getProductName()).getId());
                        item.setProductName(getProductByName(item.getProductName()).getName());
                    }
                    PurchaseInvoiceLineDetail purchaseInvoiceLineDetail = modelMapper.map(item, PurchaseInvoiceLineDetail.class);
                    purchaseInvoiceLineDetail.setPurchaseInvoiceId(purchaseInvoice.getId());
                    purchaseInvoiceLineDetail.setOrgId(purchaseInvoice.getOrgId());
                    purchaseInvoiceLineDetail.setTenantId(AuditContext.getAuditInfo().getTenantId());

                    purchaseInvoiceLineDetailRepository.save(purchaseInvoiceLineDetail);
                }
            }

        }

        // Xử lý phieeus chi
//        if(purchaseInvoiceDto.getPayment() != null){
//            StringBuilder sql = new StringBuilder("select count(payment_amount) from d_payment dp where d_purchase_invoice_id = :purchaseInvoiceId");
//
//            Query query = this.entityManager.createNativeQuery(sql.toString());
//            query = query.setParameter("purchaseInvoiceId", purchaseInvoice.getId());
//
//            Long totalPaid = ((Number) query.getSingleResult()).longValue();
//
//            // hoa don chua tra het
//            if(purchaseInvoice.getTotalAmount().compareTo(BigDecimal.valueOf(totalPaid)) > 0){
//                PaymentDto paymentDto = purchaseInvoiceDto.getPayment();
//
////                paymentDto.setOrgId(purchaseInvoice.getOrgId());
////                paymentDto.setTenantId(purchaseInvoice.getTenantId());
//                paymentDto.setPurchaseInvoiceId(purchaseInvoice.getId());
//                paymentDto.setDocType("ARP");
//                paymentDto.setResponsibleUserId(purchaseInvoice.getVendorId());
//                paymentDto.setPaymentStatus(AppConstant.DOC_STATUS_COMPLETED);
//                paymentDto.setPaymentRule(paymentDto.getPaymentMethod());
//                paymentDto.setUserId(purchaseInvoiceDto.getUserId());
//                if(paymentDto.getUserGroup() == null){
//                    paymentDto.setUserGroup("VEN");
//                }
//
//                GlobalReponse globalReponse =  paymentService.save(paymentDto);
//                PaymentDto payment = (PaymentDto) globalReponse.getData();
//
//                log.info("Payment: {}", payment);
//            }else{
//                throw new PosException("Hoas don da tra het tien");
//            }
//        }
//
//        purchaseInvoiceDto = modelMapper.map(purchaseInvoice, PurchaseInvoiceDto.class);

        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .data(purchaseInvoiceDto)
                .build();
    }

    private ProductDto getProductByName(String productName) {

        String sql = "SELECT " +
                "d_product_id, " +
                "name " +
                "FROM pos.d_product " +
                "WHERE unaccent(lower(name)) like unaccent(lower(CONCAT('%', :name, '%'))) ";

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("name", productName)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        if (results.isEmpty()) {
            throw new PosException(messageSource.getMessage("product.name.not_found", new Object[]{productName}, LocaleContextHolder.getLocale()));
        }
        Map<String, Object> firstRow = results.get(0);

        return ProductDto.builder()
                .id(ParseHelper.INT.parse(firstRow.get("d_product_id")))
                .name(ParseHelper.STRING.parse(firstRow.get("name")))
                .build();
    }


    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        Parameter parameter = new Parameter();
        parameter.add("d_purchase_invoice_id", id, Param.Logical.EQUAL, Param.Relational.AND,
                Param.NONE );

        ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_purchase_invoice_v",
                parameter);
        try {
            while (rs.next()) {
                PurchaseInvoiceViewDto purchaseInvoiceViewDto = PurchaseInvoiceViewDto.builder()
                        .dateInvoiced(rs.getString("date_invoiced"))
                        .documentNo(rs.getString("document_no"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .invoiceStatus(rs.getString("invoice_status"))
                        .invoiceForm(rs.getString("invoice_form"))
                        .invoiceSign(rs.getString("invoice_sign"))
                        .invoiceNo(rs.getString("invoice_no"))
                        .searchCode(rs.getString("search_code"))
                        .searchLink(rs.getString("search_link"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .discountType(rs.getString("discount_type"))
                        .issuedDate(rs.getString("issued_date"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .paid(rs.getBigDecimal("paid"))
                        .description(rs.getString("description"))
                        .vendor(VendorDto.builder()
                                .id(rs.getInt("d_vendor_id"))
                                .code(rs.getString("tax_code"))
                                .name(rs.getString("vendor_name"))
                                .address1(rs.getString("address"))
                                .taxCode(rs.getString("tax_code"))
                                .build())
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .fullName(rs.getString("full_name"))
                                .build())
                        .invoiceLines(getPurchaseInvoiceLine(rs.getInt("d_purchase_invoice_id")))
                        .invoiceLineDetails(getPurchaseInvoiceLineDetail(rs.getInt("d_purchase_invoice_id")))
                        .paymentDtos(getPayment(rs.getInt("d_purchase_invoice_id")))
                        .build();
                purchaseInvoiceViewDto.setId(rs.getInt("d_purchase_invoice_id"));
                return GlobalReponse.builder()
                        .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .data(purchaseInvoiceViewDto)
                        .build();
            }
        }catch(Exception e){
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
        throw new PosException("Not found");
    }

    private List<PaymentViewDto> getPayment(Integer purchaseInvoiceId){
        StringBuilder sql = new StringBuilder("SELECT* FROM d_payment_v_2 where d_purchase_invoice_id =:purchaseInvoiceId ");

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                .setParameter("purchaseInvoiceId", purchaseInvoiceId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PaymentViewDto> paymentViewDtos = new ArrayList<>();
        for ( Map<String, Object> row : results ) {
            PaymentViewDto payment = PaymentViewDto.builder()
                    .purchaseInvoiceId(ParseHelper.INT.parse(row.get("d_purchase_invoice_id")))
                    .paymentStatus(ParseHelper.STRING.parse(row.get("payment_status")))
                    .paymentAmount(ParseHelper.BIGDECIMAL.parse(row.get("payment_amount")))
                    .documentNo(ParseHelper.STRING.parse(row.get("document_no")))
                    .paymentRule(ParseHelper.STRING.parse(row.get("payment_method")))
                    .paymentDate(ParseHelper.STRING.parse(row.get("payment_date")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .fullName(ParseHelper.STRING.parse(row.get("full_name")))
                    .build();
            payment.setId(ParseHelper.INT.parse(row.get("d_payment_id")));

            paymentViewDtos.add(payment);
        }
        return paymentViewDtos;
    }

    private List<PurchaseInvoiceLineDto> getPurchaseInvoiceLine(Integer purchaseInvoiceId){
        Parameter parameter = new Parameter();
        parameter.add("d_purchase_invoice_id", purchaseInvoiceId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE );

        ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_purchase_invoiceline_v",
                parameter);

        List<PurchaseInvoiceLineDto> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PurchaseInvoiceLineDto lineDetail = PurchaseInvoiceLineDto.builder()
                        .purchaseOrderId(rs.getInt("d_purchase_order_id"))
                        .documentNo(rs.getString("documentno"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .description(rs.getString("description"))
                        .lines(getPODetail(rs.getInt("d_purchase_order_id")))
                        .build();
                lineDetail.setId(rs.getInt("d_purchase_invoiceline_id"));        //Cho case cập nhật
                list.add(lineDetail);
            }

            return list;
        }catch(Exception e){
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

    }

    private List<PurchaseInvoiceLineDetailVDto> getPurchaseInvoiceLineDetail(Integer purchaseInvoiceId){
        Parameter parameter = new Parameter();
        parameter.add("d_purchase_invoice_id", purchaseInvoiceId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE );

        ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_purchase_invoiceline_detail_v",
                parameter);

        List<PurchaseInvoiceLineDetailVDto> invoiceLineDetails = new ArrayList<>();

        try {
            while (rs.next()) {
                PurchaseInvoiceLineDetailVDto lineDetail = PurchaseInvoiceLineDetailVDto.builder()
                        .purchaseInvoiceId(rs.getInt("d_purchase_invoice_id"))
                        .qty(rs.getInt("qty"))
                        .priceEntered(rs.getBigDecimal("price_entered"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .priceDiscount(rs.getBigDecimal("price_discount"))
                        .discountType(rs.getString("discount_type"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .totalDiscountAmount(rs.getBigDecimal("total_discount_amount"))
                        .description(rs.getString("description"))
                        .productId(rs.getInt("d_product_id"))
                        .productName(rs.getString("product_name"))
                        .productCode(rs.getString("product_code"))
                        .taxId(rs.getInt("d_tax_id"))
                        .taxRate(rs.getBigDecimal("tax_rate"))
                        .taxName(rs.getString("tax_name"))
                        .uomId(rs.getInt("d_uom_id"))
                        .uomName(rs.getString("uom_name"))
//                        .product(ProductPOVDto.builder()
//                                .productId(rs.getInt("d_product_id"))
//                                .productName(rs.getString("product_name"))
//                                .productCode(rs.getString("product_code"))
//                                .build())
//                        .tax(TaxDto.builder()
//                                .id(rs.getInt("d_tax_id"))
//                                .taxRate(rs.getBigDecimal("tax_rate"))
//                                .name(rs.getString("tax_name"))
//                                .build())
//                        .uom(UomDto.builder()
//                                .id(rs.getInt("d_uom_id"))
//                                .code(rs.getString("uom_code"))
//                                .name(rs.getString("uom_name"))
//                                .build())
                        .build();
                lineDetail.setId(rs.getInt("d_purchase_invoiceline_detail_id"));
                invoiceLineDetails.add(lineDetail);
            }

            return invoiceLineDetails;
        }catch(Exception e){
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    @Override
    public GlobalReponsePagination getPO(POHeaderVRequest req) {
        log.info("*** Purchase Order Detail List, service; fetch all Purchase Order Detail *");

        List<POHeaderVDto> dto = null;

        int pageSize = req.getPageSize();

        if(req.getPurchaseInvoiceId() != null ){ //Chi co page 0 moi lay
            log.info("Lay cac phieu nhap cua hoa don {}", req.getPurchaseInvoiceId());
            dto = getPurchaseOrderByPurchaseInvoiceId(req);
            log.info("PO {}", dto);
            req.setPageSize(pageSize - dto.size()); //
        }

        Parameter parameter = new Parameter();

        parameter.add("order_date",
                Param.getBetweenParam(req.getDateFrom(), req.getDateTo()),
                Param.Logical.BETWEEN,
                Param.Relational.AND,
                Param.NONE);
        parameter.add("documentno", req.getDocumentNo(), Param.Logical.LIKE,Param.Relational.AND ,Param.NONE);
        parameter.add("vendor_name", req.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.OR,
                Param.START);
        parameter.add("vendor_code", req.getVendorKeyword(), Param.Logical.LIKE, Param.Relational.NONE,
                Param.END);

        if(req.getPageSize() > 0) {
            ResultSet rs = queryEngine.getRecords("d_pi_header_v",
                    parameter, req);

            if (dto == null) {
                dto = new ArrayList<>();
            }

            try {
                while (rs.next()) {
                    POHeaderVDto poHeaderVDto = POHeaderVDto.builder()
                            .id(rs.getInt("d_purchase_order_id"))
                            .orgId(rs.getInt("d_org_id"))
                            .documentNo(rs.getString("documentno"))
                            .orderDate(rs.getObject("order_date") != null
                                    ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                    : null)
                            .orderStatus(rs.getString("order_status"))
                            .discountAmount(rs.getBigDecimal("discount_amount"))
                            .discountPercent(rs.getBigDecimal("discount_percent"))
                            .totalAmount(rs.getBigDecimal("total_amount"))
                            .netAmount(rs.getBigDecimal("net_amount"))
                            .taxAmount(rs.getBigDecimal("tax_amount"))
                            .isActive(rs.getString("is_active"))
                            .vendorDto(VendorPOVDto.builder()
                                    .vendorId(rs.getInt("d_vendor_id"))
                                    .vendorCode(rs.getString("vendor_code"))
                                    .vendorName(rs.getString("vendor_name"))
                                    .vendorPhone(rs.getString("vendor_phone"))
                                    .taxCode(rs.getString("tax_code"))
                                    .address1(rs.getString("address1"))
                                    .build())
                            .lines(getPODetail(rs.getInt("d_purchase_order_id")))
                            .build();
                    dto.add(poHeaderVDto);
                }
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
                throw new PosException(e.getMessage());
            }
        }

        req.setPageSize(pageSize);
        Pagination pagination = queryEngine.getPagination("d_pi_header_v", parameter, req);
        Integer sizePurchaseOrder = 0;
        if(req.getPurchaseInvoiceId() != null){
            sizePurchaseOrder =  getSizePurchaseOrderByPurchaseInvoiceId(req);
        }

        long totalItems = pagination.getTotalCount() + sizePurchaseOrder;

        log.info("Load pagination...");

        return GlobalReponsePagination.builder()
                .data(dto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pageSize)
                .currentPage(req.getPage())
                .totalPages((int) Math.ceil((double) totalItems / pageSize))
                .totalItems(totalItems)
                .build();
    }

    private List<POHeaderVDto> getPurchaseOrderByPurchaseInvoiceId(POHeaderVRequest req){
        StringBuilder sql = new StringBuilder("SELECT dpo.d_purchase_order_id,\n" +
                "    dpo.documentno,\n" +
                "    dpo.d_org_id,\n" +
                "    dpo.d_vendor_id,\n" +
                "    dv.name AS vendor_name,\n" +
                "    dv.code AS vendor_code,\n" +
                "    dv.phone1 AS vendor_phone,\n" +
                "    dv.tax_code,\n" +
                "    dv.address1,\n" +
                "    dpo.order_status,\n" +
                "    dpo.order_date,\n" +
                "    dpo.total_amount,\n" +
                "    dpo.description,\n" +
                "    dpo.discount_percent,\n" +
                "    dpo.discount_amount,\n" +
                "    dpo.net_amount,\n" +
                "    dpo.tax_amount,\n" +
                "    dpo.created,\n" +
                "    dpo.is_active\n" +
                "   FROM pos.d_purchase_order dpo\n" +
                "     LEFT JOIN pos.d_vendor dv ON dpo.d_vendor_id = dv.d_vendor_id\n" +
                "  WHERE dpo.order_status = 'COM' \n" +
                "  AND dpo.is_active = 'Y' \n" +
                "  AND dpo.d_purchase_order_id IN ( \n" +
                "  SELECT dpi.d_purchase_order_id\n" +
                "           FROM pos.d_purchase_invoiceline dpi \n" +
                "           where dpi.d_purchase_invoice_id = :purchaseInvoiceId and dpi.is_active = 'Y' \n" +
                "   ) ");

        if(req.getDateFrom() != null && req.getDateTo() != null) {
            sql.append(" AND ( dpo.order_date BETWEEN :dateFrom AND :dateTo )");
        }

        if(req.getDocumentNo() != null){
            sql.append(" AND ( lower(dpo.documentno) like :documentNo )");
        }

        if(req.getVendorKeyword() != null){
            sql.append(" AND (lower(dv.name) like :keyword or lower(dv.code) like :keyword ) ");
        }

        sql.append(" LIMIT :limit OFFSET :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("purchaseInvoiceId", req.getPurchaseInvoiceId());

        query.setParameter("limit", req.getPageSize());
        query.setParameter("offset", req.getPage()*req.getPageSize());

        if(req.getDateFrom() != null && req.getDateTo() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String fromDateStr = req.getDateFrom() + " 00:00:00";
            String toDateStr = req.getDateTo() + " 23:59:59";

            LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, formatter);
            LocalDateTime toDate = LocalDateTime.parse(toDateStr, formatter);

            query.setParameter("dateFrom", Timestamp.valueOf(fromDate));
            query.setParameter("dateTo", Timestamp.valueOf(toDate));
        }

        if(req.getVendorKeyword() != null){
            query.setParameter("keyword", "%" + req.getVendorKeyword().toLowerCase() + "%");
        }

        if(req.getDocumentNo() != null){
            query.setParameter("documentNo", "%" + req.getDocumentNo().toLowerCase() + "%");
        }

        List<Map<String, Object>> results = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();


        List<POHeaderVDto> dto = new ArrayList<>();

        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);
            POHeaderVDto poHeaderVDto = POHeaderVDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_purchase_order_id")))
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .documentNo(ParseHelper.STRING.parse(row.get("documentno")))
                    .orderDate(ParseHelper.STRING.parse(row.get("order_date")))
                    .orderStatus(ParseHelper.STRING.parse(row.get("order_status")))
                    .discountAmount(ParseHelper.BIGDECIMAL.parse(row.get("discount_amount")))
                    .discountPercent(ParseHelper.BIGDECIMAL.parse(row.get("discount_percent")))
                    .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                    .netAmount(ParseHelper.BIGDECIMAL.parse(row.get("net_amount")))
                    .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                    .vendorDto(VendorPOVDto.builder()
                            .vendorId(ParseHelper.INT.parse(row.get("d_vendor_id")))
                            .vendorCode(ParseHelper.STRING.parse(row.get("vendor_code")))
                            .vendorName(ParseHelper.STRING.parse(row.get("vendor_name")))
                            .vendorPhone(ParseHelper.STRING.parse(row.get("vendor_phone")))
                            .taxCode(ParseHelper.STRING.parse(row.get("tax_code")))
                            .address1(ParseHelper.STRING.parse(row.get("address1")))
                            .build())
                    .lines(getPODetail(ParseHelper.INT.parse(row.get("d_purchase_order_id"))))
                    .build();
            dto.add(poHeaderVDto);
        }

        return dto;
    }

    private Integer getSizePurchaseOrderByPurchaseInvoiceId(POHeaderVRequest req){
        StringBuilder sql = new StringBuilder("SELECT COUNT(1) " +
                "   FROM pos.d_purchase_order dpo\n" +
                "     LEFT JOIN pos.d_vendor dv ON dpo.d_vendor_id = dv.d_vendor_id\n" +
                "  WHERE dpo.order_status = 'COM' \n" +
                "  AND dpo.is_active = 'Y' \n" +
                "  AND dpo.d_purchase_order_id IN ( \n" +
                "  SELECT dpi.d_purchase_order_id\n" +
                "           FROM pos.d_purchase_invoiceline dpi \n" +
                "           where dpi.d_purchase_invoice_id = :purchaseInvoiceId  and dpi.is_active = 'Y' \n" +
                "   ) ");

        if(req.getDateFrom() != null && req.getDateTo() != null){
            sql.append(" AND ( dpo.order_date BETWEEN :dateFrom AND :dateTo )");
        }

        if(req.getDocumentNo() != null){
            sql.append(" AND ( lower(dpo.documentno) like :documentNo )");
        }

        if(req.getVendorKeyword() != null){
            sql.append(" AND (lower(dv.name) like :keyword or lower(dv.code) like :keyword ) ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("purchaseInvoiceId", req.getPurchaseInvoiceId());

        if(req.getDateFrom() != null && req.getDateTo() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String fromDateStr = req.getDateFrom() + " 00:00:00";
            String toDateStr = req.getDateTo() + " 23:59:59";

            LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, formatter);
            LocalDateTime toDate = LocalDateTime.parse(toDateStr, formatter);

            query.setParameter("dateFrom", Timestamp.valueOf(fromDate));
            query.setParameter("dateTo", Timestamp.valueOf(toDate));
        }

        if(req.getVendorKeyword() != null){
            query.setParameter("keyword", "%" + req.getVendorKeyword().toLowerCase() + "%");
        }

        if(req.getDocumentNo() != null){
            query.setParameter("documentNo", "%" + req.getDocumentNo().toLowerCase() + "%");
        }

        Object results = query.getSingleResult();

        return results == null ? 0 : ((Number)results).intValue();
    }

    public List<PODetailVDto> getPODetail(Integer purchareOrderId){
        Parameter parameter = new Parameter();
        parameter.add("d_purchase_order_id", purchareOrderId, Param.Logical.EQUAL, Param.Relational.AND,
                Param.NONE );
        parameter.add("is_active", "Y", Param.Logical.EQUAL, Param.Relational.NONE,
                Param.NONE );


        ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_po_detail_v",
                parameter);

        List<PODetailVDto> poDetailVDtos = new ArrayList<>();
        try {
            while (rs.next()) {
                PODetailVDto poHeaderVDto = PODetailVDto.builder()
                        .id(rs.getInt("d_purchase_orderline_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .purchaseOrderId(rs.getInt("d_purchase_order_id"))
                        .qty(rs.getBigDecimal("qty"))
                        .priceEntered(rs.getBigDecimal("priceentered"))
                        .netAmount(rs.getBigDecimal("net_amount"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .description(rs.getString("description"))
                        .totalDiscountAmount(rs.getBigDecimal("total_discount_amount"))
                        .discountAmount(rs.getBigDecimal("discount_amount"))
                        .discountPercent(rs.getBigDecimal("discount_percent"))
                        .priceDiscount(rs.getBigDecimal("price_discount"))
                        .productDto(ProductPOVDto.builder()
                                .productId(rs.getInt("d_product_id"))
                                .productName(rs.getString("product_name"))
                                .productCode(rs.getString("product_code"))
                                .build())
                        .tax(TaxDto.builder()
                                .id(rs.getInt("d_tax_id"))
                                .taxRate(rs.getBigDecimal("tax_rate"))
                                .name(rs.getString("tax_name"))
                                .build())
                        .uom(UomDto.builder()
                                .id(rs.getInt("d_uom_id"))
                                .code(rs.getString("uom_code"))
                                .name(rs.getString("uom_name"))
                                .build())
                        .build();
                poDetailVDtos.add(poHeaderVDto);
            }
        }catch(Exception e){
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return poDetailVDtos;
    }


}
