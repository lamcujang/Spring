package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.AttributeValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.AttributeValueDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.AttributeValueQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/attributeValues")
@Slf4j
@RequiredArgsConstructor
public class AttributeValueResource {

    private final AttributeValueService service;

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final AttributeValueDto DTO) {
        log.info("*** Save, resource; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final AttributeValueDto DTO) {
        log.info("*** Update, resource; update pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> geALl(@ModelAttribute AttributeValueQueryRequest request)
    {
        log.info("*** Find All, resource; fetch all pricelist  *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    
}
