package com.dbiz.app.proxyclient.business.tenant.service;


import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.OrgEmenuDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.BaseQueryRequest;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TENANT-SERVICE", contextId = "orgClientService", path = "/tenant-service/api/v1/org", decode404 = true)
public interface OrgClientService {

//	Tenant
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> getAllOrg(@SpringQueryMap OrgQueryRequest orgQueryRequest);


	@PostMapping("/save")
	ResponseEntity<GlobalReponse> save(@RequestBody OrgDto dto);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody OrgDto dto);

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") Integer id);

	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") Integer id);

	@GetMapping("/getOrgAccess/{userId}")
	ResponseEntity<GlobalReponse> getOrgAccess(@PathVariable("userId") Integer userId);

	@GetMapping("/getOrgEmenu")
	 ResponseEntity<GlobalReponse> getOrgEmenu(@RequestParam("orgId") Integer orgId);

	@PostMapping("/saveOrgEmenu")
	 ResponseEntity<GlobalReponse> saveOrgEmenu(@RequestBody OrgEmenuDto dto);
}










