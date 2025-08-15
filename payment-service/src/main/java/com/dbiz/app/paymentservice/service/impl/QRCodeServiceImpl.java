package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.*;
import com.dbiz.app.paymentservice.service.NapasConfigService;
import com.dbiz.app.paymentservice.service.VoucherService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.dto.orderDto.PosOrderRetailDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.dto.paymentDto.request.QRCodeReqDto;
import org.common.dbiz.dto.systemDto.NapasConfigResDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.exception.wrapper.PerstingObjectException;
import com.dbiz.app.paymentservice.repository.BankAccountRepository;
import com.dbiz.app.paymentservice.repository.BankRepository;
import com.dbiz.app.paymentservice.repository.PayMethodOrgRepository;
import com.dbiz.app.paymentservice.service.QRCodeService;
import com.dbiz.app.tenantservice.domain.AuditContext;

import com.dbiz.app.paymentservice.repository.PosPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.dto.paymentDto.*;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.Encoder;
import org.common.dbiz.model.QRCodeDecoded;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

@Service

@Slf4j
@RequiredArgsConstructor
public class QRCodeServiceImpl implements QRCodeService {

    private final PosPaymentRepository posPaymentRepository;
    private final ModelMapper modelMapper;
    private final BankAccountRepository bankAccountRepository;
    private final BankRepository bankRepository;
    private final MBBVietQR mBBVietQR;
    private final PayMethodOrgRepository payMethodOrgRepository;
    private final VoucherService voucherService;
    private final NapasConfigService napasConfigService;
    private final EntityManager entityManager;
    private final MessageSource messageSource;

    private final Integer MB_BANK_NOT_CONFIG_CODE = 422;

    private final Integer NAPAS_NOT_CONFIG_CODE = 422;


    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;
    @Autowired
    private DataSourceConfigService dataSourceConfigService;

    @Override
    public GlobalReponse generateQRCodeMBB(Object Dto) {

        QRCodeMBBDto qrCodeMBBDto = (QRCodeMBBDto) Dto;
        log.info("Generate QR code MBB");
        GlobalReponse response = new GlobalReponse();
        String qrCode = null;
        int status = HttpStatus.OK.value();
        String message = messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale());
        Optional<PayMethodOrg> payMethodOrg = payMethodOrgRepository.findByOrgIdAndPayMethodName(
                AuditContext.getAuditInfo().getTenantId(),
                qrCodeMBBDto.getOrgId(),
                "MBB_QRCODE");
        if(!payMethodOrg.isPresent()){
            response.setData(QRCodeRespDto.builder()
                    .qrCode(qrCode)
                    .build());
            response.setMessage(messageSource.getMessage("mb.not.config", null, LocaleContextHolder.getLocale()));
            response.setStatus(MB_BANK_NOT_CONFIG_CODE);
            return response;
        }

        try{
            String myToken = mBBVietQR.GetMBBToken( 0);
            log.info("My Token: {}", myToken);

            if (myToken != null) {
                 qrCode = mBBVietQR.GetQR_MBB(qrCodeMBBDto.getOrgId(),
                            qrCodeMBBDto.getPosOrderId(), myToken, qrCodeMBBDto.getAmount(), payMethodOrg.get());
              //                GlobalReponse serviceDto  = restTemplate.getForObject(
//                        AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_TERMINAL_BY_ID_URL+"/" + qrCodeMBBDto.getPosTerminalId(),
//                        GlobalReponse.class);
//                PosTerminalDto terminalDto = modelMapper.map(serviceDto.getData(), PosTerminalDto.class);

                log.info("QR code: {}", qrCode);
                if(qrCode == null || qrCode.isEmpty()){
                    status = MB_BANK_NOT_CONFIG_CODE;
                    message = messageSource.getMessage("mb.not.config", null, LocaleContextHolder.getLocale());
                }
                // Create headers

                HttpHeaders headers = new HttpHeaders();
                // Add headers as needed
                headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                // For example, adding an Authorization header
                headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
                headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());

//                BankAccount bankAccount = bankAccountRepository.findById(terminalDto.getBankAccountId()).get();


                PosOrderDto posOrderDto = new PosOrderDto();
                posOrderDto.setId(qrCodeMBBDto.getPosOrderId());
//                posOrderDto.setBankId(bankAccount.getBankId());
//                posOrderDto.setBankAccountId(terminalDto.getBankAccountId());
                posOrderDto.setQrcodePayment(qrCode);
                posOrderDto.setDeviceToken(qrCodeMBBDto.getDeviceToken());
                // Create an HttpEntity with the headers and the request body
                HttpEntity<PosOrderDto> requestEntity = new HttpEntity<>(posOrderDto, headers);
                //Response from Invoice Service
                GlobalReponse reponsePosOrder = this.restTemplate
                        .postForEntity(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_API_UPDATE_HEADER_URL ,
                                requestEntity,
                                GlobalReponse.class)
                        .getBody();
                if(reponsePosOrder.getStatus().intValue() != HttpStatus.OK.value()
                        && reponsePosOrder.getStatus().intValue() != HttpStatus.CREATED.value()) {
                    throw new RuntimeException(reponsePosOrder.getMessage());
                }

                try {
                    posPaymentRepository.deletePosPaymentsByPosOrderIdAndTenantId(qrCodeMBBDto.getPosOrderId(), AuditContext.getAuditInfo().getTenantId());
                }catch (Exception e) {
                    e.printStackTrace();
                }
                for(PaymentDto paymentDto :  qrCodeMBBDto.getPayments()){
                    //PosPayment
                    PosPayment posPayment = new PosPayment();
                    posPayment.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    posPayment.setOrgId(qrCodeMBBDto.getOrgId());
                    posPayment.setPosOrderId(qrCodeMBBDto.getPosOrderId());
                    posPayment.setPaymentMethod(paymentDto.getPaymentRule());
                    posPayment.setTransactionId(paymentDto.getTransactionId());
                    if(paymentDto.getCode() != null){
                        posPayment.setVoucherCode(paymentDto.getCode());
                    }
                    posPayment.setTotalAmount(paymentDto.getPaymentAmount());
//                posPayment.setTransactionId(p.getT);
                    posPayment.setIsProcessed("Y");
//                posPayment.setNote();
                    posPayment = posPaymentRepository.save(posPayment);

                    if((paymentDto.getPaymentRule().equals(AppConstant.PaymentRule.VOUCHER) ||
                            paymentDto.getPaymentRule().equals(AppConstant.PaymentRule.COUPON))
                            && paymentDto.getDetails() != null && !paymentDto.getDetails().isEmpty()){
                        voucherService.saveVoucherCouponPayment(paymentDto.getDetails(),
                                qrCodeMBBDto.getOrgId(),
                                posPayment.getId(),
                                paymentDto.getPaymentRule());
                    }
                }

            }
        }catch (Exception e) {
            log.info("Lỗi khi lấy MBB token", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            message = messageSource.getMessage("mb.config.error", null, LocaleContextHolder.getLocale());
        }
        QRCodeRespDto qrCodeRespDto = QRCodeRespDto.builder()
                .qrCode(qrCode)
                .build();
        response.setData(qrCodeRespDto);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }

    @Override
    public GlobalReponse checkQRCodeMBB(Object Dto) {

        CheckQRCodeMBBDto qrCodeMBBDto = (CheckQRCodeMBBDto) Dto;
        log.info("Check QR code MBB");
        GlobalReponse response = new GlobalReponse();
        try {
            Optional<PayMethodOrg> payMethodOrg = payMethodOrgRepository.findByOrgIdAndPayMethodName(
                    AuditContext.getAuditInfo().getTenantId(),
                    qrCodeMBBDto.getOrgId(),
                    "MBB_QRCODE");
            if (!payMethodOrg.isPresent()) {
                throw new ObjectNotFoundException("Cannot find MBB payment method");
            }

            String myToken = mBBVietQR.GetMBBToken(0);
            if (myToken != null) {
                UpdateOrderMBBDto updateOrderMBBDto = mBBVietQR.MBBQueryTrans(qrCodeMBBDto.getOrgId(),
                        qrCodeMBBDto.getPosOrderId(), myToken, payMethodOrg.get());
                if (updateOrderMBBDto != null){
                    updateOrderMBBDto.setIsSkipCheckSum("Y");
                    updateOrderMBBDto.setReferenceLabelCode(qrCodeMBBDto.getPosOrderId().toString());
                    String reponsePosOrder = this.restTemplate
                            .postForEntity(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_API_UPDATE_MBB_URL,
                                    updateOrderMBBDto,
                                    String.class)
                            .getBody();
                    if(reponsePosOrder == null || reponsePosOrder.isEmpty()){
                        throw new PerstingObjectException("Update Order MBB failed");
                    }

                    JSONParser parser = new JSONParser();

                    JSONObject myres =  (JSONObject) parser.parse(reponsePosOrder);
                    log.info("resp Update Order MBB: " + myres.toJSONString());
                    String res =  ((String) myres.get("resCode"));
                    if(!res.equals("00")) throw new PerstingObjectException("Update Order MBB failed");


                } else {
                    throw new PerstingObjectException("Update Order MBB failed");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new PerstingObjectException("Update Order MBB failed");
        }
        response.setData("");
        response.setMessage("Check order successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse generateQRCode(Object Dto) {
        QRCodeDto credential = (QRCodeDto) Dto;
        log.info("Check QR Code");
        GlobalReponse response = new GlobalReponse();

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

//        GlobalReponse serviceDto  = restTemplate.getForObject(
//                        AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_TERMINAL_BY_ID_URL+"/" + credential.getPosTerminalId(),headers,
//                        GlobalReponse.class);
//        GlobalReponse serviceDto  = restTemplate
//                .exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_TERMINAL_BY_ID_URL+"/" + credential.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//
//        PosTerminalDto terminalDto = modelMapper.map(serviceDto.getData(), PosTerminalDto.class);

//        if(terminalDto == null){
//            throw new ObjectNotFoundException("Cannot find terminal");
//        }

        BankAccount bankAccount = bankAccountRepository.findById(credential.getBankAccountId()).orElseThrow(()-> new ObjectNotFoundException("Cannot find bank account"));

        Bank bank = bankRepository.findById(bankAccount.getBankId()).orElseThrow(()-> new ObjectNotFoundException("Cannot find bank"));


        String m_QRCode = "";
        // Phien ban du lieu
        String id00 = "000201";
        // Phuong thuc khoi tao
        String id01 = "010212";
        // Thong tin tai khoan
        //Dinh dang duy nhat toan cau = GUID
        String AID = "A000000727";
        String id3800 = "00" + right("0"+Integer.toString(AID.length()),2) + AID;


        //Acquirer ID/ BNB ID, BIN Code
        String id380100 = "00" + right("0"+Integer.toString(bank.getBinCode().length()),2) + bank.getBinCode();

        //Account No
        String id380101 = "01" + right("0"+Integer.toString(bankAccount.getAccountNo().length()),2) + bankAccount.getAccountNo();

        // Account
        String id3801 = id380100.concat(id380101);
        id3801 = "01" + right("0"+Integer.toString(id3801.length()),2) + id3801;
        // Ma dich vu
        String serviceID = "QRIBFTTA"; // Ma dich vu chuyen tien 24/7 den tai khoan
        String id3802 = "02" + right("0"+Integer.toString(serviceID.length()),2) + serviceID;
        // Thong tin tai khoan ID 38
        String id38 = id3800.concat(id3801).concat(id3802);
        id38 = "38" + right("0"+Integer.toString(id38.length()),2) + id38;

        // Ma tien te
        String currencyCode = "704"; // VND
        String id53 = "53" + right("0"+Integer.toString(currencyCode.length()),2) + currencyCode;

        // So tien GD
        String id54 = "54" + right("0"+Integer.toString(credential.getAmount().length()),2) + credential.getAmount();

        // Ma Quoc gia
        String countryCode = "VN"; // Viet Nam
        String id58 = "58" + right("0"+Integer.toString(countryCode.length()),2) + countryCode;
        // Thong tin bo sung
//					String id62 = "62";
////					 So tham chieu
//					String id6205 = "01" + right("0"+Integer.toString(order.getDocumentNo().length()),2) + order.getDocumentNo();
//					id62 = id62.concat(id6205);
        // CRC
        String id63 ="6304";
        m_QRCode = id00.concat(id01).concat(id38).concat(id53).concat(id54).concat(id58).concat(id63);
        //	String CRC = caculateCRC(m_QRCode);
        m_QRCode = m_QRCode.concat(calculateCRC(m_QRCode));

        //	m_QRCode = id00.concat(id01).concat(id38).concat(id53).concat(id54).concat(id58).concat(id62).concat(id63);
        QRCodeRespDto qrCodeRespDto = QRCodeRespDto.builder()
                .qrCode(m_QRCode)
                .bankName(bank.getName())
                .accountNo(bankAccount.getAccountNo())
                .bankOwner(bankAccount.getName())
                .build();

        response.setData(qrCodeRespDto);
        response.setMessage("Generated QRCode successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse generateQRCodeByBankId(Object Dto) {
        QRCodeDto credential = (QRCodeDto) Dto;
        log.info("Check QR Code");
        GlobalReponse response = new GlobalReponse();

        BankAccount bankAccount = bankAccountRepository.findById(credential.getBankAccountId()).orElseThrow(()-> new ObjectNotFoundException("Cannot find bank account"));

        Bank bank = bankRepository.findById(bankAccount.getBankId()).orElseThrow(()-> new ObjectNotFoundException("Cannot find bank"));


        String m_QRCode = "";
        // Phien ban du lieu
        String id00 = "000201";
        // Phuong thuc khoi tao
        String id01 = "010212";
        // Thong tin tai khoan
        //Dinh dang duy nhat toan cau = GUID
        String AID = "A000000727";
        String id3800 = "00" + right("0"+Integer.toString(AID.length()),2) + AID;


        //Acquier ID/ BNB ID, BIN Code
        String id380100 = "00" + right("0"+Integer.toString(bank.getBinCode().length()),2) + bank.getBinCode();

        //Account No
        String id380101 = "01" + right("0"+Integer.toString(bankAccount.getAccountNo().length()),2) + bankAccount.getAccountNo();

        // Account
        String id3801 = id380100.concat(id380101);
        id3801 = "01" + right("0"+Integer.toString(id3801.length()),2) + id3801;
        // Ma dich vu
        String serviceID = "QRIBFTTA"; // Ma dich vu chuyen tien 24/7 den tai khoan
        String id3802 = "02" + right("0"+Integer.toString(serviceID.length()),2) + serviceID;
        // Thong tin tai khoan ID 38
        String id38 = id3800.concat(id3801).concat(id3802);
        id38 = "38" + right("0"+Integer.toString(id38.length()),2) + id38;

        // Ma tien te
        String currencyCode = "704"; // VND
        String id53 = "53" + right("0"+Integer.toString(currencyCode.length()),2) + currencyCode;

        // So tien GD
        String id54 = "54" + right("0"+Integer.toString(credential.getAmount().length()),2) + credential.getAmount();

        // Ma Quoc gia
        String countryCode = "VN"; // Viet Nam
        String id58 = "58" + right("0"+Integer.toString(countryCode.length()),2) + countryCode;
        // Thong tin bo sung
//					String id62 = "62";
////					 So tham chieu
//					String id6205 = "01" + right("0"+Integer.toString(order.getDocumentNo().length()),2) + order.getDocumentNo();
//					id62 = id62.concat(id6205);
        // CRC
        String id63 ="6304";
        m_QRCode = id00.concat(id01).concat(id38).concat(id53).concat(id54).concat(id58).concat(id63);
        //	String CRC = caculateCRC(m_QRCode);
        m_QRCode = m_QRCode.concat(calculateCRC(m_QRCode));

        //	m_QRCode = id00.concat(id01).concat(id38).concat(id53).concat(id54).concat(id58).concat(id62).concat(id63);
        QRCodeRespDto qrCodeRespDto = QRCodeRespDto.builder()
                .qrCode(m_QRCode)
                .bankName(bank.getName())
                .accountNo(bankAccount.getAccountNo())
                .bankOwner(bankAccount.getName())
                .build();

        response.setData(qrCodeRespDto);
        response.setMessage("Generated QRCode successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse generateQRCodeByNapas(QRCodeReqDto dto) {

        log.info("Check QR Code");
        GlobalReponse response = new GlobalReponse();
        int status = HttpStatus.OK.value();
        String message = messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale());

        BankIntegrationInfoDto bankIntegrationInfo = napasConfigService.getBankIntegrationInfo(dto.getOrgId(),dto.getPosTerminalId());
        NapasConfigResDto napasConfigResDto = napasConfigService.getNapasConfig("INFO");

        String m_QRCode = "";
        // Phien ban du lieu

            String id00 = "000201";
            // Phuong thuc khoi tao
            String id01 = "010212";
            // Thong tin tai khoan
            //Dinh dang duy nhat toan cau = GUID
            String AID = "A000000727";
            String id3800 = "00" + right("0" + Integer.toString(AID.length()), 2) + AID;


            //Acquier ID/ BNB ID, BIN Code
//        String id380100 = "00" + right("0"+Integer.toString(bankIntegrationInfo.getBinCode().length()),2) + bankIntegrationInfo.getBinCode();
            String id380100 = "00" + right("0" + Integer.toString("971133".length()), 2) + "971133";

            String napasType = napasConfigResDto.getNapasCode();
            String masterMerchant = napasConfigResDto.getMasterMerchantCode();
//        String posOrderRef = getPosOrderRef(dto.getPosOrderId());
            Integer npOrderId = getNPOrderID();
        if(bankIntegrationInfo == null){
            status = NAPAS_NOT_CONFIG_CODE;
            message = messageSource.getMessage("setup.up.not.found", null, LocaleContextHolder.getLocale());
        }else {
            String identifyCode = napasType
                    + masterMerchant
                    + bankIntegrationInfo.getMerchantCode()
                    + bankIntegrationInfo.getBranchCode()
                    + bankIntegrationInfo.getPosCode()
                    + npOrderId;

            //Identify Napas Code
            String id380101 = "01" + right("0" + Integer.toString(identifyCode.length()), 2) + identifyCode;

            // Account
            String id3801 = id380100.concat(id380101);
            id3801 = "01" + right("0" + Integer.toString(id3801.length()), 2) + id3801;
            // Ma dich vu
            String serviceID = "QRIBFTTA"; // Ma dich vu chuyen tien 24/7 den tai khoan
            String id3802 = "02" + right("0" + Integer.toString(serviceID.length()), 2) + serviceID;
            // Thong tin tai khoan ID 38
            String id38 = id3800.concat(id3801).concat(id3802);
            id38 = "38" + right("0" + Integer.toString(id38.length()), 2) + id38;

            //Ma nganh nghe
            String id52 = "52" + right("0" + Integer.toString(bankIntegrationInfo.getIndustryCode().length()), 2) + bankIntegrationInfo.getIndustryCode();
            // Ma tien te
            String currencyCode = "704"; // VND
            String id53 = "53" + right("0" + Integer.toString(currencyCode.length()), 2) + currencyCode;

            // So tien GD
            String id54 = "54" + right("0" + Integer.toString(dto.getAmount().toString().length()), 2) + dto.getAmount();

            // Ma Quoc gia
            String countryCode = "VN"; // Viet Nam
            String id58 = "58" + right("0" + Integer.toString(countryCode.length()), 2) + countryCode;
            // Thong tin bo sung
//					String id62 = "62";
////					 So tham chieu
//					String id6205 = "01" + right("0"+Integer.toString(order.getDocumentNo().length()),2) + order.getDocumentNo();
//					id62 = id62.concat(id6205);

            String id59 = "5903POS";

            String cty = "Ho Chi Minh";
            String id60 = "60" + right("0" + Integer.toString(cty.length()), 2) + cty;

            //Bill Number
//        String id6201 = "01" + right("0"+Integer.toString(posOrderRef.length()),2) + posOrderRef;
            String id6201 = "01" + right("0" + Integer.toString(dto.getPosOrderId().toString().length()), 2) + dto.getPosOrderId().toString();
            //Store / Branch Label
            String id6203 = "03" + right("0" + Integer.toString(bankIntegrationInfo.getBranchCode().length()), 2) + bankIntegrationInfo.getBranchCode();
            //Terminal Label
            String id6207 = "07" + right("0" + Integer.toString(bankIntegrationInfo.getPosCode().length()), 2) + bankIntegrationInfo.getPosCode();

            //Purpose of Transaction
            String transactionPurpose = "Thanh toan don hang";
            String id6212 = "08" + right("0" + Integer.toString(transactionPurpose.length()), 2) + transactionPurpose;


            String id62 = id6201.concat(id6203).concat(id6207).concat(id6212);
            id62 = "62" + right("0" + Integer.toString(id62.length()), 2) + id62;
//        id58 = id58.concat("5910Sieu thi 16006Ha Noi62600108NFBZOL1E030383B0706Ha Noi0827Thanh toan hoa don NFBZOL1E");
            // CRC
            String id63 = "6304";
            m_QRCode = id00.concat(id01).concat(id38).concat(id52).concat(id53).concat(id54).concat(id58).concat(id59)
                    .concat(id60).concat(id62).concat(id63);
            //	String CRC = caculateCRC(m_QRCode);
            m_QRCode = m_QRCode.concat(calculateCRC(m_QRCode));
        }
        //	m_QRCode = id00.concat(id01).concat(id38).concat(id53).concat(id54).concat(id58).concat(id62).concat(id63);
        QRCodeRespDto qrCodeRespDto = QRCodeRespDto.builder()
                .qrCode(m_QRCode)
//                .bankName(bank.getName())
//                .accountNo(bankAccount.getAccountNo())
//                .bankOwner(bankAccount.getName())
                .build();

        response.setData(qrCodeRespDto);
        response.setMessage(message);
        response.setStatus(status);
        processPayment(dto, m_QRCode,npOrderId);

//        QRCodeDecoded qrCodeDecoded = Encoder.decodeLength7(posOrderRef);
//        log.info("Decoded QR Code: {}", qrCodeDecoded);
        return response;
    }

    public void processPayment(QRCodeReqDto dto, String qrCode,Integer npOrderId){
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        // Add headers as needed
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        // For example, adding an Authorization header
        headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());

//                BankAccount bankAccount = bankAccountRepository.findById(terminalDto.getBankAccountId()).get();


        PosOrderDto posOrderDto = new PosOrderDto();
        posOrderDto.setId(dto.getPosOrderId());
        posOrderDto.setNpOrderId(npOrderId);
        posOrderDto.setDeviceToken(dto.getDeviceToken());

//                posOrderDto.setBankId(bankAccount.getBankId());
//                posOrderDto.setBankAccountId(terminalDto.getBankAccountId());
        posOrderDto.setQrcodePayment(qrCode);
        // Create an HttpEntity with the headers and the request body
        HttpEntity<PosOrderDto> requestEntity = new HttpEntity<>(posOrderDto, headers);
        //Response from Invoice Service
        GlobalReponse response = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_API_UPDATE_HEADER_URL ,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();
        if(response.getStatus().intValue() != HttpStatus.OK.value()
                && response.getStatus().intValue() != HttpStatus.CREATED.value()) {
            throw new RuntimeException(response.getMessage());
        }

        //Delete old pos payment
        posPaymentRepository.deletePosPaymentsByPosOrderIdAndTenantId(dto.getPosOrderId(), AuditContext.getAuditInfo().getTenantId());
        for(PaymentDto paymentDto :  dto.getPayments()){
            //PosPayment
            PosPayment posPayment = new PosPayment();
            posPayment.setTenantId(AuditContext.getAuditInfo().getTenantId());
            posPayment.setOrgId(dto.getOrgId());
            posPayment.setPosOrderId(dto.getPosOrderId());
            posPayment.setPaymentMethod(paymentDto.getPaymentRule());
            posPayment.setTransactionId(paymentDto.getTransactionId());
            if(paymentDto.getCode() != null){
                posPayment.setVoucherCode(paymentDto.getCode());
            }
            posPayment.setTotalAmount(paymentDto.getPaymentAmount());
//                posPayment.setTransactionId(p.getT);
            posPayment.setIsProcessed("Y");
//                posPayment.setNote();
            posPayment = posPaymentRepository.save(posPayment);

            if((paymentDto.getPaymentRule().equals(AppConstant.PaymentRule.VOUCHER) ||
                    paymentDto.getPaymentRule().equals(AppConstant.PaymentRule.COUPON))
                    && paymentDto.getDetails() != null && !paymentDto.getDetails().isEmpty()){
                voucherService.saveVoucherCouponPayment(paymentDto.getDetails(),
                        dto.getOrgId(),
                        posPayment.getId(),
                        paymentDto.getPaymentRule());
            }
        }
    }






    public static String right(String value, int length) {
        // To get right characters from a string, change the begin index.
        return value.substring(value.length() - length);
    }

    public static String calculateCRC(String message) {
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)
        byte[] bytes = message.getBytes();
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        String CRC16 = Integer.toHexString(crc).toUpperCase();
        return StringUtils.leftPad(CRC16, 4, "0");
    }

    public Integer getNPOrderID(){
        Query query = entityManager.createNativeQuery("SELECT  nextval('pos.d_np_order_sq')");
        return ((Number) query.getSingleResult()).intValue();
    }

    public void sendPaymentNotify(){

    }



}
