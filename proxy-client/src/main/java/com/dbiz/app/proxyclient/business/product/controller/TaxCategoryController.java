package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.TaxCategoryClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.TaxCategoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxCategoryQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taxCategory")
@RequiredArgsConstructor
public class TaxCategoryController {

	private final TaxCategoryClientService clientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final TaxCategoryQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final TaxCategoryDto  dto) {
		return ResponseEntity.ok(this.clientService.save(dto).getBody());
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final TaxCategoryDto dto) {
		return ResponseEntity.ok(this.clientService.update(dto).getBody());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer uomId) {
		return ResponseEntity.ok(this.clientService.delete(uomId).getBody());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.findById(id).getBody());
	}
}

