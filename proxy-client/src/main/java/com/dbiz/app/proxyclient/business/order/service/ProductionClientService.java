package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.common.dbiz.request.orderRequest.ProductionQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FeignClient(name = "ORDER-SERVICE", contextId = "productionClientService", path = "/order-service/api/v1/productions")
public interface ProductionClientService {
	
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final ProductionQueryRequest entityQueryRequest);

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final ProductionDto DTO);

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final ProductionDto DTO);

	@DeleteMapping("/deleteLine/{productionLineId}")
	public ResponseEntity<GlobalReponse> deleteProductionLineById(@PathVariable("productionLineId") final Integer productionLineId);
	
}










