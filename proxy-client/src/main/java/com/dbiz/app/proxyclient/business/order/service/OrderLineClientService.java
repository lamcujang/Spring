package com.dbiz.app.proxyclient.business.order.service;


import org.common.dbiz.dto.orderDto.OrderLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderLineQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "orderLineClientService", path = "/order-service/api/v1/orderLines")
public interface OrderLineClientService {

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final OrderLineQueryRequest  entityQueryRequest);


    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final OrderLineListDto DTO);
}
