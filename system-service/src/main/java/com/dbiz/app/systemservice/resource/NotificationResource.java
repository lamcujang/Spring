package com.dbiz.app.systemservice.resource;


import com.dbiz.app.systemservice.service.ConfigService;
import com.dbiz.app.systemservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ClearCacheDto;
import org.common.dbiz.dto.systemDto.NotificationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.NotificationQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/notify"})
@Slf4j
@RequiredArgsConstructor
public class NotificationResource {

    private final NotificationService service;
    @GetMapping("/findAll")
    public GlobalReponsePagination getEMenuConfig(@ModelAttribute NotificationQueryRequest request) {
        log.info("*** notify List, resource; fetch all notify *");
        return this.service.findAll(request);
    }


    @PostMapping("/sendNotify")
    public GlobalReponse sendNotify(@RequestBody SendNotification request) {
        log.info("*** notify resource, send notify*");
        return this.service.sendNotify(request);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody NotificationDto request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.service.save(request);
    }

    @GetMapping("/getMessage")
    public GlobalReponse getMessage(@RequestParam String key) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.service.getMessage(key);
    }

    @PostMapping("/clearCacheFE")
    public GlobalReponse clearCacheFE(@RequestBody ClearCacheDto request) {
        log.info("*** notify resource, clear cache FE*");
        return this.service.clearCacheFE(request);
    }

    @PostMapping("/updateStatusNotification")
    public GlobalReponse updateStatusNotification(@RequestBody NotificationDto request) {
        log.info("*** notify resource, update status notification*");
        return this.service.updateStatusNotification(request);
    }
}
