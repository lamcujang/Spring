package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.ConfigClientService;
import com.dbiz.app.proxyclient.business.system.service.ProvincesClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.CountryRequest;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.ProvincesRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/provinces")
@Slf4j
@RequiredArgsConstructor
public class ProvincesController {

    private final ProvincesClientService clientService;

    @GetMapping("/getArea")
    GlobalReponse getArea(@SpringQueryMap ProvincesRequest rq){
        return clientService.getArea(rq);
    }

    @GetMapping("/getWards")
    public GlobalReponse getWards(@SpringQueryMap ProvincesRequest request){
        return clientService.getWards(request);
    }

    @GetMapping("/getProvinces")
    public GlobalReponse getProvinces(@SpringQueryMap ProvincesRequest request)
    {
        return clientService.getProvinces(request);
    }

    @GetMapping("/getDistricts")
    public GlobalReponse getDistricts(@ModelAttribute ProvincesRequest request)
    {
        log.info("get Districts resource");
        return clientService.getDistricts(request);
    }

    @GetMapping("/getCountry")
    public GlobalReponsePagination getCountry(@SpringQueryMap CountryRequest request)
    {
        log.info("get Country resource");
        return clientService.getCountry(request);
    }
}
