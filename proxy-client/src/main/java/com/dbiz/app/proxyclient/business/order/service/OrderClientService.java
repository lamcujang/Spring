package com.dbiz.app.proxyclient.business.order.service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ORDER-SERVICE", contextId = "orderClientService", path = "/order-service/api/v1/orders")
public interface OrderClientService {
	
	@GetMapping
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final OrderQueryRequest  entityQueryRequest);
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> findById(
			@PathVariable("orderId") 
			@NotBlank(message = "Input must not be blank!") 
			@Valid final String orderId);

	@PostMapping
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final OrderDto DTO);

	@PostMapping("/updateOrderMBB")
	public ResponseEntity<String> updateOrderMBB(@RequestBody @Valid final UpdateOrderMBBDto DTO);
	
	@PutMapping
	public ResponseEntity<OrderDto> update(
			@RequestBody 
			@NotNull(message = "Input must not be NULL!") 
			@Valid final OrderDto orderDto);
	
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDto> update(
			@PathVariable("orderId")
			@NotBlank(message = "Input must not be blank!")
			@Valid final String orderId,
			@RequestBody 
			@NotNull(message = "Input must not be NULL!") 
			@Valid final OrderDto orderDto);
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Boolean> deleteById(@PathVariable("orderId") final String orderId);
	
}










