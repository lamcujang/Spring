package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.ConfigClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
@Slf4j
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigClientService clientService;

    @GetMapping("/findByName/{name}")
    public GlobalReponse findValueByName(@PathVariable(value = "name") String name) {
        log.info("*** Config , resource; fetch config by name *");
        return this.clientService.findValueByName(name);
    }
    @GetMapping("/getEMenuConfig")
    public GlobalReponsePagination getEMenuConfig(@SpringQueryMap EMenuGetUrlQueryRequest request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.clientService.getEMenuConfig(request);
    }

    @PostMapping("/sendNotify")
    public GlobalReponse sendNotify(@RequestBody SendNotification request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.clientService.sendNotify(request);
    }

    @GetMapping("/getParam")
    public GlobalReponse getParam(@RequestParam String param)
    {
        log.info("*** get param emenu ***");
        return this.clientService.getParam(param);
    }


    @PostMapping("/napas")
    public GlobalReponse getNapasConfig(@RequestBody NapasConfigReqDto dto) {
        log.info("*** Get Napas config *");
        return this.clientService.getNapasConfig(dto);
    }

}
