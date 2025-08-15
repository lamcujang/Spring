package com.dbiz.app.proxyclient.business.product.service;


import com.dbiz.app.proxyclient.business.user.queryRequest.PartnerGroupQuery;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.ProductComboDto;
import org.common.dbiz.request.productRequest.ProductComboQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "productComboClientService", path = "/product-service/api/v1/productCombo")
public interface ProductComboClientService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer productId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final ProductComboQueryRequest  request
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final ProductComboDto  dto);


	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final ProductComboDto dto);

	@GetMapping("/component/{id}")
	ResponseEntity<GlobalReponse> findByProductIsComponentById(final Integer id);

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










