package com.dbiz.app.systemservice.resource;


import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.payload.GlobalReponse;
import com.dbiz.app.systemservice.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/config"})
@Slf4j
@RequiredArgsConstructor
public class ConfigResource {

    private final ConfigService service;

    @GetMapping("/findByName/{name}")
    public GlobalReponse findValueByName(@PathVariable(value = "name") String name) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.service.findValueByName(name);
    }

    @GetMapping("/getEMenuConfig")
    public GlobalReponsePagination getEMenuConfig(@ModelAttribute EMenuGetUrlQueryRequest request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.service.getEMenuConfig(request);
    }


    @PostMapping("/sendNotify")
    public GlobalReponse sendNotify(@RequestBody SendNotification request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.service.sendNotify(request);
    }

    @PostMapping("/napas")
    public GlobalReponse getNapasConfig(@RequestBody NapasConfigReqDto dto) {
        log.info("*** Get Napas config *");
        return this.service.getNapasConfig(dto);
    }



}
