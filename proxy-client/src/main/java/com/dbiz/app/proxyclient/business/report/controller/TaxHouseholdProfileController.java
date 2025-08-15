package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.report.service.TaxHouseholdProfileClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.reportDto.TaxHouseholdProfileDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/taxHouseholdProfile")
@RequiredArgsConstructor
public class TaxHouseholdProfileController {
	
	private final TaxHouseholdProfileClientService clientService;

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id));
	};

	@PostMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final TaxHouseholdProfileDto entityDto) {
		return ResponseEntity.ok(this.clientService.update(entityDto).getBody());
	}
}




