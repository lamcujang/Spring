package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.CancelReasonService;
import com.dbiz.app.proxyclient.business.order.service.ReturnReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.orderDto.ReturnReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.common.dbiz.request.orderRequest.ReturnReasonQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/returnReasons")
@RequiredArgsConstructor
@Slf4j
public class ReturnReasonController {
    private final ReturnReasonService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id){
        log.info("*** ReturnReason, controller; fetch ReturnReason By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };

    @GetMapping
    ResponseEntity<GlobalReponsePagination > findAll(
            @SpringQueryMap final ReturnReasonQueryRequest returnReasonQueryRequest
            ){
        log.info("*** ReturnReason, controller; fetch ReturnReason All *");
        return ResponseEntity.ok(this.entityClientService.findAll(returnReasonQueryRequest).getBody());
    }


    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody final ReturnReasonDto entityDto) {
        log.info("*** ReturnReason, controller; save ReturnReason *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** ReturnReason, controller; delete ReturnReason By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final ReturnReasonDto entityDto) {
        log.info("*** ReturnReason, controller; update ReturnReason By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }
}
