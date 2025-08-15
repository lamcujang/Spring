package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.FloorQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "floorClientService", path = "/order-service/api/v1/floors")
public interface FloorClientService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final FloorQueryRequest  floorQueryRequest
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final FloorDto  entityDto);


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final FloorDto entityDto);


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










