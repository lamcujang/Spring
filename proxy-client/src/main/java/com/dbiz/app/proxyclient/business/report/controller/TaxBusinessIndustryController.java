package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.report.service.TaxBusinessIndustryClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxBusinessIndustryQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/taxBusinessIndustry")
@RequiredArgsConstructor
public class TaxBusinessIndustryController {
	
	private final TaxBusinessIndustryClientService clientService;

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id));
	};

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final TaxBusinessIndustryQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request));
	}
}




