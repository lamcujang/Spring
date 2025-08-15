package com.dbiz.app.proxyclient.business.order.service;
import org.common.dbiz.dto.orderDto.PosOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "posOrderLineClientService", path = "/order-service/api/v1/posOrderLine")
public interface PosOrderLineClientService {
	@GetMapping("/{posOrderLineId}")
	ResponseEntity<GlobalReponse > findById(@PathVariable("posOrderLineId") final Integer posOrderLineId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@RequestParam(name = "orderId", required = false) Integer orderId,
			@RequestParam(name = "productId", required = false) Integer productId,
			@RequestParam(name = "isActive", required = false) String isActive,
			@RequestParam(name = "orgId", required = false) Integer orgId,

			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "pageSize", required = false) Integer pageSize
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PosOrderlineDto entityDto);

	@DeleteMapping("/delete/{posOrderLineId}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("posOrderLineId") final Integer posOrderLineId);

	@PutMapping("/delete/withReason")
	ResponseEntity<GlobalReponse> deleteWithReason(@RequestBody final PosOrderlineDto entityDto);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PosOrderlineDto entityDto);


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










