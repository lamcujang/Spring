package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.PosOrderLineClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PosOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posOrderLine")
@RequiredArgsConstructor
@Slf4j
public class PosOrderLineController {
    private final PosOrderLineClientService entityClientService;

    @GetMapping("/{posOrderLineId}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("posOrderLineId") final Integer posOrderLineId){
        log.info("*** PosOrderLine, controller; fetch PosOrderLine By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(posOrderLineId).getBody());
    };

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @RequestParam(name = "orderId", required = false) Integer orderId,
            @RequestParam(name = "productId", required = false) Integer productId,
            @RequestParam(name = "isActive", required = false) String isActive,
            @RequestParam(name = "orgId", required = false) Integer orgId,

            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ){
        log.info("*** PosOrderLine, controller; fetch PosOrderLine All *");
        return ResponseEntity.ok(this.entityClientService.findAll(orderId, productId,isActive,orgId, page, pageSize).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final PosOrderlineDto entityDto) {
        log.info("*** PosOrderLine, controller; save PosOrderLine *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{posOrderLineId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("posOrderLineId") final Integer id) {
        log.info("*** PosOrderLine, controller; delete PosOrderLine By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }

    @PutMapping("/delete/withReason")
    public ResponseEntity<GlobalReponse> deleteWithReason(@RequestBody final PosOrderlineDto DTO) {
        log.info("*** PosOrderLine, controller; delete PosOrderLine with reason *");
        return ResponseEntity.ok(this.entityClientService.deleteWithReason(DTO).getBody());
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final PosOrderlineDto entityDto) {
        log.info("*** PosOrderLine, controller; update PosOrderLine By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }
}
