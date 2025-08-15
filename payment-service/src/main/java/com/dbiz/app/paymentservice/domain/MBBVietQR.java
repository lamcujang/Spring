package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
//import org.json.JSONObject;
//import org.json.JSONArray;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.helper.Encoder;
import org.common.dbiz.payload.GlobalReponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MBBVietQR {

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;
    private final static String D_MBB_URL_PRE ="D_MBB_URL_PRE";
    private final static String D_MBB_URL_CREATEQR ="D_MBB_URL_CREATEQR";
    private final static String D_MBB_URL_GETTOKEN = "D_MBB_URL_GETTOKEN";
    private final static String D_MBB_TOKEN_AUTHOR = "D_MBB_TOKEN_AUTHOR";
    private final static String D_MBB_URL_CHECK_ORDER ="D_MBB_URL_CHECK_ORDER";
    private final static String D_MBB_CHECKSUM = "D_MBB_KeyCheckSum";

//    public static String GetQR_MBB(int p_org_id,int p_method_id, int pos_order_id, String p_token, BigDecimal p_amount) throws IOException{
//
//        if (p_amount.compareTo(BigDecimal.ZERO)<=0)
//            return "";
//
//        String UrlCreate = D_MBB_URL_PRE + D_MBB_URL_CREATEQR;
//        log.info("UrlCreate " + UrlCreate);
////        if (log.isLoggable(Level.FINE)) s_log.fine("UrlCreate " + UrlCreate);
//
////        String mysql = "Select b.Cus_Paymethod_Org_ID from Cus_Paymethod a,Cus_Paymethod_Org b where a.Cus_Paymethod_ID=b.Cus_Paymethod_ID"
////                +" and b.ad_org_id = " + p_org_id + " and instr(upper(a.name),'MBB')>0 and a.Cus_Paymethod_id = "+p_method_id;
////        System.out.println("mysql="+mysql);
////
////        int orgmethod_id = DB.getSQLValue(null, mysql);
////        if (orgmethod_id<=0)
////            return "";
////
//        UUID myuuid = UUID.randomUUID();
////        if (s_log.isLoggable(Level.FINE)) s_log.fine("UUID: " + myuuid);
////        X_Cus_Paymethod_Org orgmethod = new X_Cus_Paymethod_Org(Env.getCtx(),orgmethod_id,null);
//
//        //orgmethod.getTe
////        String p_terminalID = orgmethod.getTerminalId();
////        String p_username = orgmethod.getAccess_Code();
////        String p_secretKey = orgmethod.getHash_Key();
//        String p_terminalID = "SSGJSC1";
//        String p_username = "MB_SSGJSC";
//        String p_secretKey = "NdNgCLQ9Uo7C5qWyZbKWGyONavFUD06XIiDJvDBbi20cRCmcwnUvzDoaRljZsKax";
//        String reqStr = "{"
//                + "\"terminalID\": \""+ p_terminalID +"\","
//                + "\"qrcodeType\": \""+ "4" +"\","
//                + "\"initMethod\": \""+ "12" +"\","
//                + "\"transactionAmount\": \"" + p_amount.toString() +"\","
//                + "\"referenceLabelCode\": \"" + pos_order_id +"\","
//                + "\"additionalAddress\": \"" + "" +"\","
//                + "\"additionalMobile\": \""+ "" +"\","
//                + "\"partnerType\": \""+ "2" +"\","
//                + "\"transactionPurpose\": \""+ "CKPM" +"\","
//                + "\"additionalEmail\": \""+ "" +"\""
//                + "}";
//        log.info("reqStr " + reqStr);
////        s_log.warning("reqStr: "+reqStr);
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(10000, TimeUnit.MILLISECONDS)
//                .readTimeout(10000, TimeUnit.MILLISECONDS)
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, reqStr);
//        Request request = new Request.Builder()
//                .url(UrlCreate)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("clientMessageId", myuuid.toString())
//                .addHeader("secretKey", p_secretKey)
//                .addHeader("username", p_username)
//                .addHeader("Authorization", "Bearer " + p_token)
//                .build();
//
//        log.info("request " + request.toString());
//
//        String responseTr = null;
//
//        try (Response response = client.newCall(request).execute()) {
//            responseTr = response.body().string();
//            log.info("CreateQR " + responseTr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("response: " + responseTr);
//
//        String qr_content = "" ;
//        String qr_status = "";
//
//        try {
//            JSONObject myres = new JSONObject(responseTr);
//            qr_status = (String) myres.get("errorCode");
//            if (qr_status.equals("000")) {
//                JSONObject data = new JSONObject(myres.get("data").toString());
//                qr_content = (String) data.get("qrcode");
//                return qr_content;
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null ;
//
//    }
//
//    public static String GetMBBToken(int p_method_id) throws IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, DecoderException{
//
//
//        InputStreamReader read;
//
//        String MBB_UrlGetToken = D_MBB_URL_PRE + D_MBB_URL_GETTOKEN;
//
//
//        log.info("MBB_UrlGetToken " + MBB_UrlGetToken);
////        String MBB_TokenAuthor = MSysConfig.getValue("MBB_TokenAuthor", 0, Env.getAD_Client_ID(Env.getCtx())) ;
//        String MBB_TokenAuthor = D_MBB_TOKEN_AUTHOR;
//
//
//        //create URL object
//        URL urlToken = new URL( MBB_UrlGetToken );
//
//        String accessCode = MBB_TokenAuthor;
//
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(10000, TimeUnit.MILLISECONDS)
//                .readTimeout(10000, TimeUnit.MILLISECONDS).build();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
//        Request request = new Request.Builder()
//                .url(urlToken)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Authorization", accessCode)
//                .build();
//        log.info("request token: " + request.toString());
//
//        String result=null;
//        try (Response response = client.newCall(request).execute()) {
//            result = response.body().string();
//            log.info("GenToken: " + result);
//
//        } catch (Exception e) {
//            log.info("Exception Gen Token: " + e);
//            e.printStackTrace();
//        }
//
//        try {
//            JSONObject myres = new JSONObject(result);
//            return ((String) myres.get("access_token"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null ;
//
//    }//GetMBBToken

    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public String GetQR_MBB(int p_org_id, int pos_order_id, String p_token, BigDecimal p_amount,
                            PayMethodOrg payMethodOrg) throws IOException{

        if (p_amount.compareTo(BigDecimal.ZERO)<=0)
            return "";
//        disableSslVerification();
        String UrlCreate = getValueByName(D_MBB_URL_PRE) + getValueByName(D_MBB_URL_CREATEQR);
        log.info("UrlCreate " + UrlCreate);

        UUID myuuid = UUID.randomUUID();

        String p_terminalID = payMethodOrg.getTerminalId();
        String p_username = payMethodOrg.getAccessCode();
        String p_secretKey = payMethodOrg.getHashKey();

        log.info("Create QRCode MBB");
        log.info("tenantId: " + AuditContext.getAuditInfo().getMainTenantId());
        log.info("pos_order_id: " + pos_order_id);
        log.info("timestamp: " + Instant.now().getEpochSecond());

        String refenrenceLabel = Encoder.encode(AuditContext.getAuditInfo().getMainTenantId(),pos_order_id, Instant.now().getEpochSecond());
        String reqStr = "{"
                + "\"terminalID\": \""+ p_terminalID +"\","
                + "\"qrcodeType\": \""+ "4" +"\","
                + "\"initMethod\": \""+ "12" +"\","
                + "\"transactionAmount\": \"" + p_amount.toString() +"\","
                + "\"referenceLabelCode\": \"" + refenrenceLabel +"\","
                + "\"additionalAddress\": \"" + "" +"\","
                + "\"additionalMobile\": \""+ "" +"\","
                + "\"partnerType\": \""+ "2" +"\","
                + "\"transactionPurpose\": \""+ "CKPM" +"\","
                + "\"additionalEmail\": \""+ "" +"\""
                + "}";
        log.info("reqStr " + reqStr);
//
        log.info("secretKey: " + p_secretKey);
        log.info("username: " + p_username);
        log.info("Authorization: " + "Bearer " +p_token);

        URL url = new URL(UrlCreate);
        System.out.println(UrlCreate);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("clientMessageId",  myuuid.toString());
        conn.setRequestProperty("secretKey", p_secretKey);
        conn.setRequestProperty("username", p_username);
        conn.setRequestProperty("Authorization", "Bearer " +p_token);
        OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
        os.write(reqStr);
        os.flush();



        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
//            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() +" - Err MSG: "+ conn.getResponseMessage());
            log.info("Failed : HTTP error code : " + conn.getResponseCode() +" - Err MSG: "+ conn.getResponseMessage());
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));

        String output="";
        StringBuilder sb = new StringBuilder();

        while ((output = br.readLine()) != null) {
            sb.append(output);
            output = output + br.readLine();
        }
        System.out.println(sb.toString());
        br.close();
        conn.disconnect();
        JSONParser parser = new JSONParser();
        try {
            JSONObject myres =  (JSONObject) parser.parse(sb.toString());
            String qr_status = (String) myres.get("errorCode");
            if (qr_status.equals("000")) {
                JSONObject data = (JSONObject) parser.parse(myres.get("data").toString());
                return (String) data.get("qrcode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;


    }

    public String GetMBBToken(int p_method_id) throws IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, DecoderException{


        InputStreamReader read;

        String MBB_UrlGetToken = getValueByName(D_MBB_URL_PRE) + getValueByName(D_MBB_URL_GETTOKEN);


        log.info("MBB_UrlGetToken " + MBB_UrlGetToken);
        String MBB_TokenAuthor = getValueByName(D_MBB_TOKEN_AUTHOR);


        //create URL object
        URL urlToken = new URL( MBB_UrlGetToken );

        String accessCode = MBB_TokenAuthor;


        URL url = new URL(MBB_UrlGetToken);
        System.out.println(MBB_UrlGetToken);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", accessCode);
        // Prepare the request body
        String body = "grant_type=client_credentials";

        // Write the request body to the output stream
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }


//        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
////            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() +" - Err MSG: "+ conn.getResponseMessage());
//            return null;
//        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));

        String output="";
        StringBuilder sb = new StringBuilder();

        while ((output = br.readLine()) != null) {
            sb.append(output);
            output = output + br.readLine();
        }
        System.out.println(sb.toString());
        br.close();
        conn.disconnect();
        JSONParser parser = new JSONParser();
        try {
            JSONObject myres =  (JSONObject) parser.parse(sb.toString());
            return ((String) myres.get("access_token"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;


    }//GetMBBToken


    public UpdateOrderMBBDto MBBQueryTrans(int p_org_id, int pos_order_id, String p_token,
                            PayMethodOrg payMethodOrg) throws IOException{


        String UrlCreate = getValueByName(D_MBB_URL_CHECK_ORDER) ;
        log.info("UrlCheck Order " + UrlCreate);

        UUID myuuid = UUID.randomUUID();

        String p_checkSumKey = getValueByName(D_MBB_CHECKSUM);
        String p_terminalID = payMethodOrg.getTerminalId();
        String p_username = payMethodOrg.getAccessCode();
        String p_secretKey = payMethodOrg.getHashKey();
        String p_shortname = payMethodOrg.getMerchantCode();
        String checkStr = getMD5(pos_order_id+p_shortname+p_checkSumKey);
        String reqStr = "{"
                + "\"traceTransfer\": \""+ "" +"\","
                + "\"billNumber\": \""+ "" +"\","
                + "\"referenceLabel\": \""+ pos_order_id +"\","
                //+ "\"MerchantShortName\": \"" + p_shortname +"\","
                + "\"terminalID\": \"" + p_terminalID +"\","
                + "\"checkSum\": \""+ checkStr +"\""
                + "}";
        log.info("reqStr " + reqStr);
//
        URL url = new URL(UrlCreate);
        System.out.println(UrlCreate);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("clientMessageId",  myuuid.toString());
        conn.setRequestProperty("secretKey", p_secretKey);
        conn.setRequestProperty("username", p_username);
        conn.setRequestProperty("Authorization", "Bearer " +p_token);
        OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
        os.write(reqStr);
        os.flush();

//        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
//          throw new PerstingObjectException("Update Order MBB failed");
//        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output="";
        StringBuilder sb = new StringBuilder();

        while ((output = br.readLine()) != null) {
            sb.append(output);
            output = output + br.readLine();
        }
        System.out.println(sb.toString());
        br.close();
        conn.disconnect();
        JSONParser parser = new JSONParser();
        try {
            JSONObject myres =  (JSONObject) parser.parse(sb.toString());
            String qr_status = (String) myres.get("errorCode");
            if (qr_status.equals("000")) {
                JSONObject data =  (JSONObject) parser.parse(myres.get("data").toString());

                String res_resCode = (String) data.get("respCode");
                String traceTransfer = (String) data.get("traceTransfer");
                String storeLabel = (String) data.get("storeLabel");
                String terminalLabel = (String) data.get("terminalLabel");
                String debitAmount = (String) data.get("debitAmount");
                String realAmount = String.valueOf(data.get("realAmount"));
                String payDate = (String) data.get("payDate");
                String respDesc = (String) data.get("respDesc");
                String checkSum = (String) data.get("checkSum");
                String rate = (String) data.get("rate");
                String ftCode = (String) data.get("ftCode");
                UpdateOrderMBBDto updateOrderMBBDto = new UpdateOrderMBBDto();
                updateOrderMBBDto.setTraceTransfer(traceTransfer);
                updateOrderMBBDto.setStoreLabel(storeLabel);
                updateOrderMBBDto.setTerminalLabel(terminalLabel);
                updateOrderMBBDto.setDebitAmount(debitAmount);
                updateOrderMBBDto.setRealAmount(realAmount);
                updateOrderMBBDto.setPayDate(payDate);
                updateOrderMBBDto.setRespCode(res_resCode);
                updateOrderMBBDto.setRespDesc(respDesc);
                updateOrderMBBDto.setCheckSum(checkSum);
                updateOrderMBBDto.setRate(rate);
                updateOrderMBBDto.setFtCode(ftCode);

                return updateOrderMBBDto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;


    }

    public String getValueByName (String name){
        GlobalReponse  serviceDto  = restTemplate.getForObject(
                AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_API_VALUE_BY_NAME_URL+"/" + name,
                GlobalReponse.class);
        return (String) serviceDto.getData();
    }

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String OUTPUT_FORMAT = "%-20s:%s";

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String getMD5(String p_text) {
        byte[] md5InBytes = digest(p_text.getBytes(UTF_8));
        System.out.println(String.format(OUTPUT_FORMAT, "MD5 (hex) ", bytesToHex(md5InBytes)));
        return bytesToHex(md5InBytes);
    }

    private static byte[] digest(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }

}
