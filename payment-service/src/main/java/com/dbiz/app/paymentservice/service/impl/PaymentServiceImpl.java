package com.dbiz.app.paymentservice.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import com.dbiz.app.paymentservice.domain.Payment;
import com.dbiz.app.paymentservice.domain.PosPayment;
import com.dbiz.app.paymentservice.repository.PosPaymentRepository;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.PaymentMethodDto;
import org.common.dbiz.dto.paymentDto.PaymentSummaryDto;
import org.common.dbiz.dto.paymentDto.request.PaymentReqDto;
import org.common.dbiz.dto.paymentDto.response.PaymentRespDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.userDto.*;
import org.common.dbiz.exception.PosException;
import com.dbiz.app.paymentservice.specification.PaymentSpecification;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.repository.PaymentRepository;
import com.dbiz.app.paymentservice.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final EntityManager entityManager;
    private final MessageSource messageSource;
    private final DataSource dataSource;
    private final PosPaymentRepository posPaymentRepository;
    private final KafkaAuditUserHelper kafkaAuditUserHelper;
    private final ObjectMapper objectMapper;

    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    @Override
    public GlobalReponsePagination findAll(Object queryRequest) {
        log.info("*** Payment List, service; fetch all Payment *");
        PaymentQueryRequest query = (PaymentQueryRequest) queryRequest;
        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<Payment> spec = PaymentSpecification.getSpecification(query);
        Page<Payment> entityList = paymentRepository.findAll(spec, pageable);
        List<PaymentDto> listData = new ArrayList<>();
        for (Payment item : entityList.getContent()) {
            PaymentDto paymentDto = modelMapper.map(item, PaymentDto.class);
            paymentDto.setPaymentDate(item.getPaymentDate() != null ? DateHelper.fromInstantDateAndTime(item.getPaymentDate()) : null);
            listData.add(paymentDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("Payment fetched successfully");
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

        PaymentDto paymentDto = (PaymentDto) Dto;

        String insertOrUpdate = "INSERT";
        if (paymentDto.getId() != null) {
            insertOrUpdate = "UPDATE";
        }

        log.info("*** Payment, service; save Payment ***");
        Payment payment = null;
        try {

            Instant paymentDate = paymentDto.getPaymentDate() != null
                    ? DateHelper.toInstantUTC(paymentDto.getPaymentDate()) : DateHelper.toInstantNowUTC();

            if (paymentDto.getId() != null) {

                payment = paymentRepository.findById(paymentDto.getId()).orElseThrow(
                        () -> new PosException(messageSource.getMessage("payment.not.found", null, LocaleContextHolder.getLocale())));
                if (payment.getPaymentStatus().equals("COM")
                        || payment.getPaymentStatus().equals("VOD")) {
                    throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
                }

                if (paymentDto.getPaymentStatus() != null && paymentDto.getPaymentStatus().equals("VOD")) {
                    payment.setPaymentStatus(paymentDto.getPaymentStatus());
                } else {
                    paymentDto.setPosTerminalId(null);
                    modelMapper.map(paymentDto, payment);
                    payment.setPaymentDate(paymentDate);
                }


            } else {
                payment = modelMapper.map(paymentDto, Payment.class);
                payment.setTenantId(AuditContext.getAuditInfo().getTenantId());
                if (payment.getDocumentNo() == null) {
//					Query query = entityManager.createNativeQuery("SELECT  nextval('pos.d_payment_sq')  ");
//					String docNo =  "PAYMENT"+((Number) query.getSingleResult()).intValue();
//					payment.setDocumentNo(docNo);
                    do {
                        Integer maxId = paymentRepository.getMaxId() + 1;
                        String docNo = DocHelper.generateDocNo(paymentDto.getDocType().equals("ARP") ? "PT" : "PC", maxId);
                        payment.setDocumentNo(docNo);
                    } while (checkExistedDocumentNo(payment.getDocumentNo()).equals("FAI"));
                } else {
                    String checkExistedDocumentNo = checkExistedDocumentNo(payment.getDocumentNo());
                    if (checkExistedDocumentNo.equals("FAI")) {
                        throw new PosException(messageSource.getMessage("document.no.exists", null, LocaleContextHolder.getLocale()));
                    }
                }
                String doctypeCode = paymentDto.getDocType() != null && paymentDto.getDocType().equals("APP")
                        ? AppConstant.DocTypeCode.AP_PAYMENT : AppConstant.DocTypeCode.AR_PAYMENT;
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE + "/"
                        + doctypeCode, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                Integer doctypeId = modelMapper.map(exRes.getData(), Integer.class);
                payment.setDocTypeId(doctypeId);
                payment.setCurrencyId(13);
                payment.setPaymentDate(paymentDate);

            }


            if (!paymentDto.getPaymentStatus().equals("VOD")) {
                if (paymentDto.getUserGroup().equals("CUS")) {
                    payment.setCustomerId(paymentDto.getResponsibleUserId());
                } else if (paymentDto.getUserGroup().equals("VEN")) {
                    payment.setVendorId(paymentDto.getResponsibleUserId());
                } else if (paymentDto.getUserGroup().equals("EMP")) {
                    payment.setUserId(paymentDto.getResponsibleUserId());
                } else if (paymentDto.getUserGroup().equals("OTH")) {
                    payment.setUserOtherId(paymentDto.getResponsibleUserId());
                }
            }


            payment = this.paymentRepository.save(payment);


            //Check if payment is from order
            if(paymentDto.getPosOrderId() != null){
                if( paymentDto.getIsOriginal() == null ) {
                    createPosPayment(paymentDto, payment.getId());
                    payment.setInvoiceId(getInvoiceId(paymentDto.getPosOrderId()));
                    payment = this.paymentRepository.save(payment);
                }
            }
            //Update debit of customer
            if (paymentDto.getPaymentStatus().equals("COM")
                    && paymentDto.getUserGroup().equals("CUS")
                    && paymentDto.getDocType().equals("ARP")
            ) {
                updateCusDebit(payment.getCustomerId(), payment.getPaymentAmount().negate());
            }

            paymentDto.setId(payment.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }

        try { // Send Kafka save Audit for user

            String paymentTypeName;
            if (paymentDto.getDocType().equals("ARP")) {
                paymentTypeName = "Phiếu thu";
            } else if (paymentDto.getDocType().equals("APP")) {
                paymentTypeName = "Phiếu chi";
            } else {
                paymentTypeName = "Phiếu thu/chi";
            }
            Map<String, String> productInfo = Map.of(
                    "name", paymentTypeName,
                    "document_no", payment.getDocumentNo()
            );
            String productInfoJson = objectMapper.writeValueAsString(productInfo);

            // Send Kafka save AuditUser for Save Payment
            kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                    AuditContext.getAuditInfo().getMainTenantId(),
                    AuditContext.getAuditInfo().getOrgId(),
                    serviceName,
                    DbMetadataHelper.getTableName(payment),
                    payment.getId(),
                    insertOrUpdate,
                    AuditContext.getAuditInfo().getUserId(),
                    productInfoJson);

        } catch (Exception e) {
            log.info("Error: PaymentServiceImpl: save(): send Kafka save AuditLogUser: {}", e.getMessage());
            throw new PosException("Error: PaymentServiceImpl: save(): send Kafka save AuditLogUser"); // thêm messageSource
        }

        GlobalReponse response = new GlobalReponse();
        response.setData(paymentDto);
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        log.info("Payment saved successfully with ID: {}", payment.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponsePagination getPayments(PaymentReqDto dto) {

        log.info("*** Payment, service; fetch all Payment ***");

        StringBuilder sql = new StringBuilder(
                "SELECT d_payment_id, d_org_id, " +
                        "org_name, " +
                        "TO_CHAR(payment_date, 'YYYY-MM-DD HH24:MI:SS') as payment_date," +
                        "payment_amount," +
                        "document_no," +
                        "created_by, " +
                        "user_name," +
                        "user_full_name," +
                        "user_phone," +
                        "description," +
                        "reference_no," +
                        "user_group," +
                        "user_group_name," +
                        "d_vendor_id," +
                        "vendor_name," +
                        "vendor_phone," +
                        "d_customer_id," +
                        "customer_name," +
                        "customer_phone," +
                        "d_user_id, " +
                        "emp_user_name," +
                        "emp_full_name," +
                        "emp_phone," +
                        "payment_method," +
                        "payment_method_name," +
                        "payment_status," +
                        "payment_status_name," +
                        "d_doctype_id," +
                        "doctype_name," +
                        "doctype_code," +
                        "doc_base_type," +
                        "is_active," +
                        "d_user_other_id," +
                        "user_other_name,user_other_phone,d_bankaccount_id,bank_account_name " +
                        " FROM pos.d_payment_v  WHERE d_tenant_id = :tenantId "


        );

        buildPaymentSqlCondition(dto, sql);

        sql.append(" ORDER BY payment_date desc");
        sql.append(" LIMIT :limit OFFSET :offset ");

        Query paymentQuery = entityManager.createNativeQuery(sql.toString());
        paymentQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        setPaymentQueryParam(dto, paymentQuery);

        paymentQuery.setParameter("limit", dto.getPageSize());
        paymentQuery.setParameter("offset", dto.getPage() * dto.getPageSize());


        // Fetch the paginated results
        List<Map<String, Object>> results = paymentQuery.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PaymentRespDto> list = new ArrayList<>();

        for (Map<String, Object> result : results) {
            PaymentRespDto item = PaymentRespDto.builder()
                    .id(ParseHelper.INT.parse(result.get("d_payment_id")))
                    .documentNo(ParseHelper.STRING.parse(result.get("document_no")))
                    .paymentDate(ParseHelper.STRING.parse(result.get("payment_date")))
                    .paymentAmount(ParseHelper.BIGDECIMAL.parse(result.get("payment_amount")))
                    .description(ParseHelper.STRING.parse(result.get("description")))
                    .referenceNo(ParseHelper.STRING.parse(result.get("reference_no")))
                    .userGroup(ParseHelper.STRING.parse(result.get("user_group")))
                    .userGroupName(ParseHelper.STRING.parse(result.get("user_group_name")))
                    .paymentMethod(ParseHelper.STRING.parse(result.get("payment_method")))
                    .paymentMethodName(ParseHelper.STRING.parse(result.get("payment_method_name")))
                    .docStatus(ParseHelper.STRING.parse(result.get("payment_status")))
                    .docStatusName(ParseHelper.STRING.parse(result.get("payment_status_name")))
                    .doctypeCode(ParseHelper.STRING.parse(result.get("doctype_code")))
                    .docTypeName(ParseHelper.STRING.parse(result.get("doctype_name")))
                    .org(OrgDto.builder().id(ParseHelper.INT.parse(result.get("d_org_id")))
                            .name(ParseHelper.STRING.parse(result.get("org_name")))
                            .build())
                    .bankAccount(BankAccountDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_bankaccount_id")))
                            .name(ParseHelper.STRING.parse(result.get("bank_account_name")))
                            .build())
                    .user(UserDto.builder()
                            .userId(ParseHelper.INT.parse(result.get("d_user_id")))
                            .userName(ParseHelper.STRING.parse(result.get("user_name")))
                            .fullName(ParseHelper.STRING.parse(result.get("user_full_name")))
                            .phone(ParseHelper.STRING.parse(result.get("user_phone")))
                            .build())

                    .build();
            VariousUserDto responsibleUser = null;
            if (item.getUserGroup().equals("CUS")) {
                responsibleUser = VariousUserDto.builder()
                        .id(ParseHelper.INT.parse(result.get("d_customer_id")))
                        .name(ParseHelper.STRING.parse(result.get("customer_name")))
                        .phone(ParseHelper.STRING.parse(result.get("customer_phone")))
                        .userGroup(item.getUserGroup())
                        .build();
            } else if (item.getUserGroup().equals("VEN")) {
                responsibleUser = VariousUserDto.builder()
                        .id(ParseHelper.INT.parse(result.get("d_vendor_id")))
                        .name(ParseHelper.STRING.parse(result.get("vendor_name")))
                        .phone(ParseHelper.STRING.parse(result.get("vendor_phone")))
                        .userGroup(item.getUserGroup())
                        .build();
            } else if (item.getUserGroup().equals("EMP")) {
                responsibleUser = VariousUserDto.builder()
                        .id(ParseHelper.INT.parse(result.get("d_user_id")))
                        .name(ParseHelper.STRING.parse(result.get("emp_full_name")))
                        .phone(ParseHelper.STRING.parse(result.get("emp_phone")))
                        .userGroup(item.getUserGroup())
                        .build();
            } else if (item.getUserGroup().equals("OTH")) {
                responsibleUser = VariousUserDto.builder()
                        .id(ParseHelper.INT.parse(result.get("d_user_other_id")))
                        .name(ParseHelper.STRING.parse(result.get("user_other_name")))
                        .phone(ParseHelper.STRING.parse(result.get("user_other_phone")))
                        .userGroup(item.getUserGroup())
                        .build();
            }
            item.setResponsibleUser(responsibleUser);
            list.add(item);
        }


        StringBuilder countSql = new StringBuilder("SELECT count(1) FROM pos.d_payment_v  WHERE d_tenant_id = :tenantId "
        );

        buildPaymentSqlCondition(dto, countSql);


        // Get the total number of records
        Query totalRecordsQuery = entityManager.createNativeQuery(countSql.toString());
        totalRecordsQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        setPaymentQueryParam(dto, totalRecordsQuery);

        Long totalRecords = ((Number) totalRecordsQuery.getSingleResult()).longValue();

        return GlobalReponsePagination.builder()
                .data(list)
                .pageSize(dto.getPageSize())
                .currentPage(dto.getPage())
                .totalItems(totalRecords)
                .totalPages((int) Math.ceil((double) totalRecords / dto.getPageSize()))
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    private void buildPaymentSqlCondition(PaymentReqDto dto, StringBuilder sql) {
        if (dto.getDocType() != null) {
            if (dto.getDocType().equals("ARP")) {
                sql.append(" and doctype_code = 'AR_PAYMENT' ");
            } else {
                sql.append(" and doctype_code = 'AP_PAYMENT' ");
            }
        }

        if (dto.getOrgId() != null) {
            sql.append(" and d_org_id = :orgId ");
        }

        if (dto.getDocumentNo() != null) {
            sql.append(" and lower(document_no) like lower( '%' || :documentNo  || '%' ) ");
        }

        if (dto.getDateFrom() != null) {
            sql.append(" and DATE(payment_date) >= :dateFrom ");
        }

        if (dto.getDateTo() != null) {
            sql.append(" and DATE(payment_date) <= :dateTo ");
        }

        if (dto.getPaymentMethod() != null) {
            if ("OTH".equals(dto.getPaymentMethod())) {
                sql.append(" and payment_method not in ('CAS', 'BAN') ");
            } else {
                sql.append(" and payment_method = :paymentMethod ");
            }
        }

        if (dto.getDocStatus() != null) {
            sql.append(" and payment_status = :paymentStatus ");
        }

        if (dto.getUserSearchKey() != null) {
            sql.append(" and (lower(user_full_name) like lower( '%' || :userSearchKey  || '%' ) "
                    + "    or user_phone like  '%' || :userSearchKey  || '%'   "
                    + "    or lower(vendor_name) like lower( '%' || :userSearchKey  || '%' )  "
                    + "    or vendor_phone like  '%' || :userSearchKey  || '%'   "
                    + "    or lower(customer_name) like lower( '%' || :userSearchKey  || '%' )  "
                    + "    or customer_phone like '%' || :userSearchKey  || '%'   "
                    + "    or lower(emp_full_name) like lower( '%' || :userSearchKey  || '%' )  "
                    + "    or emp_phone like '%' || :userSearchKey  || '%' )  ");
        }
    }

    private void setPaymentQueryParam(PaymentReqDto dto, Query query) {
        if (dto.getOrgId() != null) {
            query.setParameter("orgId", dto.getOrgId());
        }

        if (dto.getDocumentNo() != null) {
            query.setParameter("documentNo", dto.getDocumentNo());
        }

        if (dto.getDateFrom() != null) {
            query.setParameter("dateFrom", DateHelper.toLocalDate(dto.getDateFrom()));
        }
        if (dto.getDateTo() != null) {
            query.setParameter("dateTo", DateHelper.toLocalDate(dto.getDateTo()));
        }

        if (dto.getPaymentMethod() != null && !"OTH".equals(dto.getPaymentMethod())) {
            query.setParameter("paymentMethod", dto.getPaymentMethod());
        }

        if (dto.getDocStatus() != null) {
            query.setParameter("paymentStatus", dto.getDocStatus());
        }

        if (dto.getUserSearchKey() != null) {
            query.setParameter("userSearchKey", dto.getUserSearchKey());
        }
    }

    @Override
    public GlobalReponse getMethodForPayment() {
        String sql = " select d_payment_type_id, code,payment_name from pos.d_payment_type " +
                "  where code in ('CAS','BAN','VIS') order by sequence_no asc ";
        List<PaymentMethodDto> resultDto = new ArrayList<>();

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        for (Map<String, Object> row : results) {
            PaymentMethodDto item = PaymentMethodDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_payment_type_id")))
                    .paymentName(ParseHelper.STRING.parse(row.get("payment_name")))
                    .code(ParseHelper.STRING.parse(row.get("code")))
                    .build();
            resultDto.add(item);
        }
        GlobalReponse response = new GlobalReponse();
//        List<ReferenceGetV> entity = referenceGetVRepository.findByNameReference(nameReference);
        response.setData(resultDto);
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }

    @Override
    public GlobalReponse getPaymentSummary(ReportReqDto rq) {


        PaymentSummaryDto report = null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if (rq.getPayMethodValue() == null)
            rq.setPayMethodValue("CAS");


        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_payment_summary(?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getPayMethodOrAll());
            callableStatement.setString(5, rq.getPayMethodValue());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = PaymentSummaryDto.builder()
                        .startAmount(rs.getBigDecimal("start_amount"))
                        .endAmount(rs.getBigDecimal("end_amount"))
                        .totalArAmount(rs.getBigDecimal("total_ar"))
                        .totalApAmount(rs.getBigDecimal("total_ap"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }

        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
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

    public String checkExistedDocumentNo(String documentNo) {
        String sql = "SELECT count(1) FROM pos.d_payment WHERE document_no = :documentNo";
        Query query = entityManager.createNativeQuery(sql);
        query = query.setParameter("documentNo", documentNo);
        Long count = ((Number) query.getSingleResult()).longValue();
        if (count > 0) {
            return "FAI";
        }
        return "COM";
    }

    public Integer createPosPayment(PaymentDto paymentDto,Integer paymentId) {
        Integer posPaymentId = null;
        //PosPayment
        PosPayment posPayment = new PosPayment();
        posPayment.setTenantId(AuditContext.getAuditInfo().getTenantId());
        posPayment.setOrgId(paymentDto.getOrgId());
        posPayment.setPosOrderId(paymentDto.getPosOrderId());
        posPayment.setPaymentMethod(paymentDto.getPaymentMethod());
        posPayment.setTransactionId(paymentDto.getTransactionId());
        if (paymentDto.getCode() != null) {
            posPayment.setVoucherCode(paymentDto.getCode());
        }
        posPayment.setTotalAmount(paymentDto.getPaymentAmount());
        posPayment.setIsProcessed("Y");
        posPayment.setPaymentId(paymentId);
        posPayment = posPaymentRepository.save(posPayment);
        posPaymentId = posPayment.getId();
        return posPaymentId;
    }

    public Integer getInvoiceId(Integer posOrderId) {

        String sql1 = "select d_invoice_id from pos.d_invoice where 1 = 1 " +
                " and d_pos_order_id = :posOrderId";

        List<?> resultList = entityManager.createNativeQuery(sql1)
                .setParameter("posOrderId", posOrderId)
                .getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        return  ((Number) resultList.get(0)).intValue();

    }




//	@Override
//	public List<PaymentDto> findAll() {
//		log.info("*** PaymentDto List, service; fetch all payments *");
//		return this.paymentRepository.findAll()
//				.stream()
//					.map(PaymentMappingHelper::map)
//					.map(p -> {
//						p.setOrderDto(this.restTemplate.getForObject(AppConstant.DiscoveredDomainsApi
//								.ORDER_SERVICE_API_URL + "/" + p.getOrderDto().getOrderId(), OrderDto.class));
//						return p;
//					})
//					.distinct()
//					.collect(Collectors.toUnmodifiableList());
//	}
//
//	@Override
//	public PaymentDto findById(final Integer paymentId) {
//		log.info("*** PaymentDto, service; fetch payment by id *");
//		return this.paymentRepository.findById(paymentId)
//				.map(PaymentMappingHelper::map)
//				.map(p -> {
//					p.setOrderDto(this.restTemplate.getForObject(AppConstant.DiscoveredDomainsApi
//							.ORDER_SERVICE_API_URL + "/" + p.getOrderDto().getOrderId(), OrderDto.class));
//					return p;
//				})
//				.orElseThrow(() -> new PaymentNotFoundException(String.format("Payment with id: %d not found", paymentId)));
//	}


}









