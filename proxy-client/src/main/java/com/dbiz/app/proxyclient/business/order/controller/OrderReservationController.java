package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.ReservationOrderClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservationOrder")
@RequiredArgsConstructor
@Slf4j
public class OrderReservationController {
    private final ReservationOrderClientService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id){
        log.info("*** ReservationOrder, controller; fetch ReservationOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final ReservationOrderQueryRequest  request
    ){
        log.info("*** ReservationOrder, controller; fetch ReservationOrder All *");
        return ResponseEntity.ok(this.entityClientService.findAll(request).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final ReservationOrderDto  entityDto) {
        log.info("*** ReservationOrder, controller; save ReservationOrder *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** ReservationOrder, controller; delete ReservationOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final ReservationOrderDto entityDto) {
        log.info("*** ReservationOrder, controller; update ReservationOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @GetMapping("/findAllV")
    ResponseEntity<GlobalReponsePagination> findAllV(
            @SpringQueryMap final ReservationOrderQueryRequest request
    ){
        log.info("*** ReservationOrder, controller; fetch ReservationOrder All *");
        return ResponseEntity.ok(this.entityClientService.findAllV(request).getBody());
    }
}
