package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.PurchaseOrderService;
import com.dbiz.app.orderservice.service.ReturnOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.dto.orderDto.ReturnOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/ro")
@Slf4j
@RequiredArgsConstructor
public class ReturnOrderResource {

    private final ReturnOrderService service;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute ReturnOrderRequest request)
    {
        log.info("*** Purchase Order Header, resource; fetch all Purchase Order Header *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @GetMapping("/{roId}")
    public ResponseEntity<GlobalReponse > findById(@PathVariable("roId") final Integer poId) {
        log.info("*** RO, resource; fetch RO by id {} *",poId);
        return ResponseEntity.ok(this.service.findById(poId));
    }

    @GetMapping("/detail")
    public ResponseEntity<GlobalReponse> findAll(@ModelAttribute RODetailVRequest request)
    {
        log.info("*** Purchase Order Detail, resource; fetch all Purchase Order Detail *");
        return ResponseEntity.ok(this.service.findAllDetail(request));
    }

    @PostMapping()
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final ReturnOrderDto DTO) {
        log.info("*** Return Order, resource; save Return Order  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse > deleteReturnOrder(@PathVariable("id") final Integer id) {
        log.info("*** RO, resource; delete RO by id {} *",id);
        return ResponseEntity.ok(this.service.deleteById(id));
    }

    @GetMapping("/pos")
    public ResponseEntity<GlobalReponsePagination> findPosOrdersRetail(@ModelAttribute PosOrderListQueryRequest DTO) {
        log.info("*** PosOrder, resource; findPosOrdersRetail  *");
        return ResponseEntity.ok(this.service.findPosOrdersRetail(DTO));
    }

    @PostMapping("/pos/detail")
    public ResponseEntity<GlobalReponse> findPosOrderRetailById(@RequestBody PosOrderReqDto DTO) {
        log.info("*** PosOrder, resource; findPosOrderRetailById  *");
        return ResponseEntity.ok(this.service.findPosOrderRetailById(DTO));
    }

    @GetMapping("/po")
    public ResponseEntity<GlobalReponsePagination > findPO(@ModelAttribute POHeaderVRequest  request)
    {
        log.info("*** Purchase Order Header, resource; fetch all Purchase Order Header *");
        return ResponseEntity.ok(this.service.findPOAll(request));
    }

    @GetMapping("/po/detail")
    public ResponseEntity<GlobalReponse> findPODetail(@ModelAttribute PODetailVRequest  request)
    {
        log.info("*** Purchase Order Detail, resource; fetch all Purchase Order Detail *");
        return ResponseEntity.ok(this.service.findPODetail(request));
    }
}
