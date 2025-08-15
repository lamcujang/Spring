package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.AttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.AttributeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/attributes")
@Slf4j
@RequiredArgsConstructor
public class AttributeResource {

    private final AttributeService service;

    @GetMapping("/getAll" )
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute AttributeQueryRequest request)
    {
        log.info("*** Find All, resource; fetch all pricelist  *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > saveProduct(@RequestBody @Valid final AttributeDto  DTO) {
        log.info("*** Save, resource; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final AttributeDto DTO) {
        log.info("*** Update, resource; update pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> geALl(@ModelAttribute AttributeQueryRequest request)
    {
        log.info("*** Find All, resource; fetch all pricelist  *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** Find By Id, resource; fetch pricelist by id  *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody @Valid final AttributeDto DTO) {
        log.info("*** Save All, resource; save all pricelist  *");
        return ResponseEntity.ok(this.service.saveAll(DTO));
    }

}
