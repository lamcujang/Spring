package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "priceListsV2ClientService", path = "/product-service/api/v2/priceLists")
public interface PriceListV2ClientService {

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PricelistDto  DTO);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PricelistDto DTO);
}










