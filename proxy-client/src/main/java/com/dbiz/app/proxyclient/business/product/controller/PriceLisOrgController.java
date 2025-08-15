package com.dbiz.app.proxyclient.business.product.controller;
 
import com.dbiz.app.proxyclient.business.product.service.PriceListOrgClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.PriceListOrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/priceListOrg")
@RequiredArgsConstructor
public class PriceLisOrgController {
	
	private final PriceListOrgClientService clientService;
	@GetMapping("/{priceListOrgId}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("priceListOrgId") final Integer priceListOrgId){
		return ResponseEntity.ok(this.clientService.findById(priceListOrgId).getBody());
	};
	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(@RequestParam(name = "isActive", required = false) String isActive,
														   @RequestParam(name = "isAll", required = false) String isAll,
														   @RequestParam(name = "pricelistId", required = false) Integer pricelistId,
														   @RequestParam(name="orgId",required = false) Integer orgId,

														   @RequestParam(name = "page", required = false) Integer page,
														   @RequestParam(name = "pageSize", required = false) Integer pageSize) {
		return ResponseEntity.ok(this.clientService.findAll(isActive,isAll,pricelistId,orgId, page, pageSize).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final PriceListOrgDto DTO) {
		return ResponseEntity.ok(this.clientService.save(DTO).getBody());
	}

	@DeleteMapping("/delete/{priceListOrgId}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListOrgId") final Integer priceListOrgId) {
		return ResponseEntity.ok(this.clientService.delete(priceListOrgId).getBody());
	}


	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final PriceListOrgDto DTO) {
		return ResponseEntity.ok(this.clientService.update(DTO).getBody());
	}
	
	
	
}




