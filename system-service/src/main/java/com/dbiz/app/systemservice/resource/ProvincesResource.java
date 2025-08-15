package com.dbiz.app.systemservice.resource;

import com.dbiz.app.systemservice.service.NotificationService;
import com.dbiz.app.systemservice.service.ProvincesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.CountryRequest;
import org.common.dbiz.request.systemRequest.ProvincesRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/provinces"})
@Slf4j
@RequiredArgsConstructor
public class ProvincesResource {

    private final ProvincesService service;
    @GetMapping("/getArea")
    public GlobalReponse getArea(@ModelAttribute ProvincesRequest request)
    {
        log.info("getArea resource");
        return service.getArea(request);
    }

    @GetMapping("/getWards")
    public GlobalReponse getWards(@ModelAttribute ProvincesRequest request)
    {
        log.info("getArea resource");
        return service.getWard(request);
    }

    @GetMapping("/getProvinces")
    public GlobalReponse getProvinces(@ModelAttribute ProvincesRequest request)
    {
        log.info("getArea resource");
        return service.getProvinces(request);
    }

    @GetMapping("/getDistricts")
    public GlobalReponse getDistricts(@ModelAttribute ProvincesRequest request)
    {
        log.info("getArea resource");
        return service.getDistrict(request);
    }

    @GetMapping("/getCountry")
    public GlobalReponsePagination getCountry(@ModelAttribute CountryRequest request)
    {
        log.info("getArea resource");
        return service.getCountry(request);
    }
}
