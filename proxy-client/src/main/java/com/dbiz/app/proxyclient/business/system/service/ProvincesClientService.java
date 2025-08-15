package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.CountryRequest;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.ProvincesRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "ProvincesClientService", path = "/system-service/api/v1/provinces", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ProvincesClientService {

    @GetMapping("/getArea")
    GlobalReponse getArea(@SpringQueryMap ProvincesRequest rq);


    @GetMapping("/getWards")
     GlobalReponse getWards(@SpringQueryMap ProvincesRequest request);

    @GetMapping("/getProvinces")
    GlobalReponse getProvinces(@SpringQueryMap ProvincesRequest request);

    @GetMapping("/getDistricts")
    public GlobalReponse getDistricts(@SpringQueryMap ProvincesRequest request);

    @GetMapping("/getCountry")
     GlobalReponsePagination getCountry(@SpringQueryMap CountryRequest request);
}
