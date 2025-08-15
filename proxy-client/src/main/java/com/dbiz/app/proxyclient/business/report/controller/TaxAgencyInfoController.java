package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.report.service.TaxAgencyInfoClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.reportDto.TaxAgencyInfoDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxAgencyInfoQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/taxAgencyInfo")
@RequiredArgsConstructor
public class TaxAgencyInfoController {
	
	private final TaxAgencyInfoClientService clientService;

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id));
	};

	@GetMapping("/current")
	ResponseEntity<GlobalReponse> getCurrentTaxAgencySetup(){
		return ResponseEntity.ok(this.clientService.getCurrentTaxAgencySetup());
	};

	@GetMapping("/getTaxAgentInfo/{code}")
	ResponseEntity<GlobalReponse> getTaxAgentInfo(@PathVariable("code") final String code){
		return ResponseEntity.ok(this.clientService.getTaxAgentInfo(code));
	};

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final TaxAgencyInfoQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request));
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final TaxAgencyInfoDto entityDto) {
		return ResponseEntity.ok(this.clientService.save(entityDto).getBody());
	}
}




