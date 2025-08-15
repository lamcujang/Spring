package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.ConfigClientService;
import com.dbiz.app.proxyclient.business.system.service.NotificationClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ClearCacheDto;
import org.common.dbiz.dto.systemDto.NotificationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.NotificationQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notify")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationClientService clientService;

    @GetMapping("/findAll")
    public GlobalReponsePagination getAllNotify(@SpringQueryMap NotificationQueryRequest request)
    {
        log.info("*** Notification List, resource; fetch all notification *");
        return this.clientService.getAllNotify(request);

    }
    @PostMapping("/sendNotify")
    public GlobalReponse sendNotify(@RequestBody SendNotification request) {
        log.info("*** Notification List, resource; fetch all notification *");
        return this.clientService.sendNotify(request);
    }


    @PutMapping("/update")
    public GlobalReponse update(@RequestBody NotificationDto request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.clientService.update(request);
    }

    @PostMapping("/clearCacheFE")
    public GlobalReponse clearCacheFE(@RequestBody ClearCacheDto request) {
        log.info("*** notify resource, clear cache FE*");
        return this.clientService.clearCacheFE(request);
    }


    @PostMapping("/updateStatusNotification")
    public GlobalReponse updateStatusNotification(@RequestBody NotificationDto request) {
        log.info("*** notify resource, update status notification*");
        return this.clientService.updateStatusNotification(request);
    }
}
