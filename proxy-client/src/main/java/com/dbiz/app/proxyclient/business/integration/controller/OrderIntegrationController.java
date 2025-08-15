package com.dbiz.app.proxyclient.business.integration.controller;

import com.dbiz.app.proxyclient.business.integration.service.ErpIntegrationClientService;
import com.dbiz.app.proxyclient.business.integration.service.OrderIntegrationClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orderIntegration")
@Slf4j
@RequiredArgsConstructor
public class OrderIntegrationController {

    private final OrderIntegrationClientService clientService;
    @PostMapping
    public ResponseEntity<GlobalReponse> getOrders(@RequestBody SyncIntegrationCredential request) {
        log.info("*** ErpIntegration, resource; findAll erpIntegration ***");

        return ResponseEntity.ok(this.clientService.getOrders(request).getBody());
    }

    @PostMapping("/product")
    public ResponseEntity<GlobalReponse> getOrderMains(@RequestBody SyncIntegrationCredential dto) {
        log.info("*** ErpIntegration, resource; findAll erpIntegration ***");

        return ResponseEntity.ok(this.clientService.getOrderMains(dto).getBody());
    }
}
