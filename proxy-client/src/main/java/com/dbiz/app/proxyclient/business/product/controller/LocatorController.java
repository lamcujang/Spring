package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.LocatorClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.LocatorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/locators")
@RequiredArgsConstructor
public class LocatorController {
	
	private final LocatorClientService locatorClientService;
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer locatorId){
		return ResponseEntity.ok(this.locatorClientService.findById(locatorId).getBody());
	};
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "isActive", required = false) String isActive,
			@RequestParam(name="orgId",required = false) Integer orgId,

			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "pageSize", required = false) Integer pageSize
	){
		return ResponseEntity.ok(this.locatorClientService.findAll(name, code, isActive,orgId, page, pageSize).getBody());
	}


	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final LocatorDto locatorDto) {
		return ResponseEntity.ok(this.locatorClientService.save(locatorDto).getBody());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer locatorId) {
		return ResponseEntity.ok(this.locatorClientService.deleteById(locatorId).getBody());
	}


	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final LocatorDto locatorDto) {
		return ResponseEntity.ok(this.locatorClientService.update(locatorDto).getBody());
	}
	
	
	
}




