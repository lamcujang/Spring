package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.AttributeDto;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "AttributeClientService", path = "/product-service/api/v1/attributes")
public interface AttributeClientService {

	@GetMapping("/getAll")
	 ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap AttributeQueryRequest  request);

	@PostMapping("/save")
	 ResponseEntity<GlobalReponse> save(@RequestBody @Valid final AttributeDto  DTO);

	@PutMapping("/update")
	 ResponseEntity<GlobalReponse> update(@RequestBody @Valid final AttributeDto DTO);

	@GetMapping("/findAll")
	 ResponseEntity<GlobalReponsePagination> getALl(@SpringQueryMap AttributeQueryRequest request);

	@PostMapping("/saveAll")
	 ResponseEntity<GlobalReponse> saveAll(@RequestBody @Valid final AttributeDto DTO);
}










