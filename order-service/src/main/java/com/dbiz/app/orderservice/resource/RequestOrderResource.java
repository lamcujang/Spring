package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.RequestOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.common.dbiz.request.orderRequest.SendNotifycationRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/requestOrder")
@Slf4j
@RequiredArgsConstructor
public class RequestOrderResource {

    private final RequestOrderService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute RequestOrderGetALlVQueryRequest request) {
        log.info("RequestOrderResource::findAll");
        return ResponseEntity.ok().body(this.service.findAll(request));
    }

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@Valid @RequestBody RequestOrderDto rq) {
        log.info("RequestOrderResource::saveAll");
        return ResponseEntity.ok().body(this.service.saveAll(rq));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@Valid @RequestBody RequestOrderDto rq) {
        log.info("RequestOrderResource::save");
        return ResponseEntity.ok().body(this.service.save(rq));
    }

    @GetMapping("/getHistoryRequestOrder")
    public ResponseEntity<GlobalReponse> getHistoryRequestOrder(@ModelAttribute RequestOrderDto request) {
        log.info("RequestOrderResource::getHistoryRequestOrder");
        return ResponseEntity.ok().body(this.service.getHistoryRequestOrder(request));
    }

    @PostMapping("/sendNotify")
    public ResponseEntity<GlobalReponse> sendNotifyPayment(@RequestBody SendNotifycationRq request) {
        log.info("RequestOrderResource::sendNotifyPayment");
        return ResponseEntity.ok().body(this.service.sendNotify(request));
    }


}
