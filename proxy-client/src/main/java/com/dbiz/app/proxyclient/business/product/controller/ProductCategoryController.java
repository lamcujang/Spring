package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.ProductCategoryClientService;

import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.dto.productDto.request.ProductCategoryReq;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categoryProducts")
@RequiredArgsConstructor
public class ProductCategoryController {

	private final ProductCategoryClientService categoryClientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final ProductCategoryQueryRequest request
	) {
		return ResponseEntity.ok(this.categoryClientService.findAll(request).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> saveCategory(@RequestBody final ProductCategoryDto categoryDto) {
		return ResponseEntity.ok(this.categoryClientService.saveCategory(categoryDto).getBody());
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final ProductCategoryDto categoryDto) {
		return ResponseEntity.ok(this.categoryClientService.update(categoryDto).getBody());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer categoryId) {
		return ResponseEntity.ok(this.categoryClientService.deleteById(categoryId).getBody());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer categoryId) {
		return ResponseEntity.ok(this.categoryClientService.findById(categoryId).getBody());
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Boolean> deleteById(@PathVariable("categoryId") final String categoryId) {
		return ResponseEntity.ok(this.categoryClientService.deleteById(categoryId).getBody());
	}

	@GetMapping("/getOrgAccess")
	public ResponseEntity<GlobalReponse> getOrgAccess(@SpringQueryMap ProductCategoryQueryRequest request) {
		return ResponseEntity.ok(this.categoryClientService.getOrgAccess(request).getBody());
	}

	@PostMapping("/org/access")
	public ResponseEntity<GlobalReponsePagination> getPcTerminalAccess(@RequestBody ProductCategoryReq request) {
		return ResponseEntity.ok(this.categoryClientService.getPcTerminalAccess(request).getBody());
	}

	@GetMapping("/findAllSimple")
	public ResponseEntity<GlobalReponsePagination> getAllSimple(@SpringQueryMap ProductCategoryQueryRequest request)
	{
		return ResponseEntity.ok(this.categoryClientService.getAllSimple(request).getBody());
	}

}

