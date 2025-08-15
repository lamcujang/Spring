package com.dbiz.app.proxyclient.business.order.controller;


import com.dbiz.app.proxyclient.business.order.service.RequestOrderClientService;
import com.dbiz.app.proxyclient.business.order.service.RequestOrderV2ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/requestOrder")
@RequiredArgsConstructor
@Slf4j
public class RequestOrderV2Controller {

    private final RequestOrderV2ClientService entityClientService;

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@Valid @RequestBody RequestOrderDto rq) {
        log.info("RequestOrderResource::save");
        return ResponseEntity.ok().body(this.entityClientService.save(rq).getBody());
    }
}
