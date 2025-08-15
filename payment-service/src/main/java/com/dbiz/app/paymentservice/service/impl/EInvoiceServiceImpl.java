package com.dbiz.app.paymentservice.service.impl;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.EInvoiceOrg;
import com.dbiz.app.paymentservice.repository.EInvoiceOrgRepository;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

import com.dbiz.app.tenantservice.service.CommonService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.E;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.paymentDto.CreateEInvoiceOrgDto;
import org.common.dbiz.dto.paymentDto.InvoiceDto;
import org.common.dbiz.dto.paymentDto.InvoiceViewDto;
import org.common.dbiz.dto.paymentDto.einvoice.IssueEInvoiceDto;
import org.common.dbiz.dto.paymentDto.einvoice.*;
import org.common.dbiz.dto.paymentDto.request.EInvoiceSetUpReqDto;
import org.common.dbiz.dto.paymentDto.response.EInvoiceSetUpResDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import com.dbiz.app.paymentservice.service.EInvoiceService;
import com.dbiz.app.paymentservice.domain.Invoice;
import com.dbiz.app.paymentservice.domain.InvoiceLine;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.MoneyToWordsHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.CreateEInvoiceRequest;
import com.dbiz.app.paymentservice.repository.InvoiceLineRepository;
import com.dbiz.app.paymentservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EInvoiceServiceImpl implements EInvoiceService {
    private final RequestParamsUtils requestParamsUtils;
//    private final EInvoiceRepository eInvoiceRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceLineRepository invoiceLineRepository;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;
    private final EInvoiceOrgRepository eInvoiceOrgRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;
    private final QueryEngine queryEngine;
    private final ObjectMapper objectMapper;
    private final String GROUP_ID = "einvoice";
    private final String ISSUE_EINVOICE_TOPIC = "ISSUE_EINVOICE_TOPIC";
    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final CommonService commonService;


    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public GlobalReponse createEInvoice(CreateEInvoiceRequest request) {
        GlobalReponse response = new GlobalReponse();

        Invoice invoice = this.invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() ->
                new ObjectNotFoundException(messageSource.getMessage("note_notFound", null, LocaleContextHolder.getLocale())));
        List<InvoiceLine> invoiceLines = this.invoiceLineRepository.findAllByInvoiceId(request.getInvoiceId());

        String xmlData = "<Invoices>" +
                "<Inv>" +
                "<Invoice>" +
                "<Fkey>ns433235</Fkey>" +
                "<Buyer>Khách mua hàng</Buyer>" +
                "<PaymentMethod>TM</PaymentMethod>" +
                "<Products>" +
                "<Product>" +
                "<Code>380038</Code>" +
                "<ProdName>Hàng hóa</ProdName>" +
                "<ProdUnit>Vé</ProdUnit>" +
                "<ProdQuantity>1</ProdQuantity>" +
                "<ProdPrice>7000</ProdPrice>" +
                "<Amount>7000</Amount>" +
                "</Product>" +
                "</Products>" +
                "<Total>7000</Total>" +
                "<Amount>7000</Amount>" +
                "<AmountInWords>Bảy nghìn đồng</AmountInWords>" +
                "<ArisingDate>24/07/2023</ArisingDate>" +
                "<Note/>" +
                "<Currency>VND</Currency>" +
                "<ExchangeRate>1</ExchangeRate>" +
                "</Invoice>" +
                "</Inv>" +
                "</Invoices>";

        // Create payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("xmlData", xmlData);
        payload.put("pattern", "1");
        payload.put("serial", "C23MRG");
        payload.put("convert", false);

        // Convert payload to JSON
//        JSONObject json = new JSONObject(payload);
//        String jsonBody = json.toString();

        String jsonBody = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonBody = objectMapper.writeValueAsString(payload);
            log.info("JSON body: {}", jsonBody);
        } catch (JsonProcessingException e) {
            log.error("Không thể chuyển đổi payload thành JSON", e);
            throw new RuntimeException("Lỗi khi convert JSON", e); // hoặc xử lý theo logic của bạn
        }

        // Create the request
        HttpRequest requestAPI = HttpRequest.newBuilder()
                .uri(URI.create("https://uat78.hilo.com.vn/api/hoadon/xuathoadon"))
                .header("Content-Type", "application/json")
                .header("Authentication", "cm9vdDpWcDdsc05mQ3BVJGZsSi4sXnlEcDowODhhZGEzNWFiMjM0YTJhYjYxNWYwMTliZjIwNjhkOQ==")
                .header("taxcode", "0106713804-999")
                .timeout(Duration.ofMinutes(2))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send the request and handle the response
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> apiResponse = client.send(requestAPI, HttpResponse.BodyHandlers.ofString());

            if (apiResponse.statusCode() == 200) {
                response.setData(apiResponse.body());
                response.setMessage("Invoice created successfully");
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setData(apiResponse.body());
                response.setMessage("Failed to create invoice, status code: " + apiResponse.statusCode());
                response.setStatus(apiResponse.statusCode());
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

//
//        response.setData(null);
//        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
//        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse createEInvoiceSetupByOrg(CreateEInvoiceOrgDto rq) {

        log.info("createEInvoiceSetupByOrg rq: {}", rq);

        GlobalReponse response = new GlobalReponse();
        if(rq.getId() == null){

            EInvoiceOrg eInvoiceOrg = modelMapper.map(rq, EInvoiceOrg.class);
            eInvoiceOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
            eInvoiceOrg = eInvoiceOrgRepository.save(eInvoiceOrg);
            rq.setId(eInvoiceOrg.getId());
            response.setStatus(HttpStatus.CREATED.value());

        }else{

            Optional<EInvoiceOrg> eInvoiceOrgOptional = eInvoiceOrgRepository.findById(rq.getId());
            if(eInvoiceOrgOptional.isPresent()) {
                EInvoiceOrg eInvoiceOrg = eInvoiceOrgOptional.get();
                modelMapper.map(rq, eInvoiceOrg);
                eInvoiceOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
                eInvoiceOrgRepository.save(eInvoiceOrg);
                response.setStatus(HttpStatus.OK.value());
            }else{
                throw new PosException(messageSource.getMessage("setup.up.not.found", null, LocaleContextHolder.getLocale()));
            }
        }
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setData(rq);
        return response;
    }

    @Override
    public GlobalReponse getEInvoiceOrgBySetUpIdAndOrgId(CreateEInvoiceOrgDto rq) {

        log.info("getEInvoiceOrgBySetUpIdAndOrgId rq: {}", rq);

        CreateEInvoiceOrgDto result = null;
        try {
            String sql = "SELECT " +
                    " *  " +
                    " FROM pos.d_einvoice_org WHERE 1 = 1 " +
                    " AND d_einvoice_setup_id = :setUpId and d_org_id = :orgId and is_active = 'Y'";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("setUpId", rq.getEinvoiceSetupId())
                    .setParameter("orgId", rq.getOrgId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                result = new CreateEInvoiceOrgDto();
                result.setId(ParseHelper.INT.parse(row.get("d_einvoice_org_id")));
                result.setEinvoiceSetupId(ParseHelper.INT.parse(row.get("d_einvoice_setup_id")));
                result.setOrgId(ParseHelper.INT.parse(row.get("d_org_id")));
                result.setTaxId(ParseHelper.STRING.parse(row.get("taxid")));
                result.setPassword(ParseHelper.STRING.parse(row.get("password")));
                result.setName(ParseHelper.STRING.parse(row.get("name")));
                result.setDescription(ParseHelper.STRING.parse(row.get("description")));
                result.setIsDirectIssue(ParseHelper.STRING.parse(row.get("is_direct_issue")));
                result.setIsAttachNote(ParseHelper.STRING.parse(row.get("is_attach_note")));
                result.setInvoiceForm(ParseHelper.STRING.parse(row.get("invoice_form")));
                result.setInvoiceSign(ParseHelper.STRING.parse(row.get("invoice_sign")));
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(result)
                .build();
    }

    @Override
    public GlobalReponsePagination getEInvoiceSetUp(EInvoiceSetUpReqDto rq) {

        try {
            Parameter parameter = new Parameter();
            parameter.add("value", rq.getValue(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
            parameter.add("name", rq.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
            parameter.add("einvoice_partner", rq.getEinvoicePartner(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
            parameter.add("is_active", "Y", Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

            ResultSet rs = queryEngine.getRecords("pos.d_einvoice_setup", parameter, rq);
            List<EInvoiceSetUpResDto> data = new ArrayList<>();
            while (rs.next()) {
                EInvoiceSetUpResDto eInvoiceSetUpResDto = EInvoiceSetUpResDto.builder()
                        .id(rs.getInt("d_einvoice_setup_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .value(rs.getString("value"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .einvoicePartner(rs.getString("einvoice_partner"))
                        .isActive(rs.getString("is_active"))
                        .isDefault(rs.getString("is_default"))
                        .build();
                data.add(eInvoiceSetUpResDto);
            }

            Pagination pagination = queryEngine.getPagination("pos.d_einvoice_setup", parameter, rq);
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
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new PosException(e.getMessage());
        }
    }

    @Override
    public GlobalReponse issueEInvoice(IssueEInvoiceDto rq) {

        log.info("issueEInvoice rq: {}", rq);

        StringBuilder sql = new StringBuilder("select value from d_einvoice_setup des where is_active = 'Y' and is_default = 'Y'");

        Query query = this.entityManager.createNativeQuery(sql.toString());

        Object value = query.getSingleResult();

        String message = "FAI";
        if (value != null) {

            if (value.equals("Hilo")) {

                 message = issueHiloInvoice(rq);
            } else if (value.equals("Viettel Telecom")) {

                 message = issueInvoice(rq);
            } else {

                throw new PosException(messageSource.getMessage("setup.not.configured", null, LocaleContextHolder.getLocale()));
            }
        }

        if(message.equals("FAI")){

            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(getEInvoiceReqDto(rq))
                .build();
    }

    @KafkaListener(groupId = GROUP_ID, topics = ISSUE_EINVOICE_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void issueEInvoiceKafka(ConsumerRecord<String, IssueEInvoiceDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        IssueEInvoiceDto dto = consumerRecord.value();
        try {
            log.info("Topic: " + ISSUE_EINVOICE_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message: " + objectMapper.writeValueAsString(dto));
            acknowledgment.acknowledge();
            changeTenantAndLocale(dto);
            issueInvoice(dto);
        } catch (Exception e) {
            log.error("Caught error in updateImportFile(): ", e);
        }

    }

    public String issueInvoice(IssueEInvoiceDto rq){

        log.info("getNapasToken");
        ViettelAuthDto viettelAuthDto = getViettelAuth(rq);
        if(viettelAuthDto.getTaxCode() == null || viettelAuthDto.getPassword() == null){
            throw new PosException(messageSource.getMessage("setup.not.configured", null, LocaleContextHolder.getLocale()));
        }

        String url = getViettelUrl(viettelAuthDto.getTaxCode());
        EInvoiceReqDto eInvoiceReqDto = getEInvoiceReqDto(rq);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(viettelAuthDto.getTaxCode(),viettelAuthDto.getPassword());
        try{
            log.info("payload: " + objectMapper.writeValueAsString(eInvoiceReqDto));
            ResponseEntity<EInvoiceResDto> rsp = null;
            HttpEntity<EInvoiceReqDto> requestEntity = new HttpEntity<>(eInvoiceReqDto, headers);
            log.info("url: " + url);
            rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, EInvoiceResDto.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                log.info("Issue Success");
                EInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();

                String sql = "SELECT " +
                        " name, value  " +
                        " FROM pos.d_config WHERE 1 = 1 " +
                        " AND name = :param1 " ;

                String searchLink = null;

                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                        .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_VIETTEL_RESCUE_URL)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();

                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);
                    if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_VIETTEL_RESCUE_URL))
                        searchLink = ParseHelper.STRING.parse(row.get("value"));
                }

                log.info("Response: " + objectMapper.writeValueAsString(eInvoiceReqDtoResponse));
                EInvoiceResultDto eInvoiceResultDto = objectMapper.convertValue(eInvoiceReqDtoResponse.getResult(), EInvoiceResultDto.class);
                updateInvoice(rq.getInvoiceId(),
                        eInvoiceReqDto.getGeneralInvoiceInfo().getInvoiceSeries(),
                        eInvoiceReqDto.getGeneralInvoiceInfo().getTemplateCode()
                        ,eInvoiceResultDto.getSupplierTaxCode(),
                        eInvoiceResultDto.getInvoiceNo(),
                        eInvoiceResultDto.getTransactionID(),
                        eInvoiceResultDto.getReservationCode(),
                        eInvoiceResultDto.getCodeOfTax(),
                        searchLink,
                        eInvoiceResultDto.getReservationCode(),
                        null,
                        DateHelper.toInstantNowUTC(),"ISD",null);
                return "ISD";
            }else{
                log.info("Issue Failed");
                EInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                updateInvoice(rq.getInvoiceId(),
                        eInvoiceReqDto.getGeneralInvoiceInfo().getInvoiceSeries(),
                        eInvoiceReqDto.getGeneralInvoiceInfo().getTemplateCode()
                        ,null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null, null, "FAI",eInvoiceReqDtoResponse.getMessage());
            }
        } catch (HttpClientErrorException e) {

            String body = e.getResponseBodyAsString();
            System.out.println("Raw response body: " + body);

            try {
                // parse JSON để lấy "Message"
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(body);

                String message = "";
                if (jsonNode.has("detail")) {

                    message = jsonNode.get("detail").asText();
                }

                log.info("Issue Failed");
                updateInvoice(rq.getInvoiceId(),
                        eInvoiceReqDto.getGeneralInvoiceInfo().getInvoiceSeries(),
                        eInvoiceReqDto.getGeneralInvoiceInfo().getTemplateCode(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null, null, "FAI", message);
            } catch (Exception parseEx) {

                e.printStackTrace();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "FAI";
    }

    public void updateInvoice(Integer invoiceId,
                              String invoiceSign,
                              String invoiceForm,
                              String supplierTaxCode,
                              String invoiceNo,
                              String transactionID,
                              String reservationCode,
                              String codeOfTax,
                              String searchLink,
                              String searchCode,
                              Integer referenceId,
                              Instant issuedDate, String status, String message) {

        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if (invoice.isPresent()) {
            Invoice invoiceUpdate = invoice.get();
            invoiceUpdate.setInvoiceSign(invoiceSign);
            invoiceUpdate.setInvoiceForm(invoiceForm);
            invoiceUpdate.setEinvoiceStatus(status);
            invoiceUpdate.setEinvoiceSupplierTaxcode(supplierTaxCode);
            invoiceUpdate.setInvoiceNo(invoiceNo);
            invoiceUpdate.setEinvoiceTransactionId(transactionID);
            invoiceUpdate.setEinvoiceReservationCode(reservationCode);
            invoiceUpdate.setEinvoiceCodeOfTax(codeOfTax);
            invoiceUpdate.setInvoiceError(message);
            invoiceUpdate.setIssuedDate(issuedDate);
            invoiceUpdate.setReferenceInvoiceId(referenceId);
            invoiceUpdate.setSearchLink(searchLink);
            invoiceUpdate.setSearchCode(searchCode);
            invoiceRepository.save(invoiceUpdate);
        } else {
            log.error("Update after EInvoice => Invoice not found with id: {}", invoiceId);
        }
    }


    public ViettelAuthDto getViettelAuth(IssueEInvoiceDto rq) {

        log.info("Get Viettel Auth");

        ViettelAuthDto result = new ViettelAuthDto();

        try {
            String sql = "SELECT " +
                    " DEG.taxid , DEG.password  " +
                    " FROM pos.d_einvoice_setup DES " +
                    " INNER JOIN pos.d_einvoice_org DEG on DES.d_einvoice_setup_id = DEG.d_einvoice_setup_id " +
                    " INNER JOIN pos.d_invoice DI on DI.d_org_id = DEG.d_org_id " +
                    " WHERE 1 = 1 " +
                    " AND DI.d_invoice_id = :invoiceId and DEG.is_active = 'Y' and DES.is_default = 'Y'" ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                result.setTaxCode(ParseHelper.STRING.parse(row.get("taxid")));
                result.setPassword(ParseHelper.STRING.parse(row.get("password")));
            }

            return result;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }

    public HiloAuthDto getHiloAuth(Integer invoiceId) {

        log.info("Get Hilo Auth");

        HiloAuthDto result = new HiloAuthDto();

        try {
            String sql = "SELECT " +
                    " DEG.taxid, DEG.password, DEG.name, DEG.invoice_form, DEG.invoice_sign " +
                    " FROM pos.d_einvoice_setup DES " +
                    " INNER JOIN pos.d_einvoice_org DEG on DES.d_einvoice_setup_id = DEG.d_einvoice_setup_id " +
                    " INNER JOIN pos.d_invoice DI on DI.d_org_id = DEG.d_org_id " +
                    " WHERE 1 = 1 " +
                    " AND DI.d_invoice_id = :invoiceId AND DEG.is_active = 'Y' and DES.is_default = 'Y'" ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", invoiceId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                result.setTaxCode(ParseHelper.STRING.parse(row.get("taxid")));
                result.setPassword(ParseHelper.STRING.parse(row.get("password")));
                result.setUserName(ParseHelper.STRING.parse(row.get("name")));
                result.setPattern(ParseHelper.STRING.parse(row.get("invoice_form")));
                result.setSerial(ParseHelper.STRING.parse(row.get("invoice_sign")));
            }

            return result;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }

    public String getViettelUrl(String taxCode) {

        log.info("Get Viettel URL");

        String urlPre = "";
        String uriIssue= "";

        try {
            String sql = "SELECT " +
                    " name,value  " +
                    " FROM pos.d_config WHERE 1 = 1 " +
                    " AND name in (:param1,:param2) " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_VIETTEL_URL_PRE)
                    .setParameter("param2", AppConstant.EInvoice.D_EINVOICE_VIETTEL_URL_ISSUE)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_VIETTEL_URL_PRE))
                    urlPre = ParseHelper.STRING.parse(row.get("value"));

                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_VIETTEL_URL_ISSUE))
                    uriIssue = ParseHelper.STRING.parse(row.get("value"));
            }

            return urlPre + uriIssue + taxCode;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return "";
    }

    public String getHiloUrl(String suffix) {

        log.info("Get Hilo URL");

        String urlPre = "";
        String uriIssue= "";

        try {
            String sql = "SELECT " +
                    " name,value  " +
                    " FROM pos.d_config WHERE 1 = 1 " +
                    " AND name in (:param1,:param2) " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_HILO_URL_PRE)
                    .setParameter("param2", suffix)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_HILO_URL_PRE))
                    urlPre = ParseHelper.STRING.parse(row.get("value"));

                if(ParseHelper.STRING.parse(row.get("name")).equals(suffix))
                    uriIssue = ParseHelper.STRING.parse(row.get("value"));
            }

            return urlPre + uriIssue;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return "";
    }

    public EInvoiceReqDto getEInvoiceReqDto(IssueEInvoiceDto rq){

        InvoiceDto invoiceDto = getInvoiceDto(rq);
        GeneralInvoiceInfoDto generalInvoiceInfoDto = getGeneralInvoiceInfoDto(rq, invoiceDto);
        BuyerInfoDto buyerInfoDto = getBuyerInfoDto(invoiceDto);
        SellerInfoDto sellerInfoDto = getSellerInfo(rq);
        List<ItemInfoDto> itemInfoDtos = getItemInfo(rq);
        String paymentRule = getPaymentRule(rq);
//        MetadataDto metadataDto = getMetadata(invoiceDto);
        EInvoiceReqDto eInvoiceReqDto = EInvoiceReqDto
                .builder()
                .generalInvoiceInfo(generalInvoiceInfoDto)
                .buyerInfo(buyerInfoDto)
                .payments(List.of(PaymentDto.builder()
                                .paymentMethodName(paymentRule)
                        .build()))
                .sellerInfo(sellerInfoDto)
                .itemInfo(itemInfoDtos)
//                .metadata(metadataDto)
                .build();
        return eInvoiceReqDto;
    }

    public InvoiceDto getInvoiceDto(IssueEInvoiceDto rq) {

        log.info("getInvoiceDto rq: {}", rq);

        InvoiceDto result = null;
        try {
            String sql = "SELECT " +
                    "    di.d_invoice_id AS id, " +
                    "    dpo.d_pos_order_id AS d_pos_order_id, " +
                    "    di.description, " +
                    "    di.document_no, " +
                    "    dc.code, " +
                    "    COALESCE(di.buyer_name, dpo.customer_name) AS buyer_name, " +
                    "    di.buyer_tax_code, " +
                    "    di.buyer_address, " +
                    "    di.buyer_citizen_id, " +
                    "    di.buyer_passport_number," +
                    "    di.buyer_budget_unit_code, " +
                    "    COALESCE(di.buyer_phone, dpo.phone) AS buyer_phone, " +
                    "    di.buyer_email, " +
                    "    di.buyer_company, " +
                    "    di.date_invoiced, " +
                    "    UPPER(LEFT(REPLACE(CAST(di.d_invoice_uu AS TEXT), '-', ''), 12)) AS d_invoice_uu," +
                    "    dpo.price_category_code " +
                    "FROM pos.d_invoice di " +
                    "INNER JOIN pos.d_pos_order dpo ON di.d_pos_order_id = dpo.d_pos_order_id " +
                    "INNER JOIN pos.d_customer dc ON dc.d_customer_id = dpo.d_customer_id " +
                    "WHERE 1 = 1 " +
                    "AND di.d_invoice_id = :invoiceId";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                result = new InvoiceDto();
                result.setId(ParseHelper.INT.parse(row.get("id")));
                result.setPosOrderId(ParseHelper.INT.parse(row.get("d_pos_order_id")));
                result.setDescription(ParseHelper.STRING.parse(row.get("description")));
                result.setDocumentno(ParseHelper.STRING.parse(row.get("document_no")));
                result.setCumstomerCode(ParseHelper.STRING.parse(row.get("code")));
                result.setBuyerName(ParseHelper.STRING.parse(row.get("buyer_name")));
                result.setBuyerTaxCode(ParseHelper.STRING.parse(row.get("buyer_tax_code")));
                result.setBuyerAddress(ParseHelper.STRING.parse(row.get("buyer_address")));
                result.setBuyerPhone(ParseHelper.STRING.parse(row.get("buyer_phone")));
                result.setBuyerEmail(ParseHelper.STRING.parse(row.get("buyer_email")));
                result.setBuyerCitizenId(ParseHelper.STRING.parse(row.get("buyer_citizen_id")));
                result.setBuyerPassportNumber(ParseHelper.STRING.parse(row.get("buyer_passport_number")));
                result.setBuyerBudgetUnitCode(ParseHelper.STRING.parse(row.get("buyer_budget_unit_code")));
                result.setBuyerCompany(ParseHelper.STRING.parse(row.get("buyer_company")));
                result.setDInvoiceUu(ParseHelper.STRING.parse(row.get("d_invoice_uu")));
                result.setDateInvoicedTimestamp(Timestamp.valueOf(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("date_invoiced")))));
                result.setPriceCateCode(ParseHelper.STRING.parse(row.get("price_category_code")));

            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    public GeneralInvoiceInfoDto getGeneralInvoiceInfoDto(IssueEInvoiceDto rq,InvoiceDto invoiceDto) {

        log.info("getEInvoiceOrgBySetUpIdAndOrgId rq: {}", rq);

        GeneralInvoiceInfoDto result = null;
        String invoiceForm = null;
        String invoiceSign = null;
        try {
            String sql = "SELECT " +
                    " name,value  " +
                    " FROM pos.d_config WHERE 1 = 1 " +
                    " AND name in (:param1,:param2) " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_VIETTEL_FORM)
                    .setParameter("param2", AppConstant.EInvoice.D_EINVOICE_VIETTEL_SIGN)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_VIETTEL_FORM))
                    invoiceForm = ParseHelper.STRING.parse(row.get("value"));

                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_VIETTEL_SIGN))
                    invoiceSign = ParseHelper.STRING.parse(row.get("value"));
            }

            String invoiceType = invoiceForm.substring(0,1);
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            result = new GeneralInvoiceInfoDto();
            result.setInvoiceType(invoiceType);
            result.setTemplateCode(invoiceForm);
            result.setInvoiceSeries(invoiceSign);
            result.setCurrencyCode("VND");
            result.setAdjustmentType("1");
            result.setPaymentStatus(true);
            result.setCusGetInvoiceRight(String.valueOf(true));
            result.setInvoiceIssuedDate(invoiceDto.getDateInvoicedTimestamp().getTime());
            result.setAdditionalReferenceDesc(invoiceDto.getDescription());
            result.setTransactionUuid(uuidString);

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }


    public BuyerInfoDto getBuyerInfoDto(InvoiceDto invoiceDto) {

        log.info("getBuyerInfoDto rq: {}", invoiceDto);

        BuyerInfoDto result = null;
        try {
            result = new BuyerInfoDto();
            result.setBuyerName(invoiceDto.getBuyerName());
            result.setBuyerLegalName(invoiceDto.getBuyerCompany());
            result.setBuyerTaxCode(invoiceDto.getBuyerTaxCode());
            result.setBuyerAddressLine(invoiceDto.getBuyerAddress());
            result.setBuyerPhoneNumber(invoiceDto.getBuyerPhone());
            result.setBuyerEmail(invoiceDto.getBuyerEmail());

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }


    public SellerInfoDto getSellerInfo(IssueEInvoiceDto rq) {

        log.info("getInvoiceDto rq: {}", rq);

        SellerInfoDto result = null;
        try {
            String sql = "SELECT " +
                    " DOG.name, DOG.tax_code, DOG.address, DOG.phone  " +
                    " FROM pos.d_invoice DI " +
                    " INNER JOIN pos.d_org DOG on DOG.d_org_id = DI.d_org_id " +
                    " WHERE 1 = 1 " +
                    " AND d_invoice_id = :invoiceId " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                result = new SellerInfoDto();
                result.setSellerLegalName(ParseHelper.STRING.parse(row.get("name")));
                result.setSellerTaxCode(ParseHelper.STRING.parse(row.get("tax_code")));
                result.setSellerAddressLine(ParseHelper.STRING.parse(row.get("address")));
                result.setSellerPhoneNumber(ParseHelper.STRING.parse(row.get("phone")));
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }


    public List<ItemInfoDto> getItemInfo(IssueEInvoiceDto rq) {

        log.info("getItemInfo rq: {}", rq);

        List<ItemInfoDto> result = new ArrayList<>();
        try {
            String sql = "select  " +
                    " DP.code as product_code, " +
                    " DP.name as product_name, " +
                    " DU.code as uom_code, " +
                    " DU.name as uom_name, " +
                    " DIL.price_entered , " +
                    " DIL.qty , " +
                    " DIL.linenet_amt, " +
                    " DT.tax_rate , " +
                    " DPOL.tax_amount, " +
                    " DIL.description " +
                    "from d_invoiceline DIL  " +
                    "inner join d_pos_orderline DPOL on DIL.d_pos_orderline_id = DPOL.d_pos_orderline_id  " +
                    "inner join d_product DP on DIL.d_product_id = DP.d_product_id  " +
                    "inner join d_uom DU on DU.d_uom_id = DP.d_uom_id  " +
                    "inner join d_tax DT on DT.d_tax_id = DIL.d_tax_id  " +
                    "where DIL.d_invoice_id = :invoiceId  " +
                    "order by DIL.created asc";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            int line = 0;
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                ItemInfoDto itemInfoDto = ItemInfoDto.builder()
                        .lineNumber(line)
                        .itemCode(ParseHelper.STRING.parse(row.get("product_code")))
                        .itemName(ParseHelper.STRING.parse(row.get("product_name")))
                        .unitCode(ParseHelper.STRING.parse(row.get("uom_code")))
                        .unitName(ParseHelper.STRING.parse(row.get("uom_name")))
                        .unitPrice(ParseHelper.BIGDECIMAL.parse(row.get("price_entered")))
                        .quantity(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .itemTotalAmountWithoutTax(ParseHelper.BIGDECIMAL.parse(row.get("linenet_amt")))
                        .taxPercentage(ParseHelper.BIGDECIMAL.parse(row.get("tax_rate")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .itemNote(ParseHelper.STRING.parse(row.get("description")))
                        .build();
                line += 10;
                result.add(itemInfoDto);
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }

    public String getPaymentRule(IssueEInvoiceDto rq) {

        log.info("getInvoiceDto rq: {}", rq);

        try {
            String sql = "SELECT " +
                    " DPP.payment_method  " +
                    " FROM pos.d_invoice DI" +
                    " INNER JOIN pos.d_pos_order DPO on DI.d_pos_order_id = DPO.d_pos_order_id " +
                    " INNER JOIN pos.d_pos_payment DPP on DPO.d_pos_order_id = DPP.d_pos_order_id " +
                    " WHERE 1 = 1 " +
                    " AND d_invoice_id = :invoiceId " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            if(results != null && results.size() > 1) {
                log.info("Mixed Payment");
                return "TM/CK";
            }

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                if(ParseHelper.STRING.parse(row.get("payment_method")).equals(AppConstant.PaymentRule.CASH))
                    return "TM";

                if(ParseHelper.STRING.parse(row.get("payment_method"))
                        .equals(AppConstant.PaymentRule.BANK)
                || ParseHelper.STRING.parse(row.get("payment_method"))
                        .equals(AppConstant.PaymentRule.QRCODE_MBB)
                || ParseHelper.STRING.parse(row.get("payment_method"))
                        .equals(AppConstant.PaymentRule.QRCODE_NAPAS)
                ){
                    return "CK";
                }

            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());

        }
        return "KHAC";
    }

    public String getPaymentHiloRule(IssueEInvoiceDto rq) {

        log.info("getInvoiceDto rq: {}", rq);

        try {
            String sql = "SELECT " +
                    " DPP.payment_method  " +
                    " FROM pos.d_invoice DI" +
                    " INNER JOIN pos.d_pos_order DPO on DI.d_pos_order_id = DPO.d_pos_order_id " +
                    " INNER JOIN pos.d_pos_payment DPP on DPO.d_pos_order_id = DPP.d_pos_order_id " +
                    " WHERE 1 = 1 " +
                    " AND d_invoice_id = :invoiceId " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            if(results != null && results.size() > 1) {
                log.info("Mixed Payment");
                return "Tiền mặt /Chuyển khoản";
            }

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                if(ParseHelper.STRING.parse(row.get("payment_method")).equals(AppConstant.PaymentRule.CASH))
                    return "Tiền mặt";

                if(ParseHelper.STRING.parse(row.get("payment_method"))
                        .equals(AppConstant.PaymentRule.BANK)
                        || ParseHelper.STRING.parse(row.get("payment_method"))
                        .equals(AppConstant.PaymentRule.QRCODE_MBB)
                        || ParseHelper.STRING.parse(row.get("payment_method"))
                        .equals(AppConstant.PaymentRule.QRCODE_NAPAS)
                ){
                    return "Chuyển khoản";
                }

            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());

        }
        return "Khác";
    }


    public MetadataDto getMetadata(InvoiceDto rq) {

        log.info("getMetadata rq: {}", rq);

        MetadataDto result = MetadataDto.builder()
                .keyTag("AR")
                .stringValue(rq.getDocumentno())
                .valueType("text")
                .keyLabel("số chứng từ AR")
                .build();
        return result;
    }

    public void changeTenantAndLocale(IssueEInvoiceDto dto) {
        try {
            int tenantNumbers = getTenantNumbers();
            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
                Map<Object, Object> configuredDataSources = dataSourceConfigService
                        .configureDataSources();

                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
            }
            if (dto.getTenantId() != 0) {
                log.info("TenantId: " + dto.getTenantId());
                dataSourceContextHolder.setCurrentTenantId(Long.valueOf(dto.getTenantId()));
            } else {
                dataSourceContextHolder.setCurrentTenantId(null);
            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, "en", dto.getTenantId()));
            LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));

        } catch (Exception e) {
            log.error("Something went wrong", e);
        }
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }


    @Override
    public GlobalReponse issueHiloEInvoice(IssueEInvoiceDto request) {

        log.info("issueEInvoice rq: {}", request);
        String message = issueHiloInvoice(request);
        if(message.equals("FAI")){
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(issueHiloInvoice(request))
                .build();
    }

    public String issueHiloInvoice(IssueEInvoiceDto request) {

        GlobalReponse response = new GlobalReponse();

        Invoice invoice = this.invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() ->
                new ObjectNotFoundException(messageSource.getMessage("invoice.not.Found", null, LocaleContextHolder.getLocale())));
        List<InvoiceLine> invoiceLines = this.invoiceLineRepository.findAllByInvoiceId(request.getInvoiceId());

        HiloAuthDto hiloAuthDtoDto = getHiloAuth(request.getInvoiceId());
        if (hiloAuthDtoDto.getTaxCode() == null || hiloAuthDtoDto.getPassword() == null || hiloAuthDtoDto.getUserName() == null || hiloAuthDtoDto.getPattern() == null || hiloAuthDtoDto.getSerial() == null) {
            throw new PosException(messageSource.getMessage("setup.not.configured", null, LocaleContextHolder.getLocale()));
        }

        HiloEInvoiceReqDto hiloEInvoiceReqDto = getHiloEInvoiceReqDto(request, hiloAuthDtoDto.getPattern(), hiloAuthDtoDto.getSerial());
        String xmlData = null;

        try {

            xmlData = convertToXml(hiloEInvoiceReqDto, "issue");
        }catch(JAXBException e){

            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

        // Create payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("xmlData", xmlData);
        payload.put("pattern", hiloAuthDtoDto.getPattern());
        payload.put("serial", hiloAuthDtoDto.getSerial());
        payload.put("convert", false);

        String jsonBody = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            jsonBody = objectMapper.writeValueAsString(payload);
            log.info("JSON body: {}", jsonBody);
        } catch (JsonProcessingException e) {

            log.error("Không thể chuyển đổi payload thành JSON", e);
            throw new RuntimeException("Lỗi khi convert JSON", e); // hoặc xử lý theo logic của bạn
        }

        String encoding = generateAuthHeader(hiloAuthDtoDto.getUserName(), hiloAuthDtoDto.getPassword());

        // Lấy URL động
        String url = getHiloUrl(AppConstant.EInvoice.D_EINVOICE_HILO_URL_ISSUE);

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.set("Authentication", encoding);
        headers.set("taxcode", hiloAuthDtoDto.getTaxCode());

        try{

            log.info("payload: " + xmlData);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            log.info("url: " + url);
            ResponseEntity<HiloEInvoiceResDto> rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, HiloEInvoiceResDto.class);

            if (rsp.getStatusCode().value() == HttpStatus.OK.value() && rsp.getBody().getSuccess() == true) {

                log.info("Issue Success");
                HiloEInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                log.info("Response: " + objectMapper.writeValueAsString(eInvoiceReqDtoResponse));
//                HiloEInvoiceResultDto eInvoiceResultDto = objectMapper.convertValue(eInvoiceReqDtoResponse.getData(), HiloEInvoiceResultDto.class);

                Object dataObj = eInvoiceReqDtoResponse.getData();

                List<?> rawList = (List<?>) dataObj;
                Map<String, Object> rawData = (Map<String, Object>) rawList.get(0);

                HiloEInvoiceResultDto eInvoiceResultDto = new HiloEInvoiceResultDto();
                eInvoiceResultDto.setFKey((String) rawData.get("fkey"));
                eInvoiceResultDto.setNo((String) rawData.get("no"));
                eInvoiceResultDto.setPattern((String) rawData.get("pattern"));
                eInvoiceResultDto.setSerial((String) rawData.get("serial"));
                eInvoiceResultDto.setSearchKey((String) rawData.get("searchKey"));
                eInvoiceResultDto.setTaxOfCode((String) rawData.get("TaxOfCode"));

                String sql = "SELECT " +
                        " name, value  " +
                        " FROM pos.d_config WHERE 1 = 1 " +
                        " AND name = :param1 " ;

                String searchLink = null;

                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                        .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_HILO_RESCUE_URL)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();

                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);
                    if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_HILO_RESCUE_URL))
                        searchLink = ParseHelper.STRING.parse(row.get("value"));
                }

                updateInvoice(request.getInvoiceId(),
                        eInvoiceResultDto.getSerial(),
                        eInvoiceResultDto.getPattern(),
                        hiloAuthDtoDto.getTaxCode(),
                        eInvoiceResultDto.getNo(),
                        null,
                        null,
                        eInvoiceResultDto.getTaxOfCode(),
                        searchLink,
                        eInvoiceResultDto.getFKey(),
                        null,
                        DateHelper.toInstantNowUTC(), "ISD",null);
                return "ISD";
            }else {

                log.info("Issue Failed");
                HiloEInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                updateInvoice(request.getInvoiceId(),
                        hiloAuthDtoDto.getSerial(),
                        hiloAuthDtoDto.getPattern(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null, null, "FAI", eInvoiceReqDtoResponse.getMessages());
            }
        } catch (HttpClientErrorException e) {

            String body = e.getResponseBodyAsString();
            System.out.println("Raw response body: " + body);

            try {
                // parse JSON để lấy "Message"
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(body);

                String message = "";
                if (jsonNode.has("Message")) {

                    message = jsonNode.get("Message").asText();
                } else if (jsonNode.has("messages")) {

                    message = jsonNode.get("messages").asText();
                }

                log.info("Issue Failed");
                updateInvoice(request.getInvoiceId(),
                        hiloAuthDtoDto.getSerial(),
                        hiloAuthDtoDto.getPattern(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null, null, "FAI", message);
            } catch (Exception parseEx) {

                e.printStackTrace();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "FAI";
    }

    public List<HiloEInvoiceProductReqDto> getHiloProductInfo(IssueEInvoiceDto rq) {

        log.info("getItemInfo rq: {}", rq);

        List<HiloEInvoiceProductReqDto> result = new ArrayList<>();
        try {
            String sql = "select  " +
                    " DP.code as product_code, " +
                    " DP.name as product_name, " +
                    " DU.code as uom_code, " +
                    " DU.name as uom_name, " +
                    " DIL.price_entered , " +
                    " DIL.qty , " +
                    " DIL.linenet_amt, " +
                    " DT.tax_rate , " +
                    " DPOL.tax_amount, " +
                    " DIL.description, " +
                    " COALESCE(DIL.linenet_amt, 0) + COALESCE(DPOL.tax_amount, 0) AS amount, " +
                    " DPO.price_category_code " +
                    "from d_invoiceline DIL  " +
                    "inner join d_pos_orderline DPOL on DIL.d_pos_orderline_id = DPOL.d_pos_orderline_id  " +
                    "inner join d_pos_order DPO on DPO.d_pos_order_id = DPOL.d_pos_order_id  " +
                    "inner join d_product DP on DIL.d_product_id = DP.d_product_id  " +
                    "left join d_uom DU on DU.d_uom_id = DP.d_uom_id  " +
                    "inner join d_tax DT on DT.d_tax_id = DIL.d_tax_id  " +
                    "where DIL.d_invoice_id = :invoiceId  " +
                    "order by DIL.created asc";

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("invoiceId", rq.getInvoiceId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            int line = 0;
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                HiloEInvoiceProductReqDto itemInfoDto = null;
                if (!"INC".equals(ParseHelper.STRING.parse(row.get("price_category_code")))) {

                    itemInfoDto = HiloEInvoiceProductReqDto.builder()
                            .orderBy(line)
                            .code(ParseHelper.STRING.parse(row.get("product_code")))
                            .prodName(ParseHelper.STRING.parse(row.get("product_name")))
                            .prodUnit(ParseHelper.STRING.parse(row.get("uom_name")))
//                        .prodQuantity(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                            .prodQuantity(((BigDecimal) row.get("qty")))
                            .prodPrice(((BigDecimal) row.get("price_entered")))
                            .total(((BigDecimal) row.get("linenet_amt")))
                            .vatRate(((BigDecimal) row.get("tax_rate")).intValue())
                            .vatAmount(((BigDecimal) row.get("tax_amount")))
//                        .isSum(ParseHelper.BOOLEAN.parse(row.get("is_sum")))
//                        .discount(ParseHelper.BIGDECIMAL.parse(row.get("discount")))
//                        .discountAmount(ParseHelper.BIGDECIMAL.parse(row.get("discountAmount")))
//                        .amount(ParseHelper.BIGDECIMAL.parse(row.get("amount")))
                            .amount(((BigDecimal) row.get("amount")))
//                        .characteristic(ParseHelper.BIGDECIMAL.parse(row.get("characteristic")))
//                        .extra01(ParseHelper.STRING.parse(row.get("extra01")))
//                        .extra02(ParseHelper.STRING.parse(row.get("extra02")))
                            .build();
                } else {

                    itemInfoDto = HiloEInvoiceProductReqDto.builder()
                            .orderBy(line)
                            .code(ParseHelper.STRING.parse(row.get("product_code")))
                            .prodName(ParseHelper.STRING.parse(row.get("product_name")))
                            .prodUnit(ParseHelper.STRING.parse(row.get("uom_name")))
//                        .prodQuantity(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                            .prodQuantity(((BigDecimal) row.get("qty")))
                            .prodPrice(((BigDecimal) row.get("price_entered")))
                            .total(((BigDecimal) row.get("linenet_amt")))
                            .vatRate(0)
                            .vatAmount(BigDecimal.ZERO)
//                        .isSum(ParseHelper.BOOLEAN.parse(row.get("is_sum")))
//                        .discount(ParseHelper.BIGDECIMAL.parse(row.get("discount")))
//                        .discountAmount(ParseHelper.BIGDECIMAL.parse(row.get("discountAmount")))
//                        .amount(ParseHelper.BIGDECIMAL.parse(row.get("amount")))
                            .amount(((BigDecimal) row.get("linenet_amt")))
//                        .characteristic(ParseHelper.BIGDECIMAL.parse(row.get("characteristic")))
//                        .extra01(ParseHelper.STRING.parse(row.get("extra01")))
//                        .extra02(ParseHelper.STRING.parse(row.get("extra02")))
                            .build();
                }
                line  += 10;
                result.add(itemInfoDto);
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return result;
    }

    public HiloEInvoiceReqDto getHiloEInvoiceReqDto(IssueEInvoiceDto rq, String pattern, String serial) {

        InvoiceDto invoiceDto = getInvoiceDto(rq);
        GeneralInvoiceInfoDto generalInvoiceInfoDto = getGeneralInvoiceInfoDto(rq, invoiceDto);
        SellerInfoDto sellerInfoDto = getSellerInfo(rq);

        rq.setPriceCateCode(invoiceDto.getPriceCateCode());

        List<HiloEInvoiceProductReqDto> productInfoDtos = getHiloProductInfo(rq);

        BigDecimal totalProductPrice = BigDecimal.ZERO;
        for (HiloEInvoiceProductReqDto item : productInfoDtos) {

            totalProductPrice = totalProductPrice.add(item.getTotal());
        }

        String paymentRule = getPaymentHiloRule(rq);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        GlobalReponse invoiceInfoRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.INVOICE_SERVICE_API_VALUE_BY_ID_URL + "/" + invoiceDto.getId().toString(), HttpMethod.GET, entityHeader , GlobalReponse.class).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        InvoiceViewDto invoiceInfoDto = objectMapper.convertValue(invoiceInfoRes.getData(), InvoiceViewDto.class);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

//        String amountInWords = MoneyToWordsHelper.toVietnameseWords(invoiceInfoDto.getTotalAmount());

        HiloEInvoiceReqDto hiloEInvoiceReqDto = HiloEInvoiceReqDto.builder()
                .key(invoiceDto.getDInvoiceUu())
                .fkey(invoiceDto.getDocumentno())
                .invPattern(pattern)
                .invSerial(serial)
//                .type(generalInvoiceInfoDto.getInvoiceType())
                .comTaxCode(sellerInfoDto.getSellerTaxCode())
                .comName(sellerInfoDto.getSellerLegalName())
                .comAddress(sellerInfoDto.getSellerAddressLine())
                .comFax(sellerInfoDto.getSellerFaxNumber())

                .cusCode(invoiceDto.getCumstomerCode())
                .cusTaxCode(invoiceDto.getBuyerTaxCode())
                .cusName(invoiceDto.getBuyerCompany())
                .buyer(invoiceDto.getBuyerName())
                .identityCode(invoiceDto.getBuyerCitizenId())
                .passportNumber(invoiceDto.getBuyerPassportNumber())
                .budgetUnitCode(invoiceDto.getBuyerBudgetUnitCode())
                .cusAddress(invoiceDto.getBuyerAddress())
                .cusPhone(invoiceDto.getBuyerPhone())
                .cusEmail(invoiceDto.getBuyerEmail())
//                .cusBankName("Ngân hàng ABC")
//                .cusBankNo("0123456789")

                .paymentMethod(paymentRule)
                .products(productInfoDtos)

                .fees(invoiceInfoDto.getReceiptOtherAmount()) // phí
//                .discount(5.0) // chiết khấu sản phẩm %
                .discountAmount(invoiceInfoDto.getDiscountAmount()) // chiết khấu tiền

                .vatRate(null) // thuế suất
                .grossValue(null) // tiền trước thuế (đối với các trường hợp KCT, KKKTNT)
                .vatAmount(invoiceInfoDto.getTaxAmount()) // tiền thuế
                .total(totalProductPrice) // tổng tiền trước thuế
                .amount(invoiceInfoDto.getTotalAmount()) // tổng tiền sau thuế
//                .amountInWords(amountInWords) // tổng tiền bằng chữ

                .arisingDate(today) // ngày phát sinh hóa đơn
                .currency(generalInvoiceInfoDto.getCurrencyCode())
                .note(generalInvoiceInfoDto.getInvoiceNote()) // ghi chú hóa đơn

                .grossValue0(BigDecimal.ZERO) // tiền trước thuế với mức thuế suất 0%
                .vatAmount0(BigDecimal.ZERO) // tiền thuế với thuế suất 0%
                .grossValue5(BigDecimal.ZERO) // tiền trước thuế với mức thuế suất 5%
                .vatAmount5(BigDecimal.ZERO) // tiền thuế với thuế suất 5%
                .grossValue8(BigDecimal.ZERO) // tiền trước thuế với mức thuế suất 8%
                .vatAmount8(BigDecimal.ZERO) // tiền thuế với thuế suất 8%
                .grossValue10(BigDecimal.ZERO) // tiền trước thuế với mức thuế suất 10%
                .vatAmount10(BigDecimal.ZERO) // tiền thuế với thuế suất 10%

                .build();

        if ("INC".equals(invoiceDto.getPriceCateCode())) {

            hiloEInvoiceReqDto.setVatAmount(BigDecimal.ZERO);
        }
        return hiloEInvoiceReqDto;
    }

    public String generateAuthHeader(String username, String password) {
        String nonce = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String authString = username + ":" + password + ":" + nonce;
        return Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
    }

    public String convertToXml(HiloEInvoiceReqDto input, String category) throws JAXBException {

        List<XMLProductDto> productList = new ArrayList<>();
        XMLProductsDto products = new XMLProductsDto();
        XMLInvoiceDto invoice = new XMLInvoiceDto();

        if (input.getProducts() != null && !input.getProducts().isEmpty()) {

            for (HiloEInvoiceProductReqDto productInput : input.getProducts()) {

                XMLProductDto product = XMLProductDto.builder()
                        .code(productInput.getCode())
                        .prodName(productInput.getProdName())
                        .prodUnit(productInput.getProdUnit())
                        .prodQuantity(productInput.getProdQuantity())
                        .prodPrice(productInput.getProdPrice())
                        .total(productInput.getTotal())
                        .amount(productInput.getAmount())
                        .discount(productInput.getDiscount())
                        .discountAmount(productInput.getDiscountAmount())
                        .vatRate(productInput.getVatRate())
                        .vatAmount(productInput.getVatAmount())
                        .orderBy(productInput.getOrderBy())
                        .isSum(productInput.getIsSum())
                        .characteristic(productInput.getCharacteristic())
                        .extra01(productInput.getExtra01())
                        .extra02(productInput.getExtra02())
                        .build();

                productList.add(product);
            }
        }

        products.setProductList(productList);

        invoice.setInvPattern(input.getInvPattern());
        invoice.setInvSerial(input.getInvSerial());
        invoice.setType(input.getType());
        invoice.setComTaxCode(input.getComTaxCode());
        invoice.setComName(input.getComName());
        invoice.setComAddress(input.getComAddress());
        invoice.setComFax(input.getComFax());
        invoice.setCusCode(input.getCusCode());
        invoice.setBuyer(input.getBuyer());
        invoice.setIdentityCode(input.getIdentityCode());
        invoice.setPassportNumber(input.getPassportNumber());
        invoice.setBudgetUnitCode(input.getBudgetUnitCode());
        invoice.setCusName(input.getCusName());
        invoice.setCusPhone(input.getCusPhone());
        invoice.setCusAddress(input.getCusAddress());
        invoice.setCusTaxCode(input.getCusTaxCode());
        invoice.setNote(input.getNote());
        invoice.setPaymentMethod(input.getPaymentMethod());
        invoice.setArisingDate(input.getArisingDate());
        invoice.setProducts(products);
        invoice.setTotal(input.getTotal());
        invoice.setVatRate(input.getVatRate());
        invoice.setVatAmount(input.getVatAmount());
        invoice.setAmount(input.getAmount());
//        invoice.setAmountInWords(input.getAmountInWords());
        invoice.setCurrency(input.getCurrency());

        if (category.equals("issue")) {

            XMLInvoicesDto invoices = new XMLInvoicesDto();
            XMLInvDto inv = new XMLInvDto();

            inv.setKey(input.getKey());
            inv.setInvoice(invoice);
            invoices.setInvs(List.of(inv));

            JAXBContext context = JAXBContext.newInstance(XMLInvoicesDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(invoices, writer);
            return writer.toString();
        } else if (category.equals("adjust")) {

            XMLAdjustInvDto invoices = new XMLAdjustInvDto();
            invoices.setFkey(input.getKey());
            invoices.setAdjustInv(invoice);

            JAXBContext context = JAXBContext.newInstance(XMLAdjustInvDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            invoices.setType("3");
            invoices.setTypeofAdj("3");

            StringWriter writer = new StringWriter();
            marshaller.marshal(invoices, writer);
            return writer.toString();
        } else {

            XMLReplaceInvDto invoices = new XMLReplaceInvDto();
            invoices.setKey(input.getKey());
            invoices.setFkey(input.getKey());
            invoices.setReplaceInv(invoice);

            JAXBContext context = JAXBContext.newInstance(XMLReplaceInvDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(invoices, writer);
            return writer.toString();
        }
    }

    @Override
    public GlobalReponse replaceHiloEInvoice(IssueEInvoiceDto request) {

        log.info("replaceHiloInvoice rq: {}", request);
        String message = replaceHiloInvoice(request);
        if(message.equals("FAI")){
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(issueHiloInvoice(request))
                .build();
    }

    public String replaceHiloInvoice(IssueEInvoiceDto request) {

        GlobalReponse response = new GlobalReponse();

        Invoice invoice = this.invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() ->
                new ObjectNotFoundException(messageSource.getMessage("invoice.not.Found", null, LocaleContextHolder.getLocale())));
        List<InvoiceLine> invoiceLines = this.invoiceLineRepository.findAllByInvoiceId(request.getInvoiceId());

        HiloAuthDto hiloAuthDtoDto = getHiloAuth(request.getInvoiceId());
        if (hiloAuthDtoDto.getTaxCode() == null || hiloAuthDtoDto.getPassword() == null || hiloAuthDtoDto.getUserName() == null || hiloAuthDtoDto.getPattern() == null || hiloAuthDtoDto.getSerial() == null) {

            throw new PosException(messageSource.getMessage("setup.not.configured", null, LocaleContextHolder.getLocale()));
        }

        HiloEInvoiceReqDto hiloEInvoiceReqDto = getHiloEInvoiceReqDto(request, hiloAuthDtoDto.getPattern(), hiloAuthDtoDto.getSerial());
        String xmlData = null;

        try {

            xmlData = convertToXml(hiloEInvoiceReqDto, "replace");
        }catch(JAXBException e){

            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

        // Create payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("xmlData", xmlData);
        payload.put("fkey", invoice.getId());
        payload.put("pattern", hiloAuthDtoDto.getPattern());
        payload.put("serial", hiloAuthDtoDto.getSerial());
        payload.put("convert", false);

        String jsonBody = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            jsonBody = objectMapper.writeValueAsString(payload);
            log.info("JSON body: {}", jsonBody);
        } catch (JsonProcessingException e) {

            log.error("Không thể chuyển đổi payload thành JSON", e);
            throw new RuntimeException("Lỗi khi convert JSON", e); // hoặc xử lý theo logic của bạn
        }

        String encoding = generateAuthHeader(hiloAuthDtoDto.getUserName(), hiloAuthDtoDto.getPassword());

        // Lấy URL động
        String url = getHiloUrl(AppConstant.EInvoice.D_EINVOICE_HILO_URL_REPLACE);

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.set("Authentication", encoding);
        headers.set("taxcode", hiloAuthDtoDto.getTaxCode());

        try{

            log.info("payload: " + xmlData);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            log.info("url: " + url);
            ResponseEntity<EInvoiceResDto> rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, EInvoiceResDto.class);

            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){

                log.info("Issue Success");
                log.info("Issue Result" + objectMapper.writeValueAsString(rsp));
                EInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                log.info("Response: " + objectMapper.writeValueAsString(eInvoiceReqDtoResponse));

                Object dataObj = eInvoiceReqDtoResponse.getData();

                List<?> rawList = (List<?>) dataObj;
                Map<String, Object> rawData = (Map<String, Object>) rawList.get(0);

                HiloEInvoiceResultDto eInvoiceResultDto = new HiloEInvoiceResultDto();
                eInvoiceResultDto.setFKey((String) rawData.get("fkey"));
                eInvoiceResultDto.setNo((String) rawData.get("no"));
                eInvoiceResultDto.setPattern((String) rawData.get("pattern"));
                eInvoiceResultDto.setSerial((String) rawData.get("serial"));
                eInvoiceResultDto.setSearchKey((String) rawData.get("searchKey"));
                eInvoiceResultDto.setTaxOfCode((String) rawData.get("TaxOfCode"));

                String sql = "SELECT " +
                        " name, value  " +
                        " FROM pos.d_config WHERE 1 = 1 " +
                        " AND name = :param1 " ;

                String searchLink = null;

                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                        .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_HILO_RESCUE_URL)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();

                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);
                    if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_HILO_RESCUE_URL))
                        searchLink = ParseHelper.STRING.parse(row.get("value"));
                }

                updateInvoice(request.getInvoiceId(),
                        eInvoiceResultDto.getSerial(),
                        eInvoiceResultDto.getPattern(),
                        null,
                        eInvoiceResultDto.getNo(),
                        null,
                        null,
                        eInvoiceResultDto.getTaxOfCode(),
                        searchLink,
                        eInvoiceResultDto.getFKey(),
                        null,
                        DateHelper.toInstantNowUTC(),"ISD",null);
                return "ISD";
            }else{

                log.info("Issue Failed");
                EInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                updateInvoice(request.getInvoiceId(),
                        hiloAuthDtoDto.getSerial(),
                        hiloAuthDtoDto.getPattern(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null, null, "FAI",eInvoiceReqDtoResponse.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();

        }

        return "FAI";
    }

    @Override
    public GlobalReponse getHiloInvInfo(Integer invoiceId) {

        String url = getHiloUrl(AppConstant.EInvoice.D_EINVOICE_HILO_URL_PDF);

        HiloAuthDto hiloAuthDtoDto = getHiloAuth(invoiceId);
        if (hiloAuthDtoDto.getTaxCode() == null || hiloAuthDtoDto.getPassword() == null || hiloAuthDtoDto.getUserName() == null || hiloAuthDtoDto.getPattern() == null || hiloAuthDtoDto.getSerial() == null) {

            throw new PosException(messageSource.getMessage("setup.not.configured", null, LocaleContextHolder.getLocale()));
        }

        url = url + "?fkey=" + invoiceId +
                "&pattern=" + hiloAuthDtoDto.getPattern() +
                "&serial=" + hiloAuthDtoDto.getSerial();

        log.info("Get PDF: " + url);

        String encoding = generateAuthHeader(hiloAuthDtoDto.getUserName(), hiloAuthDtoDto.getPassword());

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        headers.set("Authentication", encoding);
        headers.set("taxcode", hiloAuthDtoDto.getTaxCode());

        ResponseEntity<EInvoiceResDto> rsp = externalRestTemplate.exchange(url, HttpMethod.GET, requestEntity, EInvoiceResDto.class);

        if (rsp.getStatusCode().value() == HttpStatus.OK.value()) {

            return GlobalReponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .data(rsp.getBody().getData())
                    .build();
        } else {

            return GlobalReponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .data(null)
                    .build();
        }
    }

    @Override
    public GlobalReponse adjustHiloEInvoice(IssueEInvoiceDto request) {

        log.info("adjustHiloEInvoice rq: {}", request);
        String message = adjustHiloInvoice(request);
        if(message.equals("FAI")){
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(issueHiloInvoice(request))
                .build();
    }

    public String adjustHiloInvoice(IssueEInvoiceDto request) {

        GlobalReponse response = new GlobalReponse();

        Invoice invoice = this.invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() ->
                new ObjectNotFoundException(messageSource.getMessage("invoice.not.Found", null, LocaleContextHolder.getLocale())));
        List<InvoiceLine> invoiceLines = this.invoiceLineRepository.findAllByInvoiceId(request.getInvoiceId());

        HiloAuthDto hiloAuthDtoDto = getHiloAuth(request.getInvoiceId());
        if (hiloAuthDtoDto.getTaxCode() == null || hiloAuthDtoDto.getPassword() == null || hiloAuthDtoDto.getUserName() == null || hiloAuthDtoDto.getPattern() == null || hiloAuthDtoDto.getSerial() == null) {

            throw new PosException(messageSource.getMessage("setup.not.configured", null, LocaleContextHolder.getLocale()));
        }

        HiloEInvoiceReqDto hiloEInvoiceReqDto = getHiloEInvoiceReqDto(request, hiloAuthDtoDto.getPattern(), hiloAuthDtoDto.getSerial());
        String xmlData = null;

        try {

            xmlData = convertToXml(hiloEInvoiceReqDto, "replace");
        }catch(JAXBException e){

            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

        // Create payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("xmlData", xmlData);
        payload.put("fkey", invoice.getId());
        payload.put("pattern", hiloAuthDtoDto.getPattern());
        payload.put("serial", hiloAuthDtoDto.getSerial());
        payload.put("convert", false);

        String jsonBody = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            jsonBody = objectMapper.writeValueAsString(payload);
            log.info("JSON body: {}", jsonBody);
        } catch (JsonProcessingException e) {

            log.error("Không thể chuyển đổi payload thành JSON", e);
            throw new RuntimeException("Lỗi khi convert JSON", e); // hoặc xử lý theo logic của bạn
        }

        String encoding = generateAuthHeader(hiloAuthDtoDto.getUserName(), hiloAuthDtoDto.getPassword());

        // Lấy URL động
        String url = getHiloUrl(AppConstant.EInvoice.D_EINVOICE_HILO_URL_REPLACE);

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.set("Authentication", encoding);
        headers.set("taxcode", hiloAuthDtoDto.getTaxCode());

        try{

            log.info("payload: " + xmlData);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            log.info("url: " + url);
            ResponseEntity<EInvoiceResDto> rsp = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, EInvoiceResDto.class);

            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){

                log.info("Issue Success");
                log.info("Issue Result" + objectMapper.writeValueAsString(rsp));
                EInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                log.info("Response: " + objectMapper.writeValueAsString(eInvoiceReqDtoResponse));

                Object dataObj = eInvoiceReqDtoResponse.getData();

                List<?> rawList = (List<?>) dataObj;
                Map<String, Object> rawData = (Map<String, Object>) rawList.get(0);

                HiloEInvoiceResultDto eInvoiceResultDto = new HiloEInvoiceResultDto();
                eInvoiceResultDto.setFKey((String) rawData.get("fkey"));
                eInvoiceResultDto.setNo((String) rawData.get("no"));
                eInvoiceResultDto.setPattern((String) rawData.get("pattern"));
                eInvoiceResultDto.setSerial((String) rawData.get("serial"));
                eInvoiceResultDto.setSearchKey((String) rawData.get("searchKey"));
                eInvoiceResultDto.setTaxOfCode((String) rawData.get("TaxOfCode"));

                String sql = "SELECT " +
                        " name, value  " +
                        " FROM pos.d_config WHERE 1 = 1 " +
                        " AND name = :param1 " ;

                String searchLink = null;

                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                        .setParameter("param1", AppConstant.EInvoice.D_EINVOICE_HILO_RESCUE_URL)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();

                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);
                    if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.EInvoice.D_EINVOICE_HILO_RESCUE_URL))
                        searchLink = ParseHelper.STRING.parse(row.get("value"));
                }

                updateInvoice(request.getInvoiceId(),
                        eInvoiceResultDto.getSerial(),
                        eInvoiceResultDto.getPattern(),
                        null,
                        eInvoiceResultDto.getNo(),
                        null,
                        null,
                        eInvoiceResultDto.getTaxOfCode(),
                        searchLink,
                        eInvoiceResultDto.getFKey(),
                        null,
                        DateHelper.toInstantNowUTC(),"ISD",null);
                return "ISD";
            }else{

                log.info("Issue Failed");
                EInvoiceResDto eInvoiceReqDtoResponse = rsp.getBody();
                updateInvoice(request.getInvoiceId(),
                        hiloAuthDtoDto.getSerial(),
                        hiloAuthDtoDto.getPattern(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null, null, "FAI",eInvoiceReqDtoResponse.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();

        }

        return "FAI";
    }

   @Override
    public GlobalReponse setDefault(Integer id) {

        log.info("setDefault rq: {}", id);
        try {

            StringBuilder sql = new StringBuilder("select is_default from d_einvoice_setup des where is_active = 'Y' and d_einvoice_setup_id = :setUpId");

            Query query = this.entityManager.createNativeQuery(sql.toString()).setParameter("setUpId", id);

            Object value = query.getSingleResult();

            if (value != null && "Y".equals(value.toString())) {

                eInvoiceOrgRepository.updateSetDefaultById(id, "N");
            } else {

                eInvoiceOrgRepository.updateSetDefaultById(id, "Y");
                eInvoiceOrgRepository.updateSetDefaultAllNotInById(id);
            }

        }catch (Exception e){

            e.printStackTrace();
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data("")
                .build();
    }


}
