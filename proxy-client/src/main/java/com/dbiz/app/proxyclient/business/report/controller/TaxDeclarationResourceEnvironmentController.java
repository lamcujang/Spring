package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.report.service.TaxDeclarationResourceEnvironmentClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationResourceEnvironmentQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/taxDeclarationResourceEnvironment")
@RequiredArgsConstructor
public class TaxDeclarationResourceEnvironmentController {
	
	private final TaxDeclarationResourceEnvironmentClientService clientService;

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id));
	};

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final TaxDeclarationResourceEnvironmentQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request));
	}

	@GetMapping("/findAllByTaxDeclarationIndividualId/{id}")
	public ResponseEntity<GlobalReponse> findAllByTaxDeclarationIndividualId(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.findAllByTaxDeclarationIndividualId(id));
	}
}




