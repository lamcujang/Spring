package com.dbiz.app.proxyclient.business.product.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.productDto.TaxCategoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxCategoryQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "taxCategoryClientService", path = "/product-service/api/v1/taxCategory",configuration = FeignClientConfig.class)
public interface TaxCategoryClientService {
	
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final TaxCategoryQueryRequest  request);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse> save(@RequestBody final TaxCategoryDto  dto);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody final TaxCategoryDto dto);

	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id);


	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);

	
}










