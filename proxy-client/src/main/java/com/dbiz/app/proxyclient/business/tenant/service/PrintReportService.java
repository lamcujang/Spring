package com.dbiz.app.proxyclient.business.tenant.service;

import org.common.dbiz.dto.tenantDto.PrintReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PrintReportQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@FeignClient(name = "TENANT-SERVICE", contextId = "printReportService", path = "/tenant-service/api/v1/printReports")
public interface PrintReportService {

	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);

	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final PrintReportQueryRequest PrintReportQueryRequest
	);

	@GetMapping("/findAllByTenant")
	ResponseEntity<GlobalReponsePagination> findAllByTenant(
			@SpringQueryMap final PrintReportQueryRequest PrintReportQueryRequest
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PrintReportDto entityDto);

	@PostMapping("/saveAll")
	ResponseEntity<GlobalReponse>saveAll(@RequestBody @Valid final List<Map<String, Object>> rawList);

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PrintReportDto entityDto);


//	@GetMapping("/{productId}")
//	ResponseEntity<ProductDto> findById(
//			@PathVariable("productId")
//			@NotBlank(message = "Input must not be blank!")
//			@Valid final String productId);
//
//	@PostMapping
//	ResponseEntity<ProductDto> save(
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final ProductDto productDto);
//
//	@PutMapping
//	ResponseEntity<ProductDto> update(
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final ProductDto productDto);
//
//	@PutMapping("/{productId}")
//	ResponseEntity<ProductDto> update(
//			@PathVariable("productId")
//			@NotBlank(message = "Input must not be blank!")
//			@Valid final String productId,
//			@RequestBody
//			@NotNull(message = "Input must not be NULL!")
//			@Valid final ProductDto productDto);
//
//	@DeleteMapping("/{productId}")
//	ResponseEntity<Boolean> deleteById(@PathVariable("productId") final String productId);
//
}










