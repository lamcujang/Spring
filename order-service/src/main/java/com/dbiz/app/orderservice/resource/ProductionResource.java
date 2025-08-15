package com.dbiz.app.orderservice.resource;

import com.dbiz.app.orderservice.service.OrderService;
import com.dbiz.app.orderservice.service.ProductionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.dto.orderDto.UpdateOrderMBBDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.common.dbiz.request.orderRequest.ProductionQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/productions")
@Slf4j
@RequiredArgsConstructor
public class ProductionResource {
	
	private final ProductionService service;


	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final ProductionQueryRequest entityQueryRequest) {
		log.info("*** Production Resource ; getAll *");
		return ResponseEntity.ok(this.service.findAll(entityQueryRequest));
	}

	
	@PostMapping("/save")
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final ProductionDto DTO) {
		log.info("*** ProdutionDto, resource; save production *");
		return ResponseEntity.ok(this.service.save(DTO));
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse > update(@RequestBody @Valid final ProductionDto DTO) {
		log.info("*** ProdutionDto, resource; update production *");
		return ResponseEntity.ok(this.service.save(DTO));
	}

	@DeleteMapping("/deleteLine/{productionLineId}")
	public ResponseEntity<GlobalReponse> deleteProductionLineById(@PathVariable("productionLineId") final Integer productionLineId) {
		log.info("*** Delete, controller; delete product  *");
		return ResponseEntity.ok(this.service.deleteProductionLineById(productionLineId));
	}

	
}










