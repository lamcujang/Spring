package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "ERequestClientService", path = "/product-service/api/v1/eRequest")
public interface ERequestClientService {

	@GetMapping("/getProductCategory")
	ResponseEntity<GlobalReponsePagination> getProductCategory(
			@SpringQueryMap final PcErEquestQueryRequest request
	);
	@GetMapping("/getProduct")
	ResponseEntity<GlobalReponsePagination> getProduct(
			@SpringQueryMap final ProductEquestQueryRequest request
	);

	@PostMapping("/updatePcERequest")
	 ResponseEntity<GlobalReponse> updatePcERequest(@RequestBody List<PcTerminalAccessDto> request);

	@GetMapping("/getAllPcAccess")
	 ResponseEntity<GlobalReponsePagination > getAllPcAccess(@SpringQueryMap ProductCategoryQueryRequest request);
}










