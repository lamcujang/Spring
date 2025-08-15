package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.ProductCategoryService;
import com.dbiz.app.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PcErEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;
import org.common.dbiz.request.productRequest.ProductEquestQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eRequest")
@Slf4j
@RequiredArgsConstructor
public class ERequestResource {
	
	private final ProductService productService;

	private final ProductCategoryService productCategoryService;

	@GetMapping("/getProductCategory")
	public ResponseEntity<GlobalReponsePagination > getProductCategory(@ModelAttribute PcErEquestQueryRequest request)
	{
		log.info("*** E - Request, controller; fetch all product category *");
		return ResponseEntity.ok(this.productService.eRequestGetPc(request));
	}


	@GetMapping("/getAllPcAccess")
	public ResponseEntity<GlobalReponsePagination > getAllPcAccess(@ModelAttribute ProductCategoryQueryRequest request)
	{
		log.info("*** E - Request, controller; fetch all product category *");
		return ResponseEntity.ok(this.productCategoryService.getAllPcAccess(request));
	}


	@GetMapping("/getProduct")
	public ResponseEntity<GlobalReponsePagination > getProduct(@ModelAttribute ProductEquestQueryRequest request)
	{
		log.info("*** E - Request, controller; fetch all product *");
		return ResponseEntity.ok(this.productService.eRequestGetProduct(request));
	}

	@PostMapping("/updatePcERequest")
	public ResponseEntity<GlobalReponse> updatePcERequest(@RequestBody List<PcTerminalAccessDto> request)
	{
		log.info("*** E - Request, controller; update product category *");
		return ResponseEntity.ok(this.productCategoryService.updatePcE_request(request));
	}

}










