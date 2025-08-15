package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.orderDto.ReturnReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.common.dbiz.request.orderRequest.ReturnReasonQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "returnReasonService", path = "/order-service/api/v1/returnReasons")
public interface ReturnReasonService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id);

	@GetMapping
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final ReturnReasonQueryRequest returnReasonQueryRequest
	);

	@PostMapping
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final ReturnReasonDto entityDto);


	@DeleteMapping("/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final ReturnReasonDto entityDto);


}










