package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.ShiftControlDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ShiftControlQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "ShiftControllClientService", path = "/order-service/api/v1/shiftControl")
public interface ShiftControlClientService {
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final ShiftControlQueryRequest entityQueryRequest);

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@Valid @RequestBody final ShiftControlDto entityDto);

    @GetMapping("/findPosOrderRp/{shiftId}")
    ResponseEntity<GlobalReponse> findByShiftId(@PathVariable final Integer shiftId);

    @GetMapping("/findPaymentRp/{shiftId}")
    ResponseEntity<GlobalReponse> findPaymentByShiftId(@PathVariable final Integer shiftId);

    @GetMapping("/findPurchaseRp/{shiftId}")
    public ResponseEntity<GlobalReponse> findPurchaseByShiftId(@PathVariable final Integer shiftId);

    @GetMapping("/findReturnRp/{shiftId}")
    public ResponseEntity<GlobalReponse> findReturnByShiftId(@PathVariable final Integer shiftId);


    @GetMapping("/findShiftInfo/{shiftId}")
    public ResponseEntity<GlobalReponse> findShiftInfo(@PathVariable final Integer shiftId);

    @GetMapping("/findNewestShift/{orgId}/{posTerminalId}")
    public ResponseEntity<GlobalReponse> getNewestShiftInfo(@PathVariable final Integer orgId, @PathVariable final Integer posTerminalId);
}










