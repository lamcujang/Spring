package com.dbiz.app.proxyclient.business.product.controller;

import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.ProductIntDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dbiz.app.proxyclient.business.product.service.ProductClientService;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductClientService productClientService;
	@GetMapping("/{productId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("productId") final Integer productId){
		return ResponseEntity.ok(this.productClientService.findById(productId).getBody());
	};
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final ProductQueryRequest  request) {
		return ResponseEntity.ok(this.productClientService.findAll(request).getBody());
	}

	@PostMapping("/saveProduct")
	public ResponseEntity<GlobalReponse> save(@RequestBody final ProductDto productDto) {
		return ResponseEntity.ok(this.productClientService.save(productDto).getBody());
	}

	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable("productId") final Integer productId) {
		return ResponseEntity.ok(this.productClientService.delete(productId).getBody());
	}


	@PutMapping("/updateProduct")
	public ResponseEntity<GlobalReponse> update(@RequestBody final ProductDto productDto) {
		return ResponseEntity.ok(this.productClientService.update(productDto).getBody());
	}

	@PostMapping("/saveAll")
	public ResponseEntity<GlobalReponse> saveAll(@RequestBody final SaveAllProductAttr  productDtos) {
		return ResponseEntity.ok(this.productClientService.saveAll(productDtos).getBody());
	}

	@PostMapping("/saveAll2")
	public ResponseEntity<GlobalReponse> saveAll(@RequestBody final ListProductDto  productDtos) {
		return ResponseEntity.ok(this.productClientService.saveAllProduct(productDtos).getBody());
	}
	@GetMapping("/KcGetProduct")
	public ResponseEntity<GlobalReponsePagination> KcGetProduct(@SpringQueryMap ProductQueryRequest request) {

		return ResponseEntity.ok(this.productClientService.KcGetProduct(request).getBody());
	}


	@GetMapping("/findByIdOrgAccess")
	public ResponseEntity<GlobalReponse> findByIdOrgAccess(@SpringQueryMap ProductQueryRequest request) {
		return ResponseEntity.ok(this.productClientService.findByIdOrgAccess(request).getBody());
	}

	@GetMapping("/findByIdLocator")
	public ResponseEntity<GlobalReponse> findByIdLocator(@SpringQueryMap ProductQueryRequest request) {
		return ResponseEntity.ok(this.productClientService.findByIdLocator(request)).getBody();
	}

	@PostMapping("/intSave")
	public ResponseEntity<GlobalReponse> intSave(@RequestBody final List<ProductIntDto> productDto) {
		return  ResponseEntity.ok(this.productClientService.intSave(productDto)).getBody();
	}

	@GetMapping("/findAllByWarehouse")
	public ResponseEntity<GlobalReponsePagination> findAllByWarehouse(@SpringQueryMap ProductQueryRequest request) {
		return ResponseEntity.ok(this.productClientService.findAllByWarehouse(request).getBody());
	}


	@GetMapping("/getOnHand")
	public ResponseEntity<GlobalReponsePagination> getOnHand(@SpringQueryMap ProductQueryRequest request) {
		return ResponseEntity.ok(this.productClientService.getOnHand(request).getBody());
	}

	@PostMapping("/savePart")
	public ResponseEntity<GlobalReponse> savePart(@RequestBody final SaveAllProductAttr  productDto) {
		return ResponseEntity.ok(this.productClientService.savePart(productDto)).getBody();
	}

	@PostMapping("/org")
	public ResponseEntity<GlobalReponsePagination> getOrgAssign(@RequestBody final ProductQueryRequest  productDto) {

		return ResponseEntity.ok(this.productClientService.getOrgAssign(productDto)).getBody();
	}

	@PostMapping("/saveIntefaceProduct")
	public ResponseEntity<GlobalReponse> saveIntefaceProduct(@RequestBody final ProductIntDto productIntDto)
	{
		return ResponseEntity.ok(this.productClientService.saveIntefaceProduct(productIntDto)).getBody();
	}

	@GetMapping("/findAllSimple")
	public ResponseEntity<GlobalReponsePagination> findAllSimple(@SpringQueryMap ProductQueryRequest request) {
		return ResponseEntity.ok(this.productClientService.findAllSimple(request)).getBody();
	}
	@GetMapping
	public ResponseEntity<GlobalReponsePagination> findAll2(@SpringQueryMap final ProductQueryRequest  request) {
		return ResponseEntity.ok(this.productClientService.findAll2(request).getBody());
	}
}




