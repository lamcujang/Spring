package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.systemDto.ClearCacheDto;
import org.common.dbiz.dto.systemDto.NotificationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.NotificationQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "notifyClientService", path = "/system-service/api/v1/notify", decode404 = true
        , configuration = FeignClientConfig.class )
public interface NotificationClientService {

    @GetMapping("/findAll")
    public GlobalReponsePagination getAllNotify(@SpringQueryMap NotificationQueryRequest request);


    @PostMapping("/sendNotify")
    public GlobalReponse sendNotify(@RequestBody SendNotification request) ;


    @PutMapping("/update")
    public GlobalReponse update(@RequestBody NotificationDto request) ;

    @PostMapping("/clearCacheFE")
    public GlobalReponse clearCacheFE(@RequestBody ClearCacheDto request);

    @PostMapping("/updateStatusNotification")
    public GlobalReponse updateStatusNotification(@RequestBody NotificationDto request);
}
