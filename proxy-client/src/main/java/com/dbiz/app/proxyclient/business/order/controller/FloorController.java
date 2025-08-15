package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.FloorClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.FloorQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/floors")
@RequiredArgsConstructor
@Slf4j
public class FloorController {
    private final FloorClientService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id){
        log.info("*** Floor, controller; fetch Floor By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination > findAll(
            @SpringQueryMap final FloorQueryRequest  floorQueryRequest
            ){
        log.info("*** Floor, controller; fetch Floor All *");
        return ResponseEntity.ok(this.entityClientService.findAll(floorQueryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final FloorDto  entityDto) {
        log.info("*** Floor, controller; save Floor *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** Floor, controller; delete Floor By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final FloorDto entityDto) {
        log.info("*** Floor, controller; update Floor By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }
}
