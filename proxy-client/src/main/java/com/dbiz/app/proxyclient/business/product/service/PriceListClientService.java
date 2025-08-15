package com.dbiz.app.proxyclient.business.product.service;

import com.fasterxml.jackson.annotation.JsonView;
import org.common.dbiz.dto.productDto.JsonViewPriceListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "priceListsClientService", path = "/product-service/api/v1/priceLists")
public interface PriceListClientService {
	@GetMapping("/{priceListId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("priceListId") final Integer priceListId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap PriceListQueryRequest  queryRequest
			);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PricelistDto  DTO);


	@DeleteMapping("/delete/{priceListId}")
	ResponseEntity<GlobalReponse> delete(@PathVariable("priceListId") final Integer priceListId);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PricelistDto DTO);

	@GetMapping("/cashier")
	public ResponseEntity<GlobalReponsePagination > findAllByCashier(@SpringQueryMap PriceListQueryRequest  request);

}










