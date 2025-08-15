package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.RequestOrderClientService;
import com.dbiz.app.proxyclient.business.order.service.ReservationOrderClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.common.dbiz.request.orderRequest.SendNotifycationRq;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/requestOrder")
@RequiredArgsConstructor
@Slf4j
public class RequestOrderController {
    private final RequestOrderClientService entityClientService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap RequestOrderGetALlVQueryRequest request) {
        log.info("RequestOrderResource::findAll");
        return ResponseEntity.ok().body(this.entityClientService.findAll(request).getBody());
    }

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@Valid @RequestBody RequestOrderDto rq)
    {
        log.info("*** RequestOrder, controller; save RequestOrder *");
        return ResponseEntity.ok(this.entityClientService.saveAll(rq).getBody());
    }
    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@Valid @RequestBody RequestOrderDto rq) {
        log.info("RequestOrderResource::save");
        return ResponseEntity.ok().body(this.entityClientService.save(rq).getBody());
    }
    @GetMapping("/getHistoryRequestOrder")
    public ResponseEntity<GlobalReponse> getHistoryRequestOrder(@SpringQueryMap RequestOrderDto request) {
        log.info("RequestOrderResource::getHistoryRequestOrder");
        return ResponseEntity.ok().body(this.entityClientService.getHistoryRequestOrder(request).getBody());
    }

    @PostMapping("/sendNotify")
    public ResponseEntity<GlobalReponse> sendNotifyPayment(@RequestBody SendNotifycationRq request) {
        log.info("RequestOrderResource::sendNotifyPayment");
        return ResponseEntity.ok().body(this.entityClientService.sendNotify(request).getBody());
    }
}
