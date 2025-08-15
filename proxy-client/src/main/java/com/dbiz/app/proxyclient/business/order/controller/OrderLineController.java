package com.dbiz.app.proxyclient.business.order.controller;


import com.dbiz.app.proxyclient.business.order.service.OrderLineClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderLineQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/orderLines")
@Slf4j
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineClientService orderLineClientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final OrderLineQueryRequest entityQueryRequest) {
        log.info("*** Order, controller; fetch Order all *");
        return ResponseEntity.ok(this.orderLineClientService.findAll(entityQueryRequest).getBody());
    }

    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final OrderLineListDto  DTO) {
        log.info("*** OrderDto, resource; save order *");
        return ResponseEntity.ok(this.orderLineClientService.save(DTO).getBody());
    }
}
