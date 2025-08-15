package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.productDto.NoteGroupDto;
import org.common.dbiz.request.productRequest.NoteGroupQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "noteGroupService", path = "/product-service/api/v1/noteGroups")
public interface NoteGroupService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> findAll(
			@SpringQueryMap final NoteGroupQueryRequest NoteGroupQueryRequest
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final NoteGroupDto  entityDto);


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final NoteGroupDto entityDto);

	@GetMapping("/findAllNoteGroupAndNote")
	ResponseEntity<GlobalReponsePagination> findAllNoteGroupAndNote(@SpringQueryMap final NoteGroupQueryRequest entityQueryRequest);
}










