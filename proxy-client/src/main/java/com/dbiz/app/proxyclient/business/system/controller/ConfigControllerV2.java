package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.ConfigClientService;
import com.dbiz.app.proxyclient.business.system.service.ConfigClientServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/config")
@Slf4j
@RequiredArgsConstructor
public class ConfigControllerV2 {

    private final ConfigClientServiceV2 clientService;

    @GetMapping("/getEMenuConfig")
    public GlobalReponsePagination getEMenuConfig(@SpringQueryMap EMenuGetUrlQueryRequest request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.clientService.getEMenuConfig(request);
    }
    @GetMapping("/getParam")
    public GlobalReponse getParam(@RequestParam String param)
    {
        log.info("*** get param emenu ***");
        return this.clientService.getParam(param);
    }
}
