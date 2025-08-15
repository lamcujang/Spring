package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.AttributeValueDto;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "AttributeValueClientService", path = "/product-service/api/v1/attributeValues")
public interface AttributeValueClientService {

	@PostMapping("/save")
	 ResponseEntity<GlobalReponse> save(@RequestBody @Valid final AttributeValueDto  DTO);

	@PutMapping("/update")
	 ResponseEntity<GlobalReponse> update(@RequestBody @Valid final AttributeValueDto DTO);

	@GetMapping("/findAll")
	 ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap AttributeQueryRequest  request);
}










