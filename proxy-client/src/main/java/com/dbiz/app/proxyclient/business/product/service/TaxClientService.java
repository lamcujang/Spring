package com.dbiz.app.proxyclient.business.product.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "taxClientService", path = "/product-service/api/v1/tax",configuration = FeignClientConfig.class)
public interface TaxClientService {
	
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final TaxQueryRequest  request);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse> save(@RequestBody final TaxDto  dto);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody final TaxDto dto);

	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer id);


	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);

	
}










