package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.common.dbiz.request.orderRequest.SendNotifycationRq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "requestOrderClientService", path = "/order-service/api/v1/requestOrder")
public interface RequestOrderClientService {

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap RequestOrderGetALlVQueryRequest request);



    @PostMapping("/saveAll")
    ResponseEntity<GlobalReponse> saveAll(@Valid @RequestBody RequestOrderDto rq);

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@Valid @RequestBody RequestOrderDto rq);

    @GetMapping("/getHistoryRequestOrder")
    ResponseEntity<GlobalReponse> getHistoryRequestOrder(@SpringQueryMap RequestOrderDto request);

    @PostMapping("/sendNotify")
    ResponseEntity<GlobalReponse> sendNotify(@RequestBody SendNotifycationRq request);

}










