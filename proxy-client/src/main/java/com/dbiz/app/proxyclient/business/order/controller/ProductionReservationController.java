package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.ProductionClientService;
import com.dbiz.app.proxyclient.business.order.service.ReservationOrderClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ProductionQueryRequest;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productions")
@RequiredArgsConstructor
@Slf4j
public class ProductionReservationController {
    private final ProductionClientService entityClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final ProductionQueryRequest request
    ) {
        log.info("*** Production, controller; fetch Production All *");
        return ResponseEntity.ok(this.entityClientService.findAll(request).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final ProductionDto entityDto) {
        log.info("*** Production, controller; save Production *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final ProductionDto entityDto) {
        log.info("*** Production, controller; update Production  *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @DeleteMapping("/deleteLine/{productionLineId}")
    public ResponseEntity<GlobalReponse> deleteProductionLineById(@PathVariable("productionLineId") final Integer productionLineId) {
        log.info("*** Delete, controller; delete product  *");
        return ResponseEntity.ok(this.entityClientService.deleteProductionLineById(productionLineId)).getBody();
    }
}
