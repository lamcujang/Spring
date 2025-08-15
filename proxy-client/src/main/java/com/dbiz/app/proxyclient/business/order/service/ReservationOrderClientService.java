package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "reservationOrderClientService", path = "/order-service/api/v1/reservationOrder")
public interface ReservationOrderClientService {
    @GetMapping("/{id}")
    ResponseEntity <GlobalReponse > findById(@PathVariable("id") final Integer id);

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination > findAll(
            @SpringQueryMap final ReservationOrderQueryRequest  request
    );

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody @Valid final ReservationOrderDto  entityDto);


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer id);

    @PutMapping("/update")
    ResponseEntity<GlobalReponse> update(@RequestBody @Valid final ReservationOrderDto entityDto);

    @GetMapping("/findAllV")
    ResponseEntity<GlobalReponsePagination> findAllV(
            @SpringQueryMap final ReservationOrderQueryRequest request
    );

}










