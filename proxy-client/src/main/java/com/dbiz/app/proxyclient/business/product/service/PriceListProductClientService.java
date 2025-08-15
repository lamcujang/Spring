package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.request.PriceListProductReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.PriceListProductDto;
import org.common.dbiz.request.productRequest.ApplyNewFormulaPriceRequest;
import org.common.dbiz.request.productRequest.FindAllPriceListAndProductRequest;
import org.common.dbiz.request.productRequest.PriceListProductQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "priceListProductClientService", path = "/product-service/api/v1/priceListProduct")
public interface PriceListProductClientService {
	@GetMapping("/{priceListProductId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("priceListProductId") final Integer priceListProductId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final PriceListProductQueryRequest  DTO
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PriceListProductDto  DTO);


	@DeleteMapping("/delete/{priceListProductId}")
	ResponseEntity<GlobalReponse> delete(@PathVariable("priceListProductId") final Integer priceListProductId);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PriceListProductDto DTO);

	@PostMapping("/deleteAllByIds")
	ResponseEntity<GlobalReponse> deleteAllByIds(@RequestBody PriceListProductDto.IdsRequest ids);

	@PostMapping("/applyAllNewFormulaPrice")
	ResponseEntity<GlobalReponse> applyAllNewFormulaPrice(@RequestBody ApplyNewFormulaPriceRequest  request);

	@PostMapping("/deleteAllByPriceListId")
	ResponseEntity<GlobalReponse> deleteAllByPriceListId(@RequestBody ApplyNewFormulaPriceRequest request);

	@GetMapping("/findAllProductAndPriceList")
	ResponseEntity<GlobalReponsePagination> findAllProductAndPriceList(
			@SpringQueryMap final FindAllPriceListAndProductRequest  DTO
	);

	@GetMapping("/findAllPriceListProduct")
	ResponseEntity<GlobalReponsePagination> findAllPriceListProduct(@SpringQueryMap final PriceListProductReqDto request);

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










