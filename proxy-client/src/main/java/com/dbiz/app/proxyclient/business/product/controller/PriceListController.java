package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.PriceListClientService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.JsonViewPriceListDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/priceLists")
@RequiredArgsConstructor
public class PriceListController {
	
	private final PriceListClientService clientService;
	@GetMapping("/{priceListId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("priceListId") final Integer priceListId){
		return ResponseEntity.ok(this.clientService.findById(priceListId).getBody());
	};
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap PriceListQueryRequest queryRequest) {
		return ResponseEntity.ok(this.clientService.findAll(queryRequest).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final PricelistDto DTO) {
		return ResponseEntity.ok(this.clientService.save(DTO).getBody());
	}

	@DeleteMapping("/delete/{priceListId}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListId") final Integer priceListId) {
		return ResponseEntity.ok(this.clientService.delete(priceListId).getBody());
	}


	@PutMapping("/update")

	public ResponseEntity<GlobalReponse> update(@RequestBody final PricelistDto DTO) {
		return ResponseEntity.ok(this.clientService.update(DTO).getBody());
	}
	@GetMapping("/cashier")
	public ResponseEntity<GlobalReponsePagination > findAllByCashier(@SpringQueryMap PriceListQueryRequest  request) {
		return ResponseEntity.ok(this.clientService.findAllByCashier(request).getBody());
	}


}




