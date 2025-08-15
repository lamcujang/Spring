package com.dbiz.app.proxyclient.business.product.controller;

	import com.dbiz.app.proxyclient.business.product.service.UomClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/uom")
@RequiredArgsConstructor
public class UomController {

	private final UomClientService uomClientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination> findAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
			@RequestParam(name="orgId",required = false) Integer orgId,

            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
	) {
		return ResponseEntity.ok(this.uomClientService.findAll(name, code,orgId, page, pageSize).getBody());
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody final UomDto  uomDto) {
		return ResponseEntity.ok(this.uomClientService.save(uomDto).getBody());
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalReponse> update(@RequestBody final UomDto uomDto) {
		return ResponseEntity.ok(this.uomClientService.update(uomDto).getBody());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") final Integer uomId) {
		return ResponseEntity.ok(this.uomClientService.delete(uomId).getBody());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
		return ResponseEntity.ok(this.uomClientService.findById(id).getBody());
	}
}

