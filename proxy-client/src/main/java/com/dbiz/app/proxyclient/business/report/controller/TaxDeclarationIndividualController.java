package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.report.service.TaxDeclarationIndividualClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationIndividualDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationIndividualQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/taxDeclarationIndividual")
@RequiredArgsConstructor
@Slf4j
public class TaxDeclarationIndividualController {
	
	private final TaxDeclarationIndividualClientService clientService;

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id));
	};

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final TaxDeclarationIndividualQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request));
	}

	@GetMapping("/init")
	public ResponseEntity<GlobalReponse> init(@SpringQueryMap TaxDeclarationIndividualQueryRequest request) {
		log.info("*** TaxDeclarationIndividualResourceDto List, controller; fetch all header *");
		return ResponseEntity.ok(this.clientService.init(request));
	}

	@GetMapping("/findAllHeader")
	public ResponseEntity<GlobalReponsePagination> findAllHeader(@SpringQueryMap final TaxDeclarationIndividualQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAllHeader(request));
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final TaxDeclarationInforDto entityDto) {
		return ResponseEntity.ok(this.clientService.save(entityDto).getBody());
	}

	@PostMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final TaxDeclarationInforDto entityDto) {
		return ResponseEntity.ok(this.clientService.update(entityDto).getBody());
	}
}




