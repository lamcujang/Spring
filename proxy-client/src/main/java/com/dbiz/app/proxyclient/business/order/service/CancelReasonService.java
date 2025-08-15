package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "cancelReasonService", path = "/order-service/api/v1/cancelReasons")
public interface CancelReasonService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final CancelReasonQueryRequest  cancelReasonQueryRequest
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final CancelReasonDto entityDto);


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final CancelReasonDto entityDto);


}










