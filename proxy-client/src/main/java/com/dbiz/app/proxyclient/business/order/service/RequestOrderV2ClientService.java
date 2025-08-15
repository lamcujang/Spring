package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "requestOrderV2ClientService", path = "/order-service/api/v2/requestOrder")
public interface RequestOrderV2ClientService {

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@Valid @RequestBody RequestOrderDto rq);
}
