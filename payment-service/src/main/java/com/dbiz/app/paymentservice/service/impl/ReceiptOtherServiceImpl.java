package com.dbiz.app.paymentservice.service.impl;


import com.dbiz.app.paymentservice.domain.PosReceiptOther;
import com.dbiz.app.paymentservice.domain.ReceiptOther;
import com.dbiz.app.paymentservice.domain.ReceiptOtherOrg;
import com.dbiz.app.paymentservice.repository.PosReceiptOtherRepository;
import com.dbiz.app.paymentservice.repository.ReceiptOtherOrgRepository;
import com.dbiz.app.paymentservice.repository.ReceiptOtherRepository;
import com.dbiz.app.paymentservice.service.ReceiptOtherService;
import com.dbiz.app.paymentservice.specification.ReceiptOrtherSpecification;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.integrationDto.posOrder.PaymentInfoDto;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.ReceiptOtherDto;
import org.common.dbiz.dto.orderDto.ReceiptOtherOrgDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.paymentDto.PaymentDetailDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.*;
import org.common.dbiz.dto.paymentDto.request.BillInfoReqDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReceiptOtherQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ReceiptOtherServiceImpl implements ReceiptOtherService {

    private final ReceiptOtherRepository receiptOtherRepository;

    private final ReceiptOtherOrgRepository receiptOtherOrgRepository;

    private final MessageSource messageSource;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    private final QueryEngine queryEngine;

    private final DataSource dataSource;

    private StringBuilder sql;
    private final PosReceiptOtherRepository posReceiptOtherRepository;
    private final ObjectMapper objectMapper;

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(ReceiptOtherQueryRequest request) {
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<ReceiptOther> spec = ReceiptOrtherSpecification.getSpecification(request);

        Page<ReceiptOther> resultPage = receiptOtherRepository.findAll(spec, pageable);
        List<ReceiptOtherDto> dataDto = resultPage.getContent().stream().map((element) -> {
                    ReceiptOtherDto itemResult = modelMapper.map(element, ReceiptOtherDto.class);
                    sql = new StringBuilder("select d_receipt_other_org_id, d_receipt_other_id, d_org_id, name, wards, phone, code, is_active \n" +
                            "from pos.d_receipt_other_org_v where d_receipt_other_id = :d_receipt_other_id");


                    List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                            .setParameter("d_receipt_other_id", itemResult.getId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    List<ReceiptOtherOrgDto> receiptOtherOrgs = new ArrayList<>();
                    for (Map<String, Object> row : results) {
                        ReceiptOtherOrgDto subItem = ReceiptOtherOrgDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_receipt_other_org_id")))
                                .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                                .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                                .phone(ParseHelper.STRING.parse(row.get("phone")))
                                .wards(ParseHelper.STRING.parse(row.get("wards")))
                                .name(ParseHelper.STRING.parse(row.get("name"))).build();
                        receiptOtherOrgs.add(subItem);
                    }
                    itemResult.setReceiptOtherOrgs(receiptOtherOrgs);

                    return itemResult;
                }
        ).collect(Collectors.toList());

        return GlobalReponsePagination.builder()
                .data(dataDto)
                .totalItems(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .currentPage(resultPage.getNumber())
                .errors("")
                .status(HttpStatus.OK.value())
                .pageSize(resultPage.getSize())
                .build();
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(ReceiptOtherDto entity) {
        log.info("ReceiptOtherDto: {}", entity);
        GlobalReponse globalReponse = new GlobalReponse();
        if (entity.getId() != null) {// update
            ReceiptOther enUpdate = receiptOtherRepository.findById(entity.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("receipt.other.notFound", null, LocaleContextHolder.getLocale())));
            modelMapper.map(entity, enUpdate);
            receiptOtherRepository.save(enUpdate);

            if (entity.getOrgIds() != null) {

                //Thanh
//                receiptOtherOrgRepository.updateIsActiveByReceiptOtherId(enUpdate.getId(), "N");
//                Arrays.stream(entity.getOrgIds()).forEach(
//                        orgId ->
//                        {
//                            ReceiptOtherOrg receiptOtherOrg = receiptOtherOrgRepository.findByReceiptOtherIdAndOrgId(enUpdate.getId(), orgId);
//                            if (receiptOtherOrg == null) {
//                                receiptOtherOrgRepository.save(ReceiptOtherOrg.builder().orgId(orgId)
//                                        .receiptOtherId(enUpdate.getId())
//                                        .tenantId(AuditContext.getAuditInfo().getTenantId()).build());
//                            }else
//                            {
//                                receiptOtherOrg.setIsActive("Y");
//                                receiptOtherOrgRepository.save(receiptOtherOrg);
//                            }
//                        }
//                );

                //Tien
                receiptOtherOrgRepository.deleteReceiptOtherOrgByReceiptOtherId(enUpdate.getId());
                receiptOtherOrgRepository.flush();
                Arrays.stream(entity.getOrgIds()).forEach(
                        orgId -> {
                            ReceiptOtherOrg item = ReceiptOtherOrg.builder().orgId(orgId)
                                    .receiptOtherId(enUpdate.getId())
                                    .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                            receiptOtherOrgRepository.save(item);
                        }
                );
                globalReponse.setData(modelMapper.map(enUpdate, ReceiptOtherDto.class));
                globalReponse.setMessage(messageSource.getMessage("receipt.other.updated", null, LocaleContextHolder.getLocale()));
                globalReponse.setErrors("");
                globalReponse.setStatus(HttpStatus.OK.value());
            }

        } else {// insert
            ReceiptOther enSave = modelMapper.map(entity, ReceiptOther.class);
            enSave.setTenantId(AuditContext.getAuditInfo().getTenantId());

            if (enSave.getOrgId() == null)
                enSave.setOrgId(0);
            if (enSave.getCode() == null) {
                enSave.setCode("REC" + (receiptOtherRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth());
            }
            receiptOtherRepository.save(enSave);

            if (entity.getOrgIds() != null) {
                Arrays.stream(entity.getOrgIds()).forEach(
                        orgId -> {
                            ReceiptOtherOrg item = ReceiptOtherOrg.builder().orgId(orgId)
                                    .receiptOtherId(enSave.getId())
                                    .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                            receiptOtherOrgRepository.save(item);
                        }
                );
            }
            globalReponse.setData(modelMapper.map(enSave, ReceiptOtherDto.class));
            globalReponse.setMessage(messageSource.getMessage("receipt.other.saved", null, LocaleContextHolder.getLocale()));
            globalReponse.setErrors("");
            globalReponse.setStatus(HttpStatus.OK.value());

        }
        return globalReponse;
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {

        Optional<ReceiptOther> receiptOther = receiptOtherRepository.findById(integer);

        if (receiptOther.isPresent()) {

            receiptOtherOrgRepository.deleteReceiptOtherOrgByReceiptOtherId(receiptOther.get().getId());

            receiptOtherRepository.deleteById(receiptOther.get().getId());

            return GlobalReponse.builder()
                    .data(null)
                    .message(messageSource.getMessage("receipt.other.deleted", null, LocaleContextHolder.getLocale()))
                    .errors("")
                    .status(HttpStatus.OK.value())
                    .build();

        } else {
            throw new PosException(messageSource.getMessage("receipt.other.notFound", null, LocaleContextHolder.getLocale()));
        }

    }


    @Override
    public GlobalReponse calculateAmountROByOrder(CalculateAmountRODto dto) {

        log.info("CalculateAmountRODto: {}", dto);

        try {

            posReceiptOtherRepository.deletePosReceiptOtherByPosOrderIdAndTenantId(dto.getPosOrderId(), AuditContext.getAuditInfo().getTenantId());

            String sql = "SELECT d_tenant_id , " +
                    "d_org_id, " +
                    "d_tax_id, " +
                    "d_receipt_other_id, " +
                    "d_pos_order_id, " +
                    "amount_or_percent, " +
                    "order_amount, " +
                    "cal_other_amt, " +
                    "cal_other_tax_amt, " +
                    "cal_other_wtax_amt " +
                    " FROM pos.d_receipt_other_cal_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosReceiptOther posReceiptOther = PosReceiptOther.builder()
                        .receiptOtherId(ParseHelper.INT.parse(row.get("d_receipt_other_id")))
                        .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                        .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                        .posOrderId(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                        .taxId(ParseHelper.INT.parse(row.get("d_tax_id")))
                        .amountOrPercent(ParseHelper.BIGDECIMAL.parse(row.get("amount_or_percent")))
                        .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("order_amount")))
                        .receiptAmount(ParseHelper.BIGDECIMAL.parse(row.get("cal_other_amt")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("cal_other_tax_amt")))
                        .receiptAmountWithTax(ParseHelper.BIGDECIMAL.parse(row.get("cal_other_wtax_amt")))
                        .build();

                posReceiptOtherRepository.save(posReceiptOther);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .data(new GlobalReponse())
                .message(messageSource.getMessage("receipt.other.calculate.success", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponse getPosCost(CalculateAmountRODto dto) {
        log.info("CalculateAmountRODto: {}", dto);
        PosReceiptOtherRespDto posReceiptOtherRespDto = null;
        TotalPosOrderCostDto totalPosOrderCostDto = null;

        try {
            String listROJson = "[]";
            if (dto.getListReceiptOther() != null && dto.getListReceiptOther().size() > 0) {
                listROJson = objectMapper.writeValueAsString(dto.getListReceiptOther());
            }
            log.info("listROJson: {}", listROJson);

//            // nếu ko truyền chiết khấu, lấy theo chiết khấu dưới db nếu có (case set chiết khấu xog out ra vô lại thanh toán)
//            if (dto.getDeduction().getDeductionPercent() == null && dto.getDeduction().getDeductionAmount() == null) {
//                String checkDeductionSql =
//                        "SELECT " +
//                            "deduction_percent, " +
//                            "deduction_amount, " +
//                            "deduction_description " +
//                        "FROM pos.d_pos_order " +
//                        "WHERE d_tenant_id = :tenantId " +
//                        "AND d_pos_order_id = :orderId ";
//                List<Map<String, Object>> result = entityManager.createNativeQuery(checkDeductionSql)
//                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                        .setParameter("orderId", dto.getPosOrderId())
//                        .unwrap(NativeQuery.class)
//                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                        .getResultList();
//                for (Map<String, Object> row : result) {
//                    dto.getDeduction().setDeductionPercent(ParseHelper.BIGDECIMAL.parse(row.get("deduction_percent")));
//                    dto.getDeduction().setDeductionAmount(ParseHelper.BIGDECIMAL.parse(row.get("deduction_amount")));
//                    dto.getDeduction().setDeductionDescription(ParseHelper.STRING.parse(row.get("deduction_description")));
//                }
//            }
            String deductionJson = "{}";
            if (dto.getDeduction() != null) {
                deductionJson = objectMapper.writeValueAsString(dto.getDeduction());
            }
            log.info("deductionJson: {}", deductionJson);

            Query queryProcedure = entityManager.createNativeQuery("CALL pos.d_cal_pos_cost(:tenantId, :orgId, :posOrderId, cast(:listRO as jsonb), cast(:deduction as jsonb))");
            queryProcedure.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
            queryProcedure.setParameter("orgId", dto.getOrgId());
            queryProcedure.setParameter("posOrderId", dto.getPosOrderId());
            queryProcedure.setParameter("listRO", listROJson);
            queryProcedure.setParameter("deduction", deductionJson);

            queryProcedure.executeUpdate();

            String sql = "SELECT dpobv.d_org_id, dpobv.org_name, dpobv.address, dpobv.d_pos_terminal_id, dpobv.terminal_name, " +
                    "dpobv.d_customer_id, dpobv.customer_name, " +
                    "dpobv.total_line, " +
                    "dpobv.total_amount, " +
                    "dpobv.flat_amt, " +
                    "dpobv.flat_discount, " +
                    "dpobv.deduction_percent, " +
                    "dpobv.deduction_amount, " +
                    "dpobv.deduction_description, " +
                    "dpobv.order_date, dpobv.document_no, dpobv.phone1, " +
                    "dpobv.printer_product_id, " +
                    "dpobv.printer_vendor_id, " +
                    "dpobv.printer_name, " +
                    "dpobv.printer_page_size, " +
                    "dpobv.printer_page_so_qty, " +
                    "dpobv.printer_page_temp_qty, " +
                    "dpobv.printer_type, dpobv.printer_port, dpobv.printer_ip, " +
                    "dpobv.d_floor_id, dpobv.floor_no, dpobv.floor_name, " +
                    "dpobv.d_table_id, dpobv.table_no, dpobv.table_name, " +
                    "dpobv.order_guests, " +
                    "dpo.price_category_code, " +
                    "di.invoice_form, " +
                    "di.invoice_sign, " +
                    "di.invoice_no, " +
                    "di.search_code, " +
                    "di.search_link, " +
                    "di.einvoice_supplier_taxcode, " +
                    "di.einvoice_taxcode " +
                    "FROM pos.d_pos_order_bill_info_v dpobv " +
                    "INNER JOIN d_pos_order dpo ON dpo.d_pos_order_id = dpobv.d_pos_order_id " +
                    "LEFT JOIN d_invoice di ON di.d_pos_order_id = dpobv.d_pos_order_id " +
                    "WHERE dpobv.d_tenant_id = :tenantId " +
                    "AND dpobv.d_pos_order_id = :orderId";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosReceiptOtherDto> posReceiptOtherList = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                // lúc trước có Chiết khấu khách hàng, thêm chức năng Chiết khấu tổng bill
                // Firm rằng sẽ trả tổng Số tiền 2 loại chiết khấu qua trường Chiết khấu khách hàng Số tiền để FE ko cần xử lý thêm 1 trường khi in hóa đơn
                // Trường Chiết khấu khách hàng Số tiền sẽ ko là số tiền chiết khấu khách hàng, và ko match với Chiết khấu khách hàng Phần trăm
                BigDecimal flatAmt = ParseHelper.BIGDECIMAL.parse(row.get("flat_amt"));
                BigDecimal deductionAmt = BigDecimal.ZERO;
                flatAmt = flatAmt != null ? flatAmt : BigDecimal.ZERO;

//                if (dto.getDeduction().getDeductionDescription() == null || "".equals(dto.getDeduction().getDeductionDescription())) {
//
//                     deductionAmt = ParseHelper.BIGDECIMAL.parse(row.get("deduction_amount"));
//                }

                deductionAmt = deductionAmt != null ? deductionAmt : BigDecimal.ZERO;
                BigDecimal totalDiscount = flatAmt.add(deductionAmt);

                totalPosOrderCostDto = TotalPosOrderCostDto.builder()
                        .posOrderId(dto.getPosOrderId())
                        .documentNo(ParseHelper.STRING.parse(row.get("document_no")))
                        .orderDate(row.get("order_date") != null
                                ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date")))
                                : null)
                        .orderGuests(((Number)row.get("order_guests")).intValue())
                        .priceCateCode(ParseHelper.STRING.parse(row.get("price_category_code")))
                        .org(OrgDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_org_id")))
                                .name(ParseHelper.STRING.parse(row.get("org_name")))
                                .address(ParseHelper.STRING.parse(row.get("address")))
                                .build())
                        .posTerminal(PosTerminalDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_pos_terminal_id")))
                                .name(ParseHelper.STRING.parse(row.get("terminal_name")))
                                .printerProductId(ParseHelper.STRING.parse(row.get("printer_product_id")))
                                .printerVendorId(ParseHelper.STRING.parse(row.get("printer_vendor_id")))
                                .printerName(ParseHelper.STRING.parse(row.get("printer_name")))
                                .printerPageSize(ParseHelper.INT.parse(row.get("printer_page_size")))
                                .printerPageSoQty(ParseHelper.INT.parse(row.get("printer_page_so_qty")))
                                .printerPageTempQty(ParseHelper.INT.parse(row.get("printer_page_temp_qty")))
                                .printerType(ParseHelper.STRING.parse(row.get("printer_type")))
                                .printerPort(ParseHelper.BIGDECIMAL.parse(row.get("printer_port")))
                                .printerIp(ParseHelper.STRING.parse(row.get("printer_ip")))
                                .build())
                        .customer(CustomerDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_customer_id")))
                                .name(ParseHelper.STRING.parse(row.get("customer_name")))
                                .phone1(ParseHelper.STRING.parse(row.get("phone1")))
                                .build())
                        .floor(FloorDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_floor_id")))
                                .name(ParseHelper.STRING.parse(row.get("floor_name")))
                                .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                                .build())
                        .table(TableDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_table_id")))
                                .name(ParseHelper.STRING.parse(row.get("table_name")))
                                .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
                                .build())
                        .flatDiscount(ParseHelper.BIGDECIMAL.parse(row.get("flat_discount")))
                        .totalLine(ParseHelper.BIGDECIMAL.parse(row.get("total_line")))
                        .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                        .flatAmount(totalDiscount)
                        .deduction(DeductionDto.builder()
                                .deductionPercent(ParseHelper.BIGDECIMAL.parse(row.get("deduction_percent")))
                                .deductionAmount(ParseHelper.BIGDECIMAL.parse(row.get("deduction_amount")))
                                .deductionDescription(ParseHelper.STRING.parse(row.get("deduction_description")))
                                .build())
                        .eInvoiceInfoDto(EInvoiceInfoDto.builder()
                                .invoiceForm(ParseHelper.STRING.parse(row.get("invoice_form")))
                                .invoiceSign(ParseHelper.STRING.parse(row.get("invoice_sign")))
                                .invoiceNo(ParseHelper.STRING.parse(row.get("invoice_no")))
                                .searchCode(ParseHelper.STRING.parse(row.get("search_code")))
                                .searchLink(ParseHelper.STRING.parse(row.get("search_link")))
                                .supplierTaxCode(ParseHelper.STRING.parse(row.get("einvoice_supplier_taxcode")))
                                .taxOfCode(ParseHelper.STRING.parse(row.get("einvoice_taxcode")))
                                .build())
                        .build();
            }


            //Total Amount Receipt Other
            String sqlROSum = "SELECT round(sum(receipt_amount),2) as sum_receipt_amount FROM pos.d_pos_receipt_other WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId and is_cal = 'Y'";

            BigDecimal sumReceiptAmount = (BigDecimal) entityManager.createNativeQuery(sqlROSum)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .getSingleResult();


            //Total Amount Tax
            String sqlTaxSum = "SELECT round(sum(tax_amount),2) as sum_tax_amount FROM pos.d_pos_taxline WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";

            BigDecimal sumTaxAmount = (BigDecimal) entityManager.createNativeQuery(sqlTaxSum)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .getSingleResult();

            //Total QTY Order Line
            String sqlQtySum = "SELECT round(sum(qty),2) as sum_qty_line FROM pos.d_pos_orderline WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId and is_active = 'Y'";

            BigDecimal sumQtyAmount = (BigDecimal) entityManager.createNativeQuery(sqlQtySum)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .getSingleResult();

            if (!"INC".equals(totalPosOrderCostDto.getPriceCateCode())) {

                totalPosOrderCostDto.setTotalTaxAmount(sumTaxAmount);
                totalPosOrderCostDto.setTax(getPosTaxlineDto(dto));
            }

            totalPosOrderCostDto.setTotalQty(sumQtyAmount);
            totalPosOrderCostDto.setReceiptOtherAmount(sumReceiptAmount);
            totalPosOrderCostDto.setReceiptOther(getPosReceiptOther(dto));
            totalPosOrderCostDto.setPosOrderLine(getPosOrderLine(dto));
            totalPosOrderCostDto.setPayment(getPosPaymentDto(dto));
            totalPosOrderCostDto.setBillNo(getPosBillNo(dto));

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .data(totalPosOrderCostDto)
                .message(messageSource.getMessage("receipt.other.calculate.success", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponse getBillInfo(BillInfoReqDto reqDto) {

        log.info("ReceiptOther, service: getBillInfo");
        log.info("Input: BillInfoReqDto: {}", reqDto);

        TotalPosOrderCostDto totalPosOrderCostDto = null;

        try {
            String getPosOrderIdSql = "SELECT d_pos_order_id FROM pos.d_invoice WHERE d_invoice_id = :invoiceId ";
            Integer posOrderIdResult = ((Number) entityManager.createNativeQuery(getPosOrderIdSql)
                    .setParameter("invoiceId", reqDto.getInvoiceId())
                    .getSingleResult()).intValue();

            CalculateAmountRODto dto = CalculateAmountRODto.builder()
                    .posOrderId(posOrderIdResult)
                    .posTerminalId(reqDto.getPosTerminalId())
                    .build();

            String sql =
                    "SELECT " +
                            "  dpobv.d_org_id, " +
                            "  dpobv.org_name, " +
                            "  dpobv.address, " +
                            "  dpobv.d_pos_terminal_id, " +
                            "  dpobv.terminal_name, " +
                            "  dpobv.d_customer_id, " +
                            "  dpobv.customer_name, " +
                            "  dpobv.total_line, " +
                            "  dpobv.total_amount, " +
                            "  dpobv.flat_amt, " +
                            "  dpobv.flat_discount, " +
                            "  dpobv.deduction_percent, " +
                            "  dpobv.deduction_amount, " +
                            "  dpobv.deduction_description, " +
                            "  dpobv.order_date, " +
                            "  dpobv.document_no, " +
                            "  dpobv.phone1, " +
                            "  dpobv.printer_product_id, " +
                            "  dpobv.printer_vendor_id, " +
                            "  dpobv.printer_name, " +
                            "  dpobv.printer_page_size, " +
                            "  dpobv.printer_page_so_qty, " +
                            "  dpobv.printer_page_temp_qty, " +
                            "  dpobv.printer_type, " +
                            "  dpobv.printer_port, " +
                            "  dpobv.printer_ip, " +
                            "  dpobv.d_floor_id, " +
                            "  dpobv.floor_no, " +
                            "  dpobv.floor_name, " +
                            "  dpobv.d_table_id, " +
                            "  dpobv.table_no, " +
                            "  dpobv.table_name, " +
                            "  dpobv.order_guests, " +
                            "  di.invoice_form, " +
                            "  di.invoice_sign, " +
                            "  di.invoice_no, " +
                            "  di.search_code, " +
                            "  di.search_link, " +
                            "  di.einvoice_supplier_taxcode, " +
                            "  di.einvoice_taxcode " +
                            "FROM pos.d_pos_order_bill_info_v dpobv " +
                            "LEFT JOIN d_invoice di ON di.d_pos_order_id = dpobv.d_pos_order_id " +
                            "WHERE dpobv.d_tenant_id = :tenantId " +
                            "  AND dpobv.d_pos_order_id = :orderId";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                totalPosOrderCostDto = TotalPosOrderCostDto.builder()
                        .posOrderId(dto.getPosOrderId())
                        .documentNo(ParseHelper.STRING.parse(row.get("document_no")))
                        .orderDate(row.get("order_date") != null
                                ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date")))
                                : null)
                        .orderGuests(((Number)row.get("order_guests")).intValue())
                        .org(OrgDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_org_id")))
                                .name(ParseHelper.STRING.parse(row.get("org_name")))
                                .address(ParseHelper.STRING.parse(row.get("address")))
                                .build())
                        .posTerminal(PosTerminalDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_pos_terminal_id")))
                                .name(ParseHelper.STRING.parse(row.get("terminal_name")))
                                .printerProductId(ParseHelper.STRING.parse(row.get("printer_product_id")))
                                .printerVendorId(ParseHelper.STRING.parse(row.get("printer_vendor_id")))
                                .printerName(ParseHelper.STRING.parse(row.get("printer_name")))
                                .printerPageSize(ParseHelper.INT.parse(row.get("printer_page_size")))
                                .printerPageSoQty(ParseHelper.INT.parse(row.get("printer_page_so_qty")))
                                .printerPageTempQty(ParseHelper.INT.parse(row.get("printer_page_temp_qty")))
                                .printerType(ParseHelper.STRING.parse(row.get("printer_type")))
                                .printerPort(ParseHelper.BIGDECIMAL.parse(row.get("printer_port")))
                                .printerIp(ParseHelper.STRING.parse(row.get("printer_ip")))
                                .build())
                        .customer(CustomerDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_customer_id")))
                                .name(ParseHelper.STRING.parse(row.get("customer_name")))
                                .phone1(ParseHelper.STRING.parse(row.get("phone1")))
                                .build())
                        .floor(FloorDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_floor_id")))
                                .name(ParseHelper.STRING.parse(row.get("floor_name")))
                                .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                                .build())
                        .table(TableDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_table_id")))
                                .name(ParseHelper.STRING.parse(row.get("table_name")))
                                .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
                                .build())
                        .flatDiscount(ParseHelper.BIGDECIMAL.parse(row.get("flat_discount")))
                        .totalLine(ParseHelper.BIGDECIMAL.parse(row.get("total_line")))
                        .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                        .flatAmount(ParseHelper.BIGDECIMAL.parse(row.get("flat_amt")))
                        .deduction(DeductionDto.builder()
                                .deductionPercent(ParseHelper.BIGDECIMAL.parse(row.get("deduction_percent")))
                                .deductionAmount(ParseHelper.BIGDECIMAL.parse(row.get("deduction_amount")))
                                .deductionDescription(ParseHelper.STRING.parse(row.get("deduction_description")))
                                .build())
                        .eInvoiceInfoDto(EInvoiceInfoDto.builder()
                                .invoiceForm(ParseHelper.STRING.parse(row.get("invoice_form")))
                                .invoiceSign(ParseHelper.STRING.parse(row.get("invoice_sign")))
                                .invoiceNo(ParseHelper.STRING.parse(row.get("invoice_no")))
                                .searchCode(ParseHelper.STRING.parse(row.get("search_code")))
                                .searchLink(ParseHelper.STRING.parse(row.get("search_link")))
                                .supplierTaxCode(ParseHelper.STRING.parse(row.get("einvoice_supplier_taxcode")))
                                .taxOfCode(ParseHelper.STRING.parse(row.get("einvoice_taxcode")))
                                .build())
                        .build();
            }

            //Total Amount Receipt Other
            String sqlROSum = "SELECT round(sum(receipt_amount),2) as sum_receipt_amount FROM pos.d_pos_receipt_other WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId and is_cal = 'Y'";

            BigDecimal sumReceiptAmount = (BigDecimal) entityManager.createNativeQuery(sqlROSum)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .getSingleResult();


            //Total Amount Tax
            String sqlTaxSum = "SELECT round(sum(tax_amount),2) as sum_tax_amount FROM pos.d_pos_taxline WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";

            BigDecimal sumTaxAmount = (BigDecimal) entityManager.createNativeQuery(sqlTaxSum)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .getSingleResult();

            //Total QTY Order Line
            String sqlQtySum = "SELECT round(sum(qty),2) as sum_qty_line FROM pos.d_pos_orderline WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId and is_active = 'Y'";

            BigDecimal sumQtyAmount = (BigDecimal) entityManager.createNativeQuery(sqlQtySum)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .getSingleResult();

            totalPosOrderCostDto.setTotalTaxAmount(sumTaxAmount);
            totalPosOrderCostDto.setTotalQty(sumQtyAmount);
            totalPosOrderCostDto.setReceiptOtherAmount(sumReceiptAmount);
            totalPosOrderCostDto.setReceiptOther(getPosReceiptOther(dto));
            totalPosOrderCostDto.setPosOrderLine(getPosOrderLine(dto));
            totalPosOrderCostDto.setTax(getPosTaxlineDto(dto));
            totalPosOrderCostDto.setPayment(getPosPaymentDto(dto));
            totalPosOrderCostDto.setBillNo(getPosBillNo(dto));

        } catch (Exception e) {
            log.error("getBillInfo() error: ", e);
            throw new PosException(messageSource.getMessage("bill_info.failure", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .data(totalPosOrderCostDto)
                .message(messageSource.getMessage("bill_info.success", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponse getROSelection(CalculateAmountRODto dto) {

        log.info("getROSelection: {}", dto);
        List<ReceiptOtherDto> receiptOtherDto = new ArrayList<>();
        try {
            String sql = "SELECT d_receipt_other_id, " +
                    "name, " +
                    "code, " +
                    "amount_or_percent, " +
                    "is_percent, is_cal " +
                    " FROM pos.d_get_ranked_receipt_other(:tenantId, :orgId, :orderId) ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orgId", dto.getOrgId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();


            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                ReceiptOtherDto posReceiptOtherDto = ReceiptOtherDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_receipt_other_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .amountOrPercent(ParseHelper.BIGDECIMAL.parse(row.get("amount_or_percent")))
                        .isPercent(ParseHelper.STRING.parse(row.get("is_percent")))
                        .isCal(ParseHelper.STRING.parse(row.get("is_cal")))
                        .build();
                receiptOtherDto.add(posReceiptOtherDto);
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .data(receiptOtherDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();

    }

    /**
     *
     * @param receiptOtherQueryRequest
     * @return
     */
    @Override
    public GlobalReponse getOrgAccess(ReceiptOtherQueryRequest receiptOtherQueryRequest){
        log.info("ReceiptOtherQueryRequest: {}", receiptOtherQueryRequest);

        ReceiptOther resultReceiptOrder = receiptOtherRepository.findById(receiptOtherQueryRequest.getId())
                .orElseThrow(() -> new PosException(messageSource.getMessage("receipt.other.notFound", null, LocaleContextHolder.getLocale())));
        ReceiptOtherDto resultDto = modelMapper.map(resultReceiptOrder, ReceiptOtherDto.class);

        try{
            StringBuilder sql = new StringBuilder("\n" +
                    "SELECT\n" +
                    "    dpct.d_receipt_other_org_id,\n" +
                    "    dog.code AS code,\n" +
                    "    dog.name AS name,\n" +
                    "    dog.phone AS phone,\n" +
                    "    dog.address AS address,\n" +
                    "    dog.is_active AS is_active,\n" +
                    "    dog.d_org_id AS d_org_id,\n" +
                    "    dpct.d_receipt_other_id AS d_receipt_other_id," +
                    "dog.area as area \n" +
                    "FROM\n" +
                        "    d_userorg_access dua\n" +
                    "        JOIN\n" +
                    "    d_org dog ON dua.d_org_id = dog.d_org_id\n" +
                    "        LEFT JOIN\n" +
                    "    d_receipt_other_org dpct ON dua.d_org_id = dpct.d_org_id\n" +
                    "        AND dpct.d_receipt_other_id = :receiptOtherId\n" +
                    "WHERE\n" +
                    "    dua.d_user_id = :userId\n" +
                    "  AND dua.d_tenant_id = :tenantId\n  AND dog.is_active = 'Y' ");

            List<Map<String, Object>> resultList = entityManager.createNativeQuery(sql.toString())
                    .setParameter("receiptOtherId", receiptOtherQueryRequest.getId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("userId", receiptOtherQueryRequest.getUserId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            List<ReceiptOtherOrgDto> receiptOtherOrgDtos = new ArrayList<>();
            for (Map<String, Object> item : resultList) {
                ReceiptOtherOrgDto receiptOtherOrgDto = ReceiptOtherOrgDto.builder()
                        .id(ParseHelper.INT.parse(item.get("d_receipt_other_org_id")))
                        .code(ParseHelper.STRING.parse(item.get("code")))
                        .orgId(ParseHelper.INT.parse(item.get("d_org_id")))
                        .name(item.get("name").toString())
                        .phone(item.get("phone") != null ? item.get("phone").toString() : "")
                        .address(item.get("address") != null ? item.get("address").toString() : "")
                        .isActive(item.get("is_active").toString())
                        .area(item.get("area")!=null?item.get("area").toString():"")
                        .isAssign(item.get("d_receipt_other_id") != null ? "Y" : "N")
                        .build();
                receiptOtherOrgDtos.add(receiptOtherOrgDto);
            }
            resultDto.setReceiptOtherOrgs(receiptOtherOrgDtos);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return GlobalReponse.builder()
                .data(resultDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponsePagination getOrgAssign(ReceiptOtherQueryRequest req) {
        log.info("ReceiptOtherQueryRequest: {}", req);

        Parameter parameter = new Parameter();
        Integer d_user_id = AuditContext.getAuditInfo().getUserId();
        parameter.add("d_user_id", d_user_id, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        StringBuilder tb1 = new StringBuilder(" select* from d_get_user_org_access_2_v dgu where 1 = 1 ");
        tb1.append(" AND d_user_id = :d_user_id");

		StringBuilder tb2 = new StringBuilder(" select* from pos.d_receipt_other_org dpro where 1=1 ");
        tb2.append(" AND d_receipt_other_id = :d_receipt_other_id");
        
        StringBuilder sql = new StringBuilder("select " +
                " tb1.d_org_id," +
                " tb1.org_name," +
                " tb1.org_code," +
                " tb1.phone," +
                " tb1.wards," +
                " COALESCE(tb2.is_active, 'N') as is_assign from " +
                " (" + tb1 + ") as tb1 left join " + "(" + tb2 + ") as tb2 on tb1.d_org_id = tb2.d_org_id where 1=1 ");
        if(req.getIsAssign() !=null) {
            sql.append(" AND COALESCE(tb2.is_active, 'N') = :is_assign ");
        }
        sql.append(" ORDER BY org_name asc");
        sql.append(" LIMIT :limit OFFSET :offset ");

        log.info(sql.toString());

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("d_user_id", d_user_id);
        query.setParameter("d_receipt_other_id", req.getId());
        if(req.getIsAssign() !=null) {
            query.setParameter("is_assign", req.getIsAssign());
        }
        query.setParameter("limit", req.getPageSize());
        query.setParameter("offset", req.getPageSize()*req.getPage());

        List<Map<String, Object>> results = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<ReceiptOtherOrgDto> receiptOtherOrgDtos = new ArrayList<>();
        for (Map<String, Object> item : results) {
            ReceiptOtherOrgDto receiptOtherOrgDto = ReceiptOtherOrgDto.builder()
                        .id(ParseHelper.INT.parse(item.get("d_receipt_other_org_id")))
                        .code(ParseHelper.STRING.parse(item.get("org_code")))
                        .orgId(ParseHelper.INT.parse(item.get("d_org_id")))
                        .name(ParseHelper.STRING.parse(item.get("org_name")))
                        .phone(ParseHelper.STRING.parse(item.get("phone")))
                        .isAssign(ParseHelper.STRING.parse(item.get("is_assign")))
                        .wards(ParseHelper.STRING.parse(item.get("wards")))
                        .build();
                receiptOtherOrgDtos.add(receiptOtherOrgDto);
        }

        req.setOrder(null);
        req.setSortBy(null);
        Pagination pagination=  queryEngine.getPagination("pos.d_get_user_org_access_2_v", parameter, req);
        return GlobalReponsePagination.builder()
                .data(receiptOtherOrgDtos)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .currentPage(pagination.getPage())
                .pageSize(pagination.getPageSize())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    @Override
    public GlobalReponse createPosReceiptOther(TotalPosOrderCostDto receiptOtherDto) {

        log.info("ReceiptOtherDto: {}", receiptOtherDto);

        if(receiptOtherDto.getReceiptOther() != null){
            posReceiptOtherRepository.updateStatusPosReceiptOthers("N", receiptOtherDto.getPosOrderId());
            receiptOtherDto.getReceiptOther().forEach(
                    item -> {
                        PosReceiptOther posReceiptOther = posReceiptOtherRepository
                                .findByPosOrderIdAndReceiptOtherIdAndTaxId
                                        (receiptOtherDto.getPosOrderId(), item.getReceiptOtherId(), item.getTaxId());
                        if(posReceiptOther != null){
                            modelMapper.map(item, posReceiptOther);
                        }else{
                            posReceiptOther = modelMapper.map(item, PosReceiptOther.class);
                            posReceiptOther.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            posReceiptOther.setPosOrderId(receiptOtherDto.getPosOrderId());
                            posReceiptOther.setOrgId(receiptOtherDto.getOrg().getId());
                        }
                        posReceiptOther.setIsActive("Y");
                        posReceiptOther.setIsCal("Y");
                        posReceiptOtherRepository.save(posReceiptOther);
                    }
            );
        }

        return GlobalReponse.builder()
                .data(null)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();

    }


    public List<PosReceiptOtherDto> getPosReceiptOther(CalculateAmountRODto dto) {
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
                    .setParameter("orderId", dto.getPosOrderId())
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

    public List<PosTaxLineDto> getPosTaxlineDto(CalculateAmountRODto dto) {
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
                    .setParameter("orderId", dto.getPosOrderId())
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

    public List<PosOrderLineRespDto> getPosOrderLine(CalculateAmountRODto dto) {
        try {
            String sql = "";

            if(isBillMerge(dto)){
                sql = "SELECT " +
                        "d_product_id, " +
                        "product_name, " +
                        "product_wtax_name, " +
                        "salesprice,round(sum(qty),2) as qty, round(sum(linenet_amt),2) as linenet_amt,round(sum(grand_total),2) as grand_total  " +
                        " FROM pos.d_pos_orderline_bill_info_v WHERE d_tenant_id = :tenantId " +
                        " AND d_pos_order_id = :orderId group by d_product_id, product_name, product_wtax_name, salesprice ";
            }else{
                sql = "SELECT " +
                        "d_product_id, " +
                        "product_name, " +
                        "product_wtax_name, " +
                        "salesprice,qty,linenet_amt,grand_total " +
                        " FROM pos.d_pos_orderline_bill_info_v WHERE d_tenant_id = :tenantId " +
                        " AND d_pos_order_id = :orderId ";
            }


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosOrderLineRespDto> posOrderLineRespDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosOrderLineRespDto posOrderLineRespDto1 = PosOrderLineRespDto.builder()
//                        .posOrderLineId(ParseHelper.INT.parse(row.get("d_pos_orderline_id")))
                        .productId(ParseHelper.INT.parse(row.get("d_product_id")))
                        .productName(ParseHelper.STRING.parse(row.get("product_name")))
                        .productWTaxName(ParseHelper.STRING.parse(row.get("product_wtax_name")))
                        .salePrice(ParseHelper.BIGDECIMAL.parse(row.get("salesprice")))
                        .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .lineNetAmount(ParseHelper.BIGDECIMAL.parse(row.get("linenet_amt")))
                        .grandTotal(ParseHelper.BIGDECIMAL.parse(row.get("grand_total")))
                        .build();
                posOrderLineRespDto.add(posOrderLineRespDto1);
            }


            return posOrderLineRespDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
    }

    public List<PosPaymentDto> getPosPaymentDto(CalculateAmountRODto dto) {
        try {
            String sql = "SELECT d_pos_payment_id, " +
                    "name, " +
                    "payment_method,total_amount " +
                    " FROM pos.d_pos_payment_bill_info_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosPaymentDto> posPaymentDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosPaymentDto posReceiptOtherDto = PosPaymentDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_payment_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .paymentMethod(ParseHelper.STRING.parse(row.get("payment_method")))
                        .amount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                        .details(getPosPaymentDetailDto(ParseHelper.INT.parse(row.get("d_pos_payment_id"))))
                        .build();
                posPaymentDto.add(posReceiptOtherDto);
            }


            return posPaymentDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
    }

    public List<PaymentDetailDto> getPosPaymentDetailDto(Integer posPaymentId) {
        try {
            String sql = "SELECT code, " +
                    "amount " +
                    " FROM pos.d_pos_payment_dt WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_payment_id = :posPaymentId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posPaymentId", posPaymentId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PaymentDetailDto> posPaymentDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PaymentDetailDto posReceiptOtherDto = PaymentDetailDto.builder()
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .amount(ParseHelper.BIGDECIMAL.parse(row.get("amount")))
                        .build();
                posPaymentDto.add(posReceiptOtherDto);
            }


            return posPaymentDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
    }

    public String getPosBillNo(CalculateAmountRODto dto) {
        try {
//            String sql = "SELECT table_no, " +
//                    "floor_no, " +
//                    "d_pos_terminal_id " +
//                    " FROM pos.d_pos_table_floor_v WHERE d_tenant_id = :tenantId " +
//                    " AND d_pos_order_id = :orderId ";
//
//
//            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
//            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
//                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                    .setParameter("orderId", dto.getPosOrderId())
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                    .getResultList();
//            String floorNo = null;
//            String tableNo = null;
//            StringBuilder billNo = new StringBuilder();
//            Integer posTerminalId = null;
//            for (Map<String, Object> row : results) {
//                log.info("Row: {}", row);
//                floorNo = ParseHelper.STRING.parse(row.get("floor_no"));
//                tableNo = ParseHelper.STRING.parse(row.get("table_no"));
//                posTerminalId = ParseHelper.INT.parse(row.get("d_pos_terminal_id"));
//            }


//            billNo.append(posTerminalId);

//            if (floorNo != null)
//                billNo.append("-" + floorNo);
//            if (floorNo != null)
//                billNo.append(floorNo);
//            if (tableNo != null)
//                billNo.append("-" + tableNo);
//
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            String date = DateHelper.fromLocalDateMMyyyy(timestamp.toLocalDateTime().toLocalDate());
//            billNo.append("-" + date);
//
//            billNo.append("-" + dto.getPosOrderId());
//            billNo.append(dto.getPosOrderId());


            String sql = "SELECT document_no " +
                    " FROM pos.d_pos_order WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                return ParseHelper.STRING.parse(row.get("document_no"));
            }

            return "";
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
    }

    public boolean isBillMerge(CalculateAmountRODto dto) {
        try {
            String sql = "SELECT d_pos_terminal_id " +
                    " FROM pos.d_pos_terminal WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_terminal_id = :posTerminalId and is_bill_merge = 'Y'";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posTerminalId", dto.getPosTerminalId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosPaymentDto> posPaymentDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                return true;
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
        return false;
    }

    @Override
    public GlobalReponse getROSelection2(CalculateAmountRODto dto) {
        List<ReceiptOtherDto> receiptOtherDto = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder( "SELECT * FROM pos.d_get_ro_selection( :orgId, :orderId)");


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                    .setParameter("orgId", dto.getOrgId())
                    .setParameter("orderId", dto.getPosOrderId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();


            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                ReceiptOtherDto posReceiptOtherDto = ReceiptOtherDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_receipt_other_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .amountOrPercent(ParseHelper.BIGDECIMAL.parse(row.get("amount_or_percent")))
                        .isPercent(ParseHelper.STRING.parse(row.get("is_percent")))
                        .isCal(ParseHelper.STRING.parse(row.get("is_cal")))
                        .isAutoAllocate((ParseHelper.STRING.parse(row.get("is_auto_allocate"))))
                        .build();
                receiptOtherDto.add(posReceiptOtherDto);
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .data(receiptOtherDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

}
