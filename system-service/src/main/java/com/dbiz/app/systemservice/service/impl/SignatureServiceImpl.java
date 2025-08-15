package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.constant.AppConstant;
import com.dbiz.app.systemservice.service.SignatureService;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.HeaderPayloadNapasDto;
import org.common.dbiz.dto.paymentDto.napas.InvestigationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.dto.systemDto.NapasConfigResDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.NapasErrorException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final EntityManager entityManager;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;


    @Override
    public GlobalReponse verifyNapasData(PayloadNapasDto dto) {

        log.info("Verify Napas Signature");

        // To get PublicKey
        PublicKey publicKey = (PublicKey) getPKey("PUBLIC");
        boolean signedData = false;
        try{
            String data = convertStringJson(dto);
            if(data != null){
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initVerify(publicKey);
                signature.update(data.getBytes());
                byte[] signatureBytes = Base64.getDecoder().decode(dto.getHeader().getSignature());
                signedData =  signature.verify(signatureBytes);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("Log 2: " + e.getMessage());
        }

//        if(signedData == false){
//            throw new NapasErrorException("Signature is invalid");
//        }

        return GlobalReponse.builder()
                .status( signedData == true ? HttpStatus.OK.value() : HttpStatus.MULTIPLE_CHOICES.value())
                .message( signedData == true ?  "Success" : "Signature is invalid")
                .build();
    }

    @Override
    public GlobalReponse signNapasData(PayloadNapasDto dto) {

        log.info("Sign Napas payload");
        // To get PrivateKey
        PrivateKey privateKey = (PrivateKey) getPKey("PRIVATE");
        String finalSignature = null;
        HeaderPayloadNapasDto header = null;
        try{
            header = getNapasHeader();
            dto.setHeader(header);
            String data = convertStringJson(dto);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] signedData = signature.sign();
            finalSignature = Base64.getEncoder().encodeToString(signedData);
            header.setSignature(finalSignature);
        }catch (Exception e){
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(header)
                .build();

    }

    public HeaderPayloadNapasDto getNapasHeader() {

        String senderId = null;
        String receiverId = null;
        try {
            String sql = "SELECT " +
                    " value , name  " +
                    " FROM pos.d_config WHERE 1 = 1 " +
                    " AND name in (:param1,:param2) " ;


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("param1", AppConstant.Napas.D_NAPAS_RECEIVER_ID)
                    .setParameter("param2", AppConstant.Napas.D_NAPAS_SENDER_ID)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_RECEIVER_ID))
                    receiverId = ParseHelper.STRING.parse(row.get("value"));

                if(ParseHelper.STRING.parse(row.get("name")).equals(AppConstant.Napas.D_NAPAS_SENDER_ID))
                    senderId = ParseHelper.STRING.parse(row.get("value"));

            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return HeaderPayloadNapasDto.builder()
                .messageIdentifier(AppConstant.NapasMessageIdentifier.INVEST_REQUEST)
                .senderReference(getNapasSenderReference(senderId,AppConstant.NapasServiceType.INVESTIGATION))
                .creationDateTime(DateHelper.convertTimeNZtoTimeZ(null))
                .senderId(senderId)
                .receiverId(receiverId)

                .build();
    }

    public static String getNapasSenderReference(String senderId,String serviceId) {

        // Định dạng ngày hiện tại thành YYYYMMDD
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());


        // Lấy giờ phút giây hiện tại
        String timePart = new SimpleDateFormat("HHmmss").format(new Date()); // hh:mm:ss → HHmmss

        // Sinh 6 chữ số ngẫu nhiên
        Random random = new Random();
        int randomNumber = random.nextInt(1_000_000); // từ 0 tới 999999
        String randomPart = String.format("%06d", randomNumber); // luôn đủ 6 số, thêm 0 phía trước nếu thiếu

        String refId = timePart + randomPart;

        // Ghép chuỗi lại
        return date + senderId + serviceId + refId;
    }


    public String convertStringJson(PayloadNapasDto dto){

        try {
            // Convert object to Map
            Map<String, Object> map = objectMapper.convertValue(dto.getPayload(), Map.class);
            if(map.containsKey("caseId")){
                map.put("caseId",dto.getHeader().getSenderReference());
            }
            // Convert back to JSON string without pretty print
            return objectMapper.disable(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(map);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    public void test() throws Exception{

        // PEM certificate
        String certPem = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDMjCCAhqgAwIBAgIUDJXTnvExP/7Vdy5AwtrGAibvX3QwDQYJKoZIhvcNAQEL\n" +
                "BQAwUzELMAkGA1UEBhMCVk4xDjAMBgNVBAgMBUhhbm9pMQ4wDAYDVQQHDAVIYW5v\n" +
                "aTEQMA4GA1UECgwHVGVzdE9yZzESMBAGA1UEAwwJVGVzdCBDZXJ0MB4XDTI1MDQy\n" +
                "NTAzMjM0N1oXDTI1MDUwNTAzMjM0N1owUzELMAkGA1UEBhMCVk4xDjAMBgNVBAgM\n" +
                "BUhhbm9pMQ4wDAYDVQQHDAVIYW5vaTEQMA4GA1UECgwHVGVzdE9yZzESMBAGA1UE\n" +
                "AwwJVGVzdCBDZXJ0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4Ue2\n" +
                "guZN0g8zfSC2xbY58AZcNIchjHrsZpjNHKqB/fGRjZJdn0QHieFFEOyOJ4972ytw\n" +
                "6L/kPYDmM3gR3BE/wLDlyXlSKS9fU3dpiAL8Yh2C3Ne1Jb2vcvy/fjEf0MdL2Yd2\n" +
                "sE/Icx0En/r8scrMjOHC6rb6KO9Qc6d0sT5w+B87SckWKDzCaz07N3/fixbNulFt\n" +
                "8meqdSAa+1HN7JiGYJdK4/z65QU8FTRCjeQ3l9LhzTBnmkCct+4MjoCiG3zsD7Vb\n" +
                "mcsNU9fRK5IFpkVTTo/+fMcH15X2Bui5thBzz/kQ7E3D+AbBvZehbowctX8bzPfj\n" +
                "sVK8PtxwZcUuMAzm8wIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQCtfzVoOWFuSG6H\n" +
                "IdzmcV18osQeIwDb1NXJvD/i/H885na/PozDRZ5tDLK/UyyyNrN+jZuMUiThRBhl\n" +
                "eU6FAmMEh0G0IEpoU0lb9NX/tYnxXecxCbALXVoQsoWgNsv14zn3Rc2Soktq74yw\n" +
                "Ep+q9z40Gai8YT0FD1unTznPtpyiVAFrdnBP24Y5UTwCj1avikwXoGnrshTlIMB0\n" +
                "2WdYfEFRtY1DHdCt1kMuR2gahIVbL6SOLvLTcgczGKYKq4xMgaN4qQGJSkY78JNs\n" +
                "VOEhlEO4uwLZCSjjHKfAwL5hGQMIG3y4o/DXfRH9oAeF7hKRRfo3DsFKySQNF15O\n" +
                "NoEtbFxS\n" +
                "-----END CERTIFICATE-----";

        // Dữ liệu gốc
        String data = "{\"id\":123,\"name\":\"John\"}";

        // Signature (Base64)
        String signatureBase64 = "U+8jIq9Ogj7y6H9GNpWRZ/xkevup/lppkeATOCxx0UR/fx2ZfpPSQUQFCVvu080eTEzcCTrHwOCkozcznGaRnZLWr9rHUJmE4aGruWg3BSqd6D2YqXlEiZ01GbcvPGaPMhvpsSIhu8lz6sJ9f8Wks4L1ffgkpOqKH/N98rPSYB1Nzm6UNXHRT5mkGXZ61jKfWMd8UojLtzcbF20AKoAbsba8I5zAOIPJpq6Xuzldx0d79WEN9icNZnom5PRGzT6d82j0zpvvbqjN+w8rsLuTtKH/9rp38rXbNGy1BwSnfnf7pxJ4ugpS7UTHM6aTQgAJXxSFk6kFO/vrGkrctdZaLw==";

        // Convert cert to PublicKey
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(certPem.getBytes());
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(inputStream);
        PublicKey publicKey = certificate.getPublicKey();

        // Verify
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes(StandardCharsets.UTF_8));
        boolean isValid = sig.verify(Base64.getDecoder().decode(signatureBase64));

        System.out.println("Signature valid? " + isValid);
    }

    public Object getPKey(String keyType) {

        try {
            if ("PRIVATE".equalsIgnoreCase(keyType)) {
                // Fetch and return PrivateKey
                return getPrivateKey(getPKeySQL(AppConstant.Napas.D_SYSTEM_PRIVATE_KEY));
            } else if ("PUBLIC".equalsIgnoreCase(keyType)) {
                // Fetch and return PublicKey
                return getPublicKey(getPKeySQL(AppConstant.Napas.D_NAPAS_PUBLIC_KEY));
            } else {
                throw new IllegalArgumentException("Invalid key type. Use 'PRIVATE' or 'PUBLIC'.");
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }
    public String getPKeySQL(String name) {

        String key = null;
        try {
            String sql = "SELECT " +
                    " p_key , name  " +
                    " FROM pos.d_config WHERE 1 = 1 " +
                    " AND name = :name " ;

            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("name", name)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                key = ParseHelper.STRING.parse(row.get("p_key"));
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
        return key;
    }

    // Chuyển chuỗi Base64 thành Public Key
    public PublicKey getPublicKey(String base64Key) throws Exception {

//        base64Key = "-----BEGIN CERTIFICATE-----\n" +
//                "MIIDuTCCAqECFAlBJ8BHl08EecwLWS5Bj8L0CZeNMA0GCSqGSIb3DQEBCwUAMIGY\n" +
//                "MQswCQYDVQQGEwJWTjEUMBIGA1UECAwLSG8gQ2hpIE1pbmgxFDASBgNVBAcMC0hv\n" +
//                "IENoaSBNaW5oMQ0wCwYDVQQKDAREQklaMQ0wCwYDVQQLDAREQklaMR8wHQYDVQQD\n" +
//                "DBZhcGltLmRpZ2l0YWxiaXouY29tLnZuMR4wHAYJKoZIhvcNAQkBFg9taW5obnZA\n" +
//                "ZGJpei5jb20wHhcNMjUwNDE5MTUwMDU2WhcNMzUwNDE3MTUwMDU2WjCBmDELMAkG\n" +
//                "A1UEBhMCVk4xFDASBgNVBAgMC0hvIENoaSBNaW5oMRQwEgYDVQQHDAtIbyBDaGkg\n" +
//                "TWluaDENMAsGA1UECgwEREJJWjENMAsGA1UECwwEREJJWjEfMB0GA1UEAwwWYXBp\n" +
//                "bS5kaWdpdGFsYml6LmNvbS52bjEeMBwGCSqGSIb3DQEJARYPbWluaG52QGRiaXou\n" +
//                "Y29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApMLs18Kkdb4cg9Wy\n" +
//                "UxInzxl+ibep6OwDxcPbKkmfw71EaYjXlA2L44tFVZdbif5TbzppisMpcMaSWTQ8\n" +
//                "0dOJu+OnFlvc3CVRY75jJzL4ifF0dUTAGGO0+oHmVmAR2fDDVBz2Eb5jkdG606tv\n" +
//                "RkUGPN02udLLsVFGicFRJVeKYOuWUNw+MMSLM7E1chsRVin1neVVJDNdyVHeqxiv\n" +
//                "ax5BHTwiuQxeaFv1gXB6tWHAz2lL6N4F2WtuyLZ/bUSEkKds0GJyDgpKaars0ywU\n" +
//                "fQtOZ23woDH4oGkfeqQ93ypXGPEd5BFDF1K0qBzDExvJWyV105o/09mzK+Xq5sL4\n" +
//                "/NCA1QIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQAyHiUuD189RkXAouPMSb9LCy8H\n" +
//                "qm8ha8tstnqen7t8fP/GXN93aYWf4JXFBgBvM5KDWHf1fAQ9WGr6KGhN76iPeCnP\n" +
//                "McPIz2WAuOMClrDIQyKOnGHF8fMmmGK6WcJ2t6ERSjcFv5iclp3ILti86K42AumU\n" +
//                "7ODz3fhH3qGLvIP/U/ez7LehXxYtNzODVINGdYkrmiroKUfd/2fWWx6tm6ru8OJQ\n" +
//                "eBfwfYJGrXMjIAFydvyBj3dMVRfVajr3TqJ4NhMhwN4ZmspNw+1aXJOUdldqJUlk\n" +
//                "RiqVEc0tKb7xe0eoS4uaGdGpUIHvwQpY5jj2RHzGKlVHMiOZVusNgQZxE+XD\n" +
//                "-----END CERTIFICATE-----";
//        base64Key = base64Key
//                .replace("-----BEGIN CERTIFICATE-----", "")
//                .replace("-----END CERTIFICATE-----", "")
//                .replaceAll("\\s+", "");
//        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Key.getBytes());

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(keyBytes));
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(inputStream);
        return certificate.getPublicKey();
    }

    // Chuyển chuỗi Base64 thành Private Key
    public PrivateKey getPrivateKey(String base64Key) throws Exception {

        base64Key = base64Key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
}
