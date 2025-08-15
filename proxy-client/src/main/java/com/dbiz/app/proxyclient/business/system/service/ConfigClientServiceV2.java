package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "configClientServiceV2", path = "/system-service/api/v2/config", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ConfigClientServiceV2 {
    @GetMapping("/getEMenuConfig")
    GlobalReponsePagination getEMenuConfig(@SpringQueryMap EMenuGetUrlQueryRequest request);

    @GetMapping("/getParam")
    GlobalReponse getParam(@RequestParam String param);
}
