package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.ProductComboClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ProductComboDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductComboQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/productCombo")
@RequiredArgsConstructor
public class ProductComboController {
	
	private final ProductComboClientService clientService;
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
		return ResponseEntity.ok(this.clientService.findById(id).getBody());
	};
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final ProductComboQueryRequest request) {
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final ProductComboDto dto) {
		return ResponseEntity.ok(this.clientService.save(dto).getBody());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.delete(id).getBody());
	}


	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final ProductComboDto dto) {
		return ResponseEntity.ok(this.clientService.update(dto).getBody());
	}

	@GetMapping("/component/{id}")
	public ResponseEntity<GlobalReponse> findByProductIsComponentById(@RequestBody final Integer id) {
		return ResponseEntity.ok(this.clientService.findByProductIsComponentById(id)).getBody();
	}

}




