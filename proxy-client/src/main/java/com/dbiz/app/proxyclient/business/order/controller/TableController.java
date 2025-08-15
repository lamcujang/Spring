package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.TableClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
@Slf4j
public class TableController {
    private final TableClientService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id){
        log.info("*** Table, controller; fetch Table By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination > findAll(
            @SpringQueryMap final TableQueryRequest tableQueryRequest
    ){
        log.info("*** Table, controller; fetch Table All *");
        return ResponseEntity.ok(this.entityClientService.findAll(tableQueryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final TableDto entityDto) {
        log.info("*** Table, controller; save Table *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** Table, controller; delete Table By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final TableDto entityDto) {
        log.info("*** Table, controller; update Table By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @GetMapping("/findAllTableAndReservationByDate")
    ResponseEntity<GlobalReponsePagination>findAllTableAndReservationByDate(@SpringQueryMap final TableQueryRequest tableQueryRequest){
        log.info("*** Table, controller; fetch Table And Reservation By Date *");
        return ResponseEntity.ok(this.entityClientService.findAllTableAndReservationByDate(tableQueryRequest).getBody());
    }

    @GetMapping("/getByIdFromCashier")
    public ResponseEntity<GlobalReponse> getFromCashier(@RequestParam("id") final Integer id) {
        log.info("*** Table, resource; fetch Table from cashier *");
        return ResponseEntity.ok(this.entityClientService.getFromCashier(id).getBody());
    }
}
