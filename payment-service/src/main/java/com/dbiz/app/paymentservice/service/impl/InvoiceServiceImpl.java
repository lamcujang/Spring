package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.*;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosTaxLineDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.TotalPosOrderCostDto;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.paymentservice.repository.*;
import com.dbiz.app.paymentservice.service.CouponService;
import com.dbiz.app.paymentservice.service.InvoiceService;
import com.dbiz.app.paymentservice.service.PaymentService;
import com.dbiz.app.paymentservice.service.VoucherService;
import com.dbiz.app.paymentservice.specification.InvoiceViewSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.dto.orderDto.ListProductDto;
import org.common.dbiz.dto.orderDto.response.ProductDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.*;
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.annotation.Native;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
@Slf4j
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final ModelMapper modelMapper;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceViewRepository invoiceViewRepository;
    private final InvoiceLineRepository invoiceLineRepository;
    private final PaymentService paymentService;
    private final RequestParamsUtils requestParamsUtils;
    private final PosPaymentRepository posPaymentRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final RestTemplate restTemplate;
    private final VoucherService voucherService;
    private final CouponService couponService;
    private final PosPaymentDTRepository posPaymentDTRepository;
    private final MessageSource messageSource;


    @Override
    public GlobalReponsePagination findAll(Object queryRequest) {

        log.info("*** Invoice List, service; fetch all Invoice *");
        InvoiceQueryRequest query = (InvoiceQueryRequest) queryRequest;
        Pageable pageable = requestParamsUtils.getPageRequest(query);
        Specification<InvoiceView> spec = InvoiceViewSpecification.getSpecification(query);
        Page<InvoiceView> entityList = invoiceViewRepository.findAll(spec, pageable);

        List<InvoiceViewDto> listData = new ArrayList<>();
        for (InvoiceView item : entityList.getContent()) {
            InvoiceViewDto invoiceDto = modelMapper.map(item, InvoiceViewDto.class);
            invoiceDto.setDateInvoiced(item.getDateInvoiced() != null ? DateHelper.fromInstantUTC(item.getDateInvoiced()) : null);
            invoiceDto.setIssuedDate(item.getIssuedDate() != null ? DateHelper.fromInstantUTC(item.getIssuedDate()) : null);
            invoiceDto.setAccountingDate(item.getAccountingDate() != null ? DateHelper.fromInstantUTC(item.getAccountingDate()) : null);

            listData.add(invoiceDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("Invoices fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(Object Dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        InvoiceDto invoiceDto = (InvoiceDto) Dto;
        log.info("*** Invoice service; save Invoice ***");
        GlobalReponse response = new GlobalReponse();




        //Invoice
        Invoice invoice = modelMapper.map(invoiceDto, Invoice.class);


        if (invoice.getDocumentNo() == null){
            Integer maxId = invoiceRepository.getMaxId();
            String docNo = DocHelper.generateDocNo("AR",maxId + 1) ;
            invoice.setDocumentNo(docNo);
        }


        GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE + "/"
                + AppConstant.DocTypeCode.AR_INVOICE, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
        Integer posTerminalDto = modelMapper.map(exRes.getData(), Integer.class);
        invoice.setDocTypeId(posTerminalDto);
        invoice.setCurrencyId(13);
        invoice.setUserId(invoiceDto.getUserId());
//        if(invoiceDto.getDateInvoiced() == null){
//            invoice.setDateInvoiced(Instant.now());
//        }else{
//            invoice.setDateInvoiced(DateHelper.toInstantDateAndTime(invoiceDto.getDateInvoiced()));
//        }
        invoice.setDateInvoiced(DateHelper.toInstantNowUTC());
        invoice.setInvoiceStatus(AppConstant.DOC_STATUS_COMPLETED);
        invoice.setTenantId(AuditContext.getAuditInfo().getTenantId());
        invoiceRepository.save(invoice);

        updateCusDebit(invoice.getCustomerId(), invoice.getTotalAmount());

        //InvoiceLine
        List<InvoiceLineDto> invoiceLinesDto = invoiceDto.getInvoiceLines();

        List<ProductDto> listProduct = new ArrayList<>();

        invoiceLinesDto.forEach(invoiceLineDto -> {
            InvoiceLine invoiceLine = modelMapper.map(invoiceLineDto, InvoiceLine.class);
            invoiceLine.setOrgId(invoice.getOrgId());
            invoiceLine.setInvoiceId(invoice.getId());
            invoiceLine.setTenantId(AuditContext.getAuditInfo().getTenantId());
            ProductDto pr = ProductDto.builder()
                    .id(invoiceLineDto.getProductId())
                    .onHand(invoiceLine.getQty().negate()).build();
            listProduct.add(pr);
            invoiceLineRepository.save(invoiceLine);
        });
        boolean isPaid = false;
        if (!(invoiceDto.getPayments() != null && !invoiceDto.getPayments().isEmpty())) {
            invoiceDto.setPayments(new ArrayList<>());
            List<PosPayment> ps = posPaymentRepository.findByPosOrderId(invoiceDto.getPosOrderId());
            if (ps != null && !ps.isEmpty()) {
                for (PosPayment p : ps) {
                    PaymentDto paymentDto = new PaymentDto();
                    paymentDto.setId(p.getId());
                    paymentDto.setPaymentRule(p.getPaymentMethod());
                    paymentDto.setPaymentAmount(p.getTotalAmount());
                    paymentDto.setCode(p.getVoucherCode());
                    invoiceDto.getPayments().add(paymentDto);

                    p.setIsProcessed("Y");
                    posPaymentRepository.save(p);
                }
                isPaid = true;
            }
        }

        if (invoiceDto.getPayments() != null && !invoiceDto.getPayments().isEmpty()) {
            if(!isPaid){
                posPaymentDTRepository.deletePosPaymentDTByPosOrderId(invoiceDto.getPosOrderId(), AuditContext.getAuditInfo().getTenantId());
                //Delete old pos payment
                posPaymentRepository.deletePosPaymentsByPosOrderIdAndTenantId(invoiceDto.getPosOrderId(), AuditContext.getAuditInfo().getTenantId());
            }

            for (PaymentDto p : invoiceDto.getPayments()) {
                PosPayment posPayment = null;
                if (!isPaid) {

                    //PosPayment
                    posPayment = new PosPayment();
                    posPayment.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    posPayment.setOrgId(invoice.getOrgId());
                    posPayment.setPosOrderId(invoiceDto.getPosOrderId());
                    posPayment.setPaymentMethod(p.getPaymentRule());
                    posPayment.setTransactionId(p.getTransactionId());
                    if (p.getCode() != null) {
                        posPayment.setVoucherCode(p.getCode());
                    }
                    posPayment.setTotalAmount(p.getPaymentAmount());
//                posPayment.setTransactionId(p.getT);
                    posPayment.setIsProcessed("Y");
//                posPayment.setNote();
                    posPayment = posPaymentRepository.save(posPayment);

                    //Save Voucher Coupon Payment
                    log.info("Start Payment Rule: {}", p.getPaymentRule());
                    if ((p.getPaymentRule().equals(AppConstant.PaymentRule.VOUCHER) ||
                            p.getPaymentRule().equals(AppConstant.PaymentRule.COUPON))
                            && p.getDetails() != null && !p.getDetails().isEmpty()) {
                        log.info("Mid Payment Rule: {}", p.getPaymentRule());
                        voucherService.saveVoucherCouponPayment(p.getDetails(),
                                invoice.getOrgId(),
                                posPayment.getId(),
                                p.getPaymentRule());
                    }
                }else{
                    posPayment = posPaymentRepository.findById(p.getId()).orElseThrow(() -> new PosException("Pos Payment not found"));
                }

                if (p.getPaymentRule().equals(AppConstant.PaymentRule.FREE)) {
                    continue;
                }

                if (p.getPaymentRule().equals(AppConstant.PaymentRule.DEBT)) {

                    continue;
                }

                log.info("End Payment Rule: {}", p.getPaymentRule());


                //Payment
                PaymentDto paymentDto = new PaymentDto();
                paymentDto.setOrgId(invoice.getOrgId());
                paymentDto.setInvoiceId(invoice.getId());
                paymentDto.setPosOrderId(invoiceDto.getPosOrderId());
                paymentDto.setDocType("ARP");
                paymentDto.setPosTerminalId(invoiceDto.getPosTerminalId());
                if(invoice.getCustomerId() != null){
                    paymentDto.setResponsibleUserId(invoice.getCustomerId());
                    paymentDto.setUserGroup("CUS");
                }else{
                    paymentDto.setResponsibleUserId(invoice.getVendorId());
                    paymentDto.setUserGroup("VEN");
                }

                if(invoiceDto.getOrderNo() != null){
                    paymentDto.setReferenceNo(invoiceDto.getOrderNo());
                    paymentDto.setDescription(messageSource.getMessage("payment.description", null,
                            Locale.lookup(Locale.LanguageRange.parse("vi"), Arrays.asList(Locale.getAvailableLocales()))));
                }

                paymentDto.setPaymentStatus(AppConstant.DOC_STATUS_COMPLETED);
                paymentDto.setPaymentAmount(p.getPaymentAmount());
                paymentDto.setPaymentRule(p.getPaymentRule());
                paymentDto.setPaymentMethod(p.getPaymentRule());
                if(invoiceDto.getPosTerminalId() != null){
                    Integer accountId = null;
                    GlobalReponse serviceDto = restTemplate.exchange(
                            AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_TERMINAL_BY_ID_URL + "/" + invoiceDto.getPosTerminalId()
                            , HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                    PosTerminalDto terminalDto = modelMapper.map(serviceDto.getData(), PosTerminalDto.class);
                    if (p.getPaymentRule().equals(AppConstant.PaymentRule.CASH)) {
                        accountId = terminalDto.getBankAccountCashId();
                    } else if (p.getPaymentRule().equals(AppConstant.PaymentRule.QRCODE_MBB)) {
//                    accountId = terminalDto.getBankAccountId();
                    } else if (p.getPaymentRule().equals(AppConstant.PaymentRule.VISA)) {
                        accountId = terminalDto.getBankAccountVisaId();
                    } else {
                        accountId = terminalDto.getBankAccountId();
                    }
                    paymentDto.setBankAccountId(accountId);
                }

                paymentDto.setIsOriginal("Y"); //Mark Payment is created from Order
                GlobalReponse globalReponse =  paymentService.save(paymentDto);
                PaymentDto payment = (PaymentDto) globalReponse.getData();
                //PosPayment
                posPayment.setPaymentId(payment.getId());
                posPayment = posPaymentRepository.save(posPayment);

                //Save Voucher Transaction
                if (p.getPaymentRule().equals(AppConstant.PaymentRule.VOUCHER)) {
                    if (p.getDetails() != null && !p.getDetails().isEmpty()) {
                        for (PaymentDetailDto detail : p.getDetails()) {

                            VoucherTransactionDto dto = VoucherTransactionDto.builder()
                                    .orgId(invoice.getOrgId())
                                    .customerId(invoice.getCustomerId())
                                    .posOrderId(invoiceDto.getPosOrderId())
                                    .voucherCode(detail.getCode())
                                    .amount(detail.getAmount().negate()).build();
                            voucherService.saveVoucherTransaction(dto);
                        }
                    }
                }

                if (p.getPaymentRule().equals(AppConstant.PaymentRule.COUPON)) {
                    if (p.getDetails() != null && !p.getDetails().isEmpty()) {
                        for (PaymentDetailDto detail : p.getDetails()) {
                            couponService.updateCouponStatus(detail.getCode(), "N");
                        }
                    }
                }

            }
        }

//        if (invoice.getInvoiceStatus().equals(AppConstant.DOC_STATUS_COMPLETED)) {
//
//            //Update Storage On Hand Product
////            updateStorageOnHandProduct(listProduct);
//        }


        invoiceDto = modelMapper.map(invoice, InvoiceDto.class);
        invoiceDto.setId(invoice.getId());
        response.setData(invoiceDto);
        response.setMessage("Invoice saved successfully");
        response.setStatus(HttpStatus.OK.value());
        log.info("Invoice saved successfully with ID: {}", invoice.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        InvoiceView invoice = invoiceViewRepository.findById(id).orElseThrow(() -> new PosException("Invoice not found"));
        InvoiceViewDto invoiceDto = modelMapper.map(invoice, InvoiceViewDto.class);
        invoiceDto.setDateInvoiced(invoice.getDateInvoiced() != null ? DateHelper.fromInstant(invoice.getDateInvoiced()) : null);
        invoiceDto.setAccountingDate(invoice.getAccountingDate() != null ? DateHelper.fromInstant(invoice.getAccountingDate()) : null);
        StringBuilder sql = new StringBuilder("select * from d_invoiceline_v where d_tenant_id = :tenantId and d_org_id = :orgId and d_invoice_id = :invoiceId");

        List<Map<String, Object>> resultLine = entityManager.createNativeQuery(sql.toString())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", invoice.getOrgId())
                .setParameter("invoiceId", invoice.getId()).unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

        List<InvoiceLineViewDto> invoiceLineDtos = new ArrayList<>();
        for (Map<String, Object> map : resultLine) {
            InvoiceLineViewDto invoiceLineDto = modelMapper.map(map, InvoiceLineViewDto.class);
            invoiceLineDto.setSalePrice(ParseHelper.BIGDECIMAL.parse(map.get("salesprice")));
            invoiceLineDto.setQty(ParseHelper.BIGDECIMAL.parse(map.get("qty")));
            invoiceLineDto.setPriceEntered(ParseHelper.BIGDECIMAL.parse(map.get("price_discount")));
            invoiceLineDto.setLineNetAmt(ParseHelper.BIGDECIMAL.parse(map.get("linenet_amt")));
            invoiceLineDto.setGrandTotal(ParseHelper.BIGDECIMAL.parse(map.get("grand_total")));
            invoiceLineDto.setUomName(ParseHelper.STRING.parse(map.get("uom_name")));
            invoiceLineDtos.add(invoiceLineDto);
        }
        invoiceDto.setInvoiceLine(invoiceLineDtos);
        sql = new StringBuilder("select * from d_invoice_v_all where d_tenant_id = :tenantId and d_org_id = :orgId and d_invoice_id = :invoiceId");

        List<Map<String, Object>> result = entityManager.createNativeQuery(sql.toString()) .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", invoice.getOrgId())
                .setParameter("invoiceId", invoice.getId()).unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

        List<PaymentViewDto> invoiceLineViewDtos = new ArrayList<>();
        for (Map<String, Object> map : result) {
            PaymentViewDto paymentViewDto = modelMapper.map(map, PaymentViewDto.class);
            paymentViewDto.setDocumentNo(ParseHelper.STRING.parse(map.get("document_no")));
            paymentViewDto.setPaymentDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(map.get("payment_date"))));
            paymentViewDto.setCustomerName(ParseHelper.STRING.parse(map.get("customer_name")));
            paymentViewDto.setVendorName(ParseHelper.STRING.parse(map.get("vendor_name")));
            paymentViewDto.setPaymentRule(ParseHelper.STRING.parse(map.get("payment_method_name")));
            paymentViewDto.setPaymentAmount(ParseHelper.BIGDECIMAL.parse(map.get("payment_amount")));
            paymentViewDto.setPaymentStatus(ParseHelper.STRING.parse(map.get("payment_status_name")));
            invoiceLineViewDtos.add(paymentViewDto);
        }
        invoiceDto.setPaymentDtos(invoiceLineViewDtos);
//        sql = new StringBuilder("select coalesce(sum(tax_amount),0) as total_receipt from pos.d_pos_receipt_other where d_pos_order_id = :posOrderId" );
//        List<BigDecimal> totalReceipt =  entityManager.createNativeQuery(sql.toString())
//                .setParameter("posOrderId", invoice.getDPosOrderId()).getResultList();
//        invoiceDto.setReceivedAmount(totalReceipt.isEmpty() ? BigDecimal.ZERO : totalReceipt.get(0) );
//
//        sql = new StringBuilder("select coalesce(sum( tax_amount),0) as tax_amount from pos.d_pos_orderline where d_pos_order_id = :posOrderId" );
//        List<BigDecimal> taxAmount =   entityManager.createNativeQuery(sql.toString())
//                .setParameter("posOrderId", invoice.getDPosOrderId()).getResultList();
//        invoiceDto.setTaxAmount(taxAmount.isEmpty() ? BigDecimal.ZERO : taxAmount.get(0));
//
//        sql = new StringBuilder("select coalesce(sum(price_discount),0) from d_pos_orderline where d_pos_order_id  = :posOrderId" );
//        List<BigDecimal> discountAmount =  entityManager.createNativeQuery(sql.toString())
//                .setParameter("posOrderId", invoice.getDPosOrderId()).getResultList();
//        invoiceDto.setDiscountAmount(discountAmount.isEmpty() ? BigDecimal.ZERO : discountAmount.get(0));

        //Total Amount Receipt Other
        String sqlROSum = "SELECT round(sum(receipt_amount),2) as sum_receipt_amount FROM pos.d_pos_receipt_other WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId and is_cal = 'Y'";

        BigDecimal sumReceiptAmount = ParseHelper.BIGDECIMAL.parse(entityManager.createNativeQuery(sqlROSum)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", invoice.getDPosOrderId())
                .getSingleResult());


        //Total Amount Tax
        String sqlTaxSum = "SELECT round(sum(tax_amount),2) as sum_tax_amount FROM pos.d_pos_taxline WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        BigDecimal sumTaxAmount = ParseHelper.BIGDECIMAL.parse(entityManager.createNativeQuery(sqlTaxSum)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", invoice.getDPosOrderId())
                .getSingleResult());

        //Total Amount Flat Discount
        String sqlFlatAmt = "SELECT flat_amt FROM pos.d_pos_order WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        BigDecimal sumFlatAmt = ParseHelper.BIGDECIMAL.parse(entityManager.createNativeQuery(sqlFlatAmt)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", invoice.getDPosOrderId())
                .getSingleResult());

        //Total Amount Deduction
        String sqlDeductionAmt = "SELECT deduction_amount FROM pos.d_pos_order WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        BigDecimal sumDeductionAmt = ParseHelper.BIGDECIMAL.parse(entityManager.createNativeQuery(sqlDeductionAmt)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", invoice.getDPosOrderId())
                .getSingleResult());

        sumFlatAmt = sumFlatAmt != null ? sumFlatAmt : BigDecimal.ZERO;
        sumDeductionAmt = sumDeductionAmt != null ? sumDeductionAmt : BigDecimal.ZERO;
        BigDecimal totalDiscount = sumFlatAmt.add(sumDeductionAmt);

        if (!"INC".equals(invoice.getPriceCateCode())) {

            invoiceDto.setTaxAmount(sumTaxAmount == null ? BigDecimal.ZERO : sumTaxAmount);
        } else {

            invoiceDto.setTaxAmount(BigDecimal.ZERO);
        }

//        invoiceDto.setDiscountAmount(sumFlatAmt == null ? BigDecimal.ZERO : sumFlatAmt);
        invoiceDto.setDiscountAmount(totalDiscount);
        invoiceDto.setReceiptOtherAmount(sumReceiptAmount == null ? BigDecimal.ZERO : sumReceiptAmount);
        invoiceDto.setTax(getPosTaxlineDto(invoice.getDPosOrderId()));
        invoiceDto.setReceiptOther(getPosReceiptOther(invoice.getDPosOrderId()));

        StringBuilder sql1 = new StringBuilder("select  d_floor_id, floor_no, floor_name, " +
                " d_table_id, table_no, table_name, " +
                " order_guests, d_pos_terminal_id, terminal_name, printer_ip FROM pos.d_pos_order_bill_info_v " +
                " WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId " );

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql1.toString())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", invoice.getDPosOrderId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);
            invoiceDto.setTable(TableDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_table_id")))
                    .name(ParseHelper.STRING.parse(row.get("table_name")))
                    .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
                    .build());

            invoiceDto.setFloor(FloorDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_floor_id")))
                    .name(ParseHelper.STRING.parse(row.get("floor_name")))
                    .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                    .build());

            invoiceDto.setPosTerminal(PosTerminalDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_pos_terminal_id")))
                    .name(ParseHelper.STRING.parse(row.get("terminal_name")))
                    .printerIp(ParseHelper.STRING.parse(row.get("printer_ip")))
                    .build());
        }

        return GlobalReponse.builder()
                .data(invoiceDto)
                .message("success")
                .status(HttpStatus.OK.value())
                .errors("").build();
    }

    public List<PosReceiptOtherDto> getPosReceiptOther(BigDecimal posOrderId) {
        try {
            String sql = "SELECT d_pos_receipt_other_id, " +
                    "d_tenant_id, " +
                    "d_org_id, " +
                    "d_pos_order_id, " +
                    "is_percent, " +
                    "d_tax_id, " +
                    "tax_name, " +
                    "d_receipt_other_id, " +
                    "receipt_name, " +
                    "receipt_wtax_name, " +
                    "code, " +
                    "amount_or_percent, " +
                    "total_amount, " +
                    "receipt_amount, " +
                    "tax_amount, " +
                    "receipt_wtax_amount " +
                    " FROM pos.d_pos_receipt_other_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId AND is_cal = 'Y'";


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
                        .id(ParseHelper.INT.parse(row.get("d_pos_receipt_other_id")))
                        .receiptName(ParseHelper.STRING.parse(row.get("receipt_name")))
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .receiptWithTaxName(ParseHelper.STRING.parse(row.get("receipt_wtax_name")))
                        .amountOrPercent(ParseHelper.BIGDECIMAL.parse(row.get("amount_or_percent")))
                        .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                        .receiptAmount(ParseHelper.BIGDECIMAL.parse(row.get("receipt_amount")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .receiptAmountWithTax(ParseHelper.BIGDECIMAL.parse(row.get("receipt_wtax_amount")))
                        .build();
                posReceiptOtherList.add(posReceiptOtherDto);
            }


            return posReceiptOtherList;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
    }

    public List<PosTaxLineDto> getPosTaxlineDto(BigDecimal posOrderId) {
        try {
            String sql = "SELECT d_tax_id, " +
                    "name, " +
                    "tax_rate, " +
                    "tax_amount, " +
                    "tax_base_amount " +
                    " FROM pos.d_pos_taxline_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", posOrderId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosTaxLineDto> posTaxLineDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosTaxLineDto posReceiptOtherDto = PosTaxLineDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_tax_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .taxRate(ParseHelper.BIGDECIMAL.parse(row.get("tax_rate")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .taxBaseAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_base_amount")))
                        .build();
                posTaxLineDto.add(posReceiptOtherDto);
            }


            return posTaxLineDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
    }

    public String updateCusDebit(Integer customerId, BigDecimal debitAmount) {

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId)
                .debitAmount(debitAmount)
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CustomerDto> requestEntity = new HttpEntity<>(customerDto, headers);

        GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.CUSTOMER_SERVICE_API_UPDATE, requestEntity, GlobalReponse.class);

        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new PosException(response.getMessage());
        }
        return "COM";
    }


    public String updateStorageOnHandProduct(List<ProductDto> listProduct) {

        if (listProduct == null || listProduct.isEmpty()) {
            return "COM";
        }

        // Create headers
        HttpHeaders headers = new HttpHeaders();
// Add headers as needed
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        ListProductDto dto = new ListProductDto();
        dto.setProducts(listProduct);
        HttpEntity<ListProductDto> requestEntity = new HttpEntity<>(dto, headers);
        //Response from Invoice Service
        GlobalReponse reponseProduct = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_UPDATEALL_PR_API_URL,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();
        if (reponseProduct.getStatus().intValue() != HttpStatus.OK.value()
                && reponseProduct.getStatus().intValue() != HttpStatus.CREATED.value()) {
            return "FAI";
        }

        return "COM";
    }
}
