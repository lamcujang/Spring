package com.dbiz.app.proxyclient.business.order.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dbiz.app.proxyclient.business.order.service.OrderClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderClientService orderClientService;

	@GetMapping
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final OrderQueryRequest  entityQueryRequest) {
		log.info("*** Order, controller; fetch Order all *");
		return ResponseEntity.ok(this.orderClientService.findAll(entityQueryRequest).getBody());
	}
	
//	@GetMapping("/{orderId}")
//	public ResponseEntity<OrderDto> findById(
//			@PathVariable("orderId")
//			@NotBlank(message = "Input must not be blank!")
//			@Valid final String orderId) {
//		return ResponseEntity.ok(this.orderClientService.findById(orderId).getBody());
//	}

	@PostMapping
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final OrderDto DTO) {
		log.info("*** OrderDto, resource; save order *");
		return ResponseEntity.ok(this.orderClientService.save(DTO).getBody());
	}

	@PostMapping("/updateOrderMBB")
	public ResponseEntity<String> updateOrderMBB(@RequestBody @Valid final UpdateOrderMBBDto  DTO) {
		log.info("*** UpdateOrderMBBDto, resource; updateOrderMBB *");
		return ResponseEntity.ok(this.orderClientService.updateOrderMBB(DTO).getBody());
	}
	
//	@PutMapping
//	public ResponseEntity<OrderDto> update(
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final OrderDto orderDto) {
//		return ResponseEntity.ok(this.orderClientService.update(orderDto).getBody());
//	}
//
//	@PutMapping("/{orderId}")
//	public ResponseEntity<OrderDto> update(
//			@PathVariable("orderId")
//			@NotBlank(message = "Input must not be blank!")
//			@Valid final String orderId,
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final OrderDto orderDto) {
//		return ResponseEntity.ok(this.orderClientService.update(orderId, orderDto).getBody());
//	}
//
//	@DeleteMapping("/{orderId}")
//	public ResponseEntity<Boolean> deleteById(@PathVariable("orderId") final String orderId) {
//		this.orderClientService.deleteById(orderId).getBody();
//		return ResponseEntity.ok(true);
//	}
	
	
	
}
