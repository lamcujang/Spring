package com.dbiz.app.proxyclient.business.system.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "docTypeClientService", path = "/system-service/api/v1/docType", decode404 = true
        , configuration = FeignClientConfig.class )
public interface DoctypeClientService {

    @GetMapping("/{code}")
    public GlobalReponse getDoctypeByCode(@PathVariable String code);
}
