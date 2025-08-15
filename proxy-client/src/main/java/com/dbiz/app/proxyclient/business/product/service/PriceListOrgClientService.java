package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.PriceListOrgDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "priceListOrgClientService", path = "/product-service/api/v1/priceListOrg")
public interface PriceListOrgClientService {
	@GetMapping("/{priceListOrgId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("priceListOrgId") final Integer priceListOrgId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@RequestParam(name = "isActive", required = false) String isActive,
			@RequestParam(name = "isAll", required = false) String isAll,
			@RequestParam(name = "pricelistId", required = false) Integer pricelistId,
			@RequestParam(name="orgId",required = false) Integer orgId,

			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "pageSize", required = false) Integer pageSize
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PriceListOrgDto  DTO);


	@DeleteMapping("/delete/{priceListOrgId}")
	ResponseEntity<GlobalReponse> delete(@PathVariable("priceListOrgId") final Integer priceListOrgId);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PriceListOrgDto DTO);

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










