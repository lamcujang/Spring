package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.PriceListProductClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.PriceListProductDto;
import org.common.dbiz.dto.productDto.request.PriceListProductReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ApplyNewFormulaPriceRequest;
import org.common.dbiz.request.productRequest.FindAllPriceListAndProductRequest;
import org.common.dbiz.request.productRequest.PriceListProductQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/priceListProduct")
@RequiredArgsConstructor
public class PriceListProductController {
	
	private final PriceListProductClientService clientService;
	@GetMapping("/{priceListProductId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("priceListProductId") final Integer priceListProductId){
		return ResponseEntity.ok(this.clientService.findById(priceListProductId).getBody());
	};
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final PriceListProductQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final PriceListProductDto DTO) {
		return ResponseEntity.ok(this.clientService.save(DTO).getBody());
	}

	@DeleteMapping("/delete/{priceListProductId}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListProductId") final Integer priceListProductId) {
		return ResponseEntity.ok(this.clientService.delete(priceListProductId).getBody());
	}


	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final PriceListProductDto DTO) {
		return ResponseEntity.ok(this.clientService.update(DTO).getBody());
	}

	@PostMapping("/deleteAllByIds")
	public ResponseEntity<GlobalReponse> deleteAllByIds(@RequestBody final PriceListProductDto.IdsRequest ids) {
		return ResponseEntity.ok(this.clientService.deleteAllByIds(ids).getBody());
	}

	@PostMapping("/applyAllNewFormulaPrice")
	public ResponseEntity<GlobalReponse> applyAllNewFormulaPrice(@RequestBody final ApplyNewFormulaPriceRequest request) {
		return ResponseEntity.ok(this.clientService.applyAllNewFormulaPrice(request).getBody());
	}

	@PostMapping("/deleteAllByPriceListId")
	public ResponseEntity<GlobalReponse> deleteAllByPriceListId(@RequestBody final ApplyNewFormulaPriceRequest request) {
		return ResponseEntity.ok(this.clientService.deleteAllByPriceListId(request).getBody());
	}

	@GetMapping("/findAllProductAndPriceList")
	public ResponseEntity<GlobalReponsePagination> findAllProductAndPriceList(@SpringQueryMap final FindAllPriceListAndProductRequest request) {
		return ResponseEntity.ok(this.clientService.findAllProductAndPriceList(request).getBody());
	}

	@GetMapping("/findAllPriceListProduct")
	public ResponseEntity<GlobalReponsePagination> findAllPriceListProduct(@ModelAttribute PriceListProductReqDto request) {
		return ResponseEntity.ok(this.clientService.findAllPriceListProduct(request).getBody());
	}
	
}




