package com.dbiz.app.proxyclient.business.tenant.service;


import org.common.dbiz.dto.tenantDto.GetAssetsImageDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TENANT-SERVICE", contextId = "tenantClientService", path = "/tenant-service/api/v1/tenant", decode404 = true)
public interface TenantClientService {

//	Tenant
//	@GetMapping("/getAll")
//	ResponseEntity<DTenantServiceCollectionDtoResponse> getAllTenants(@RequestParam("page") int page, @RequestParam("size") int size);

	@GetMapping("/getbyDomain")
	ResponseEntity<GlobalReponse> getByDomainURL(@RequestParam("domainUrl") String domainUrl);

	@PutMapping("/updateDomain")
		ResponseEntity<GlobalReponse> updateDomain(@RequestBody TenantDto dTenantDto);

	@PostMapping("/registerDomain")
	ResponseEntity<GlobalReponse> registerDomain(@RequestBody TenantDto dTenantDto);

	@GetMapping("/getAllIndustry")
	ResponseEntity<GlobalReponsePagination> getAllIndustries(@RequestParam("page") int page, @RequestParam("size") int size);

	@PostMapping("/createTenant")
	 ResponseEntity<GlobalReponse> createTenant(@RequestBody TenantDto dto);


	@PostMapping("/getImageAsset")
	 ResponseEntity<GlobalReponse> getImageAsset(@RequestBody GetAssetsImageDto dto);

	@PostMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody TenantDto dto);

	@GetMapping("/findById")
	public ResponseEntity<GlobalReponse> findById(@RequestParam("tenantId") Integer id);
}










