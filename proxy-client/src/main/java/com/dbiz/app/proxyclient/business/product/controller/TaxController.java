package com.dbiz.app.proxyclient.business.product.controller;
import com.dbiz.app.proxyclient.business.product.service.TaxClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
public class TaxController {

	private final TaxClientService clientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final TaxQueryRequest  request) {
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final TaxDto  dto) {
		return ResponseEntity.ok(this.clientService.save(dto).getBody());
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final TaxDto dto) {
		return ResponseEntity.ok(this.clientService.update(dto).getBody());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.deleteById(id).getBody());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.findById(id).getBody());
	}
}

