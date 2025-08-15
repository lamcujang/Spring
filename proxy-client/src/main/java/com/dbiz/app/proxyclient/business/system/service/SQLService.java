package com.dbiz.app.proxyclient.business.system.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "sqlClientService", path = "/system-service/api/v1/sql", decode404 = true
        , configuration = FeignClientConfig.class )
public interface SQLService {

    @PostMapping(path = "/modifyAll",consumes = MULTIPART_FORM_DATA_VALUE)
    public GlobalReponse modifyAll(@RequestPart("file") MultipartFile file);

    @PostMapping(path ="/grantAccessAll",consumes = MULTIPART_FORM_DATA_VALUE)
    public GlobalReponse grantAccessAll(@RequestPart("file") MultipartFile file);

    @PostMapping(path = "/clearTrialData")
    public GlobalReponse clearTrialData();
}
