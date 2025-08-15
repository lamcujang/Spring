package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.BusinessSectorClientService;
import com.dbiz.app.proxyclient.business.product.service.BusinessSectorGroupClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorGroupQueryRequest;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/businessSectorGroup")
@RequiredArgsConstructor
public class BusinessSectorGroupController {

	private final BusinessSectorGroupClientService clientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final BusinessSectorGroupQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.findById(id).getBody());
	}
}

