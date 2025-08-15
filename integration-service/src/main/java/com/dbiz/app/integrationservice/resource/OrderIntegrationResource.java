package com.dbiz.app.integrationservice.resource;

import com.dbiz.app.integrationservice.service.OrderIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.voucher.VoucherParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/orderIntegration"})
@Slf4j
@RequiredArgsConstructor
public class OrderIntegrationResource {
    private final OrderIntegrationService orderIntegrationService;

    @PostMapping
    public ResponseEntity<GlobalReponse> getOrders(@RequestBody SyncIntegrationCredential dto) {
        log.info("** getVoucherServices **");
        GlobalReponse globalReponse = GlobalReponse.builder()
                .data(this.orderIntegrationService.getOrders(dto))
                .build();
        return ResponseEntity.ok(globalReponse);
    }

    @PostMapping("/product")
    public ResponseEntity<GlobalReponse> getOrderMains(@RequestBody SyncIntegrationCredential dto) {
        log.info("** getVoucherServices **");
        GlobalReponse globalReponse = GlobalReponse.builder()
                .data(this.orderIntegrationService.getOrdersMainByPosOrder(dto, dto.getPosOrderId()))
                .build();
        return ResponseEntity.ok(globalReponse);
    }
}
