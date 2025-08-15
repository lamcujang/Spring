package com.dbiz.app.proxyclient.business.product.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.productDto.request.ProductCategoryReq;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "productCategoryClientService", path = "/product-service/api/v1/categoryProducts",configuration = FeignClientConfig.class)
public interface ProductCategoryClientService {
	
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final ProductCategoryQueryRequest  request);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse> saveCategory(@RequestBody final ProductCategoryDto  categoryDto);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody final ProductCategoryDto categoryDto);

	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer categoryId);


	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer categoryId);

	@DeleteMapping("/{categoryId}")
	ResponseEntity<Boolean> deleteById(@PathVariable("categoryId") final String categoryId);

	@GetMapping("/getOrgAccess")
	public ResponseEntity<GlobalReponse> getOrgAccess(@SpringQueryMap ProductCategoryQueryRequest request);

	@PostMapping("/org/access")
	public ResponseEntity<GlobalReponsePagination> getPcTerminalAccess(@RequestBody ProductCategoryReq request);

	@GetMapping("/findAllSimple")
	public ResponseEntity<GlobalReponsePagination> getAllSimple(@SpringQueryMap ProductCategoryQueryRequest request);
}










