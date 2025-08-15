package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.TableClientService;
import com.dbiz.app.proxyclient.business.order.service.TableV2ClientService;
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
@RequestMapping("/api/v2/tables")
@RequiredArgsConstructor
@Slf4j
public class TableV2Controller {
    private final TableV2ClientService entityClientService;

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

    @GetMapping("/findAllTableAndReservationByDate")
    ResponseEntity<GlobalReponsePagination>findAllTableAndReservationByDate(@SpringQueryMap final TableQueryRequest tableQueryRequest){
        log.info("*** Table, controller; fetch Table And Reservation By Date *");
        return ResponseEntity.ok(this.entityClientService.findAllTableAndReservationByDate(tableQueryRequest).getBody());
    }
}
