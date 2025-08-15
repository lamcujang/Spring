package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.PriceListClientService;
import com.dbiz.app.proxyclient.business.product.service.PriceListV2ClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/priceLists")
@RequiredArgsConstructor
public class PriceListV2Controller {
	
	private final PriceListV2ClientService clientService;

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final PricelistDto DTO) {
		return ResponseEntity.ok(this.clientService.save(DTO).getBody());
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final PricelistDto DTO) {
		return ResponseEntity.ok(this.clientService.update(DTO).getBody());
	}
}




