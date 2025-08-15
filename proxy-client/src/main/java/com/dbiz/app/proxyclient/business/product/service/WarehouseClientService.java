package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.ListWarehouseDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.WarehouseQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "warehouseClientService", path = "/product-service/api/v1/warehouses")
public interface WarehouseClientService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer warehouseId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap WarehouseQueryRequest warehouseQueryRequest  );

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final WarehouseDto  warehouseDto);


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final WarehouseDto warehouseDto);

	@PutMapping("/updateAll")
	public ResponseEntity<GlobalReponse> updateAll(@RequestBody ListWarehouseDto warehouseDto);

//	@GetMapping("/{productId}")
//	ResponseEntity<ProductDto> findById(
//			@PathVariable("productId")
//			@NotBlank(message = "Input must not be blank!")
//			@Valid final String productId);
//
//	@PostMapping
//	ResponseEntity<ProductDto> save(
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final ProductDto productDto);
//
//	@PutMapping
//	ResponseEntity<ProductDto> update(
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final ProductDto productDto);
//
//	@PutMapping("/{productId}")
//	ResponseEntity<ProductDto> update(
//			@PathVariable("productId")
//			@NotBlank(message = "Input must not be blank!")
//			@Valid final String productId,
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final ProductDto productDto);
//
//	@DeleteMapping("/{productId}")
//	ResponseEntity<Boolean> deleteById(@PathVariable("productId") final String productId);
//
}










