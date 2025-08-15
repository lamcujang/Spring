package com.dbiz.app.orderservice.resource;

import com.dbiz.app.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderResource {
	
	private final OrderService orderService;


	@GetMapping
	public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final OrderQueryRequest  entityQueryRequest) {
		log.info("*** Order, controller; fetch Order all *");
		return ResponseEntity.ok(this.orderService.findAll(entityQueryRequest));
	}

	
	@PostMapping
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final OrderDto  DTO) {
		log.info("*** OrderDto, resource; save order *");
		return ResponseEntity.ok(this.orderService.save(DTO));
	}

	@PostMapping("/updateOrderMBB")
	public ResponseEntity<String> updateOrderMBB(@RequestBody @Valid final UpdateOrderMBBDto  DTO) {
		log.info("*** UpdateOrderMBBDto, resource; updateOrderMBB *");
		return ResponseEntity.ok(this.orderService.updateOrderMBB(DTO));
	}

	
}










