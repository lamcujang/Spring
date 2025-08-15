package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.product.service.ProductClientService;
import com.dbiz.app.proxyclient.business.report.service.InventoryCategorySpecialTaxClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.ProductIntDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.common.dbiz.request.reportRequest.InventoryCategorySpecialTaxQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/inventoryCategorySpecialTax")
@RequiredArgsConstructor
public class InventoryCategorySpecialTaxController {
	
	private final InventoryCategorySpecialTaxClientService clientService;

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id));
	};
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final InventoryCategorySpecialTaxQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request));
	}

}




