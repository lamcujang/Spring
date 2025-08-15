package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "uomClientService", path = "/product-service/api/v1/uom")
public interface UomClientService {
//	@GetMapping("/{productId}")
//	ResponseEntity<GlobalReponse> findById(@PathVariable("productId") final Integer productId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name="orgId",required = false) Integer orgId,

			@RequestParam(defaultValue = "0",name = "page", required = false) Integer page,
			@RequestParam(defaultValue = "20",name = "pageSize", required = false) Integer pageSize
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final UomDto  uomDto);


	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer uomId);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final UomDto uomDto);

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);
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










