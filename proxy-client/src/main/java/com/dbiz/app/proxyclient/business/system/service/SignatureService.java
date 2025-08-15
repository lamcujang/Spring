package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "SYSTEM-SERVICE", contextId = "signatureClientService", path = "/system-service/api/v1/signature", decode404 = true
        , configuration = FeignClientConfig.class )
public interface SignatureService {


    @PostMapping("/napas/verify")
    public GlobalReponse verifyNapasData(@RequestBody PayloadNapasDto dto);

    @PostMapping("/napas/sign")
    public GlobalReponse signNapasData(@RequestBody PayloadNapasDto dto);
}
