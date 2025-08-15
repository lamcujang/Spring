package com.dbiz.app.systemservice.resource;


import com.dbiz.app.systemservice.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v2/config"})
@Slf4j
@RequiredArgsConstructor
public class ConfigResourceV2 {

    private final ConfigService service;

    @GetMapping("/getEMenuConfig")
    public GlobalReponsePagination getEMenuConfig(@ModelAttribute EMenuGetUrlQueryRequest request) {
        log.info("*** Vendor List, resource; fetch all vendor *");
        return this.service.getEMenuConfigV2(request);
    }
    @GetMapping("/getParam")
    public GlobalReponse getParam(@RequestParam String param)
    {
        log.info("*** get param emenu ***");
        return this.service.getParamEmenu(param);
    }
}
