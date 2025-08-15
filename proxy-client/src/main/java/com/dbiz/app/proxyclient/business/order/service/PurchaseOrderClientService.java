package com.dbiz.app.proxyclient.business.order.service;


import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PODetailVRequest;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "poClientService", path = "/order-service/api/v1/po")
public interface PurchaseOrderClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap POHeaderVRequest request);

    @GetMapping("/findAllDetail")
    public ResponseEntity<GlobalReponsePagination> findAllDetail(@SpringQueryMap PODetailVRequest  request);

    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final PurchaseOrderDto  DTO);

    @GetMapping("/{poId}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("poId") final Integer poId);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deletePurchaseOrder(@PathVariable("id") final Integer id);
}
