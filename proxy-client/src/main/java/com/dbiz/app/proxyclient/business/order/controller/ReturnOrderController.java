package com.dbiz.app.proxyclient.business.order.controller;


import com.dbiz.app.proxyclient.business.order.service.PurchaseOrderClientService;
import com.dbiz.app.proxyclient.business.order.service.ReturnOrderClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.dto.orderDto.ReturnOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/ro")
@RequiredArgsConstructor
@Slf4j
public class ReturnOrderController {

    private final ReturnOrderClientService entityClientService;


    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ReturnOrderRequest request)
    {
        log.info("*** Purchase Order Header, resource; fetch all Purchase Order Header *");
        return ResponseEntity.ok(this.entityClientService.findAll(request).getBody());
    }

    @GetMapping("/{roId}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("roId") final Integer roId) {
        log.info("*** RO, resource; fetch RO by id {} *",roId);
        return ResponseEntity.ok(this.entityClientService.findById(roId).getBody());
    }

    @GetMapping("/detail")
    public ResponseEntity<GlobalReponse> findAll(@SpringQueryMap RODetailVRequest request)
    {
        log.info("*** Return Order Detail, resource; fetch all Return Order Detail *");
        return ResponseEntity.ok(this.entityClientService.findAllDetail(request).getBody());
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final ReturnOrderDto DTO) {
        log.info("*** Return Order, resource; save Return Order  *");
        return ResponseEntity.ok(this.entityClientService.save(DTO).getBody());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> deletePurchaseOrder(@PathVariable("id") final Integer id) {
        log.info("*** RO, resource; delete RO by id {} *",id);
        return ResponseEntity.ok(this.entityClientService.deleteReturnOrder(id).getBody());
    }


    @GetMapping("/pos")
    public ResponseEntity<GlobalReponsePagination> findPosOrdersRetail(@SpringQueryMap PosOrderListQueryRequest DTO) {
        log.info("*** PosOrder, resource; findPosOrdersRetail  *");
        return ResponseEntity.ok(this.entityClientService.findPosOrdersRetail(DTO)).getBody();
    }

    @PostMapping("/pos/detail")
    public ResponseEntity<GlobalReponse> findPosOrderRetailById(@RequestBody PosOrderReqDto DTO) {
        log.info("*** PosOrder, resource; findPosOrderRetailById  *");
        return ResponseEntity.ok(this.entityClientService.findPosOrderRetailById(DTO)).getBody();
    }

    @GetMapping("/po")
    public ResponseEntity<GlobalReponsePagination > findPO(@SpringQueryMap POHeaderVRequest  request)
    {
        log.info("*** Purchase Order Header, resource; fetch all Purchase Order Header *");
        return ResponseEntity.ok(this.entityClientService.findPO(request)).getBody();
    }

    @GetMapping("/po/detail")
    public ResponseEntity<GlobalReponse> findPODetail(@SpringQueryMap PODetailVRequest  request)
    {
        log.info("*** Purchase Order Detail, resource; fetch all Purchase Order Detail *");
        return ResponseEntity.ok(this.entityClientService.findPODetail(request)).getBody();
    }
}
