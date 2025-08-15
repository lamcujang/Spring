package com.dbiz.app.proxyclient.business.order.service;


import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.dto.orderDto.ReturnOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "roClientService", path = "/order-service/api/v1/ro")
public interface ReturnOrderClientService {

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap ReturnOrderRequest request);

    @GetMapping("/detail")
    public ResponseEntity<GlobalReponse> findAllDetail(@SpringQueryMap RODetailVRequest request);

    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final ReturnOrderDto DTO);

    @GetMapping("/{roId}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("roId") final Integer roId);

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> deleteReturnOrder(@PathVariable("id") final Integer id);


    @GetMapping("/pos")
    public ResponseEntity<GlobalReponsePagination> findPosOrdersRetail(@SpringQueryMap PosOrderListQueryRequest DTO);

    @PostMapping("/pos/detail")
    public ResponseEntity<GlobalReponse> findPosOrderRetailById(@RequestBody PosOrderReqDto DTO);

    @GetMapping("/po")
    public ResponseEntity<GlobalReponsePagination > findPO(@SpringQueryMap POHeaderVRequest  request);

    @GetMapping("/po/detail")
    public ResponseEntity<GlobalReponse> findPODetail(@SpringQueryMap PODetailVRequest  request);
}
