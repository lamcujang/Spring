package com.dbiz.app.proxyclient.business.product.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorGroupQueryRequest;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "businessSectorGroupClientService", path = "/product-service/api/v1/businessSectorGroup",configuration = FeignClientConfig.class)
public interface BusinessSectorGroupClientService {
	
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final BusinessSectorGroupQueryRequest request);

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);

}










