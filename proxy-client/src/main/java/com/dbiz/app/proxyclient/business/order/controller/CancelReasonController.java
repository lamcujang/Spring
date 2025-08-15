package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.CancelReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cancelReasons")
@RequiredArgsConstructor
@Slf4j
public class CancelReasonController {
    private final CancelReasonService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id){
        log.info("*** CancelReason, controller; fetch CancelReason By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination > findAll(
            @SpringQueryMap final CancelReasonQueryRequest  CancelReasonQueryRequest
            ){
        log.info("*** CancelReason, controller; fetch CancelReason All *");
        return ResponseEntity.ok(this.entityClientService.findAll(CancelReasonQueryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final CancelReasonDto  entityDto) {
        log.info("*** CancelReason, controller; save CancelReason *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** CancelReason, controller; delete CancelReason By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final CancelReasonDto entityDto) {
        log.info("*** CancelReason, controller; update CancelReason By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }
}
