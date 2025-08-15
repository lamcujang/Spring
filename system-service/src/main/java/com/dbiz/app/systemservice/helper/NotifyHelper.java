package com.dbiz.app.systemservice.helper;

import com.dbiz.app.systemservice.repository.ConfigRepository;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotifyHelper {

    private final ConfigRepository configRepository;

    private final ResourceLoader resourceLoader;

    private final DataSourceContextHolder dataSourceContextHolder;

    public  String notifyFirebase(String deviceToken,String status, String Title, String content, String code, int recordID,String router,String type,String text) {
        dataSourceContextHolder.setCurrentTenantId(null);
        String d_FIREBASE_FCM_LINK = configRepository.findValueByNameAndTenantId("D_FIREBASE_FCM_LINK",0);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        JSONObject level = new JSONObject();
        JSONObject level1 = new JSONObject();
        JSONObject level2 = new JSONObject();
        JSONObject level3 = new JSONObject();
        try {
            // thay đổi sang token

            if (deviceToken != null && deviceToken != "") {

                List<JSONObject> list = new ArrayList<>();

                level1.put("token", String.valueOf(deviceToken));
                level2.put("body", content);
                level2.put("title", Title);
                list.add(level2);
                level1.put("notification", level2);
                level3.put("record_id", recordID > 0 ? String.valueOf(recordID) : "");
                level3.put("router", router);
                level3.put("type", type);
                level3.put("speak", text);
                level3.put("code", code);
                level3.put("status", status);
                level1.put("data", level3);
                level.put("message", level1);

                String token = getAccessToken();
                log.warn("Token: "+token);
                log.warn("json: {}"+ level);
                HttpRequest postRequest = HttpRequest.newBuilder()
                        .uri(new URI(d_FIREBASE_FCM_LINK))
                        .header("Authorization", "Bearer " + token).header("Content-Type", "application/json; UTF-8").header("Accept", "application/json; UTF-8")
                        .POST(BodyPublishers.ofString(level.toString())).timeout(Duration.ofSeconds(10)).build();

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
                int httpResponseCode = postResponse.statusCode();
                log.warn(httpResponseCode+"");

            }
        } catch (Throwable t) {

            t.printStackTrace();
            return "";
        }
        return level.toString();
    }

    public  String notifyFirebaseTopic(String topic) {
        dataSourceContextHolder.setCurrentTenantId(null);
        String d_FIREBASE_FCM_LINK = configRepository.findValueByNameAndTenantId("D_FIREBASE_FCM_LINK",0);
        JSONObject level = new JSONObject();
        JSONObject level1 = new JSONObject();
        JSONObject level3 = new JSONObject();
        try {
            List<JSONObject> list = new ArrayList<>();
            level3.put("type", "UPDATE-VERSION");
            level1.put("data", level3);
            level.put("topic", topic);
            list.add(level);
            list.add(level1);

            String token = getAccessToken();
            log.warn("Token: "+token);
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(d_FIREBASE_FCM_LINK))
                    .header("Authorization", "Bearer " + token).header("Content-Type", "application/json; UTF-8").header("Accept", "application/json; UTF-8")
                    .POST(BodyPublishers.ofString(level.toString())).timeout(Duration.ofSeconds(10)).build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
            int httpResponseCode = postResponse.statusCode();
            log.warn(httpResponseCode+"");
        } catch (Throwable t) {

            t.printStackTrace();
            return "";
        }
        return level.toString();
    }

    private  String getAccessToken() throws IOException {
        dataSourceContextHolder.setCurrentTenantId(null);
        String d_FIREBASE_ACCOUNT_PATH = configRepository.findValueByNameAndTenantId("D_FIREBASE_ACCOUNT_PATH",0);
        String d_FIREBASE_SCOPES_LINK = configRepository.findValueByNameAndTenantId("D_FIREBASE_SCOPES_LINK",0);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        try {
            Resource resource = resourceLoader.getResource("classpath:" + d_FIREBASE_ACCOUNT_PATH);
            if(resource.exists())
                System.out.println("File exists");
//            GoogleCredentials googleCredentials = GoogleCredentials
//                    .fromStream(new FileInputStream(resource.getFile())).createScoped(Arrays.asList(d_FIREBASE_SCOPES_LINK));
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(resource.getInputStream()).createScoped(Arrays.asList(d_FIREBASE_SCOPES_LINK));

            return  googleCredentials.refreshAccessToken().getTokenValue();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



}
