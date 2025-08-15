package com.dbiz.app.proxyclient.business.product.controller;
import com.dbiz.app.proxyclient.business.product.service.BusinessSectorClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/businessSector")
@RequiredArgsConstructor
public class BusinessSectorController {

	private final BusinessSectorClientService clientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final BusinessSectorQueryRequest  request) {
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.clientService.findById(id).getBody());
	}
}

