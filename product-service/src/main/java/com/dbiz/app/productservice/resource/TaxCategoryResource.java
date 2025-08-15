package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.TaxCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.TaxCategoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxCategoryQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taxCategory")
@Slf4j
@RequiredArgsConstructor
public class TaxCategoryResource {
    private final TaxCategoryService service;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final TaxCategoryQueryRequest  queryRequest) {
            log.info("*** TaxCategory, controller; fetch TaxCategory by id *");
        return ResponseEntity.ok(this.service.findAll(queryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody final TaxCategoryDto  dto) {
        log.info("*** TaxCategory, controller; save TaxCategory *");
        return ResponseEntity.ok(this.service.save(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final TaxCategoryDto dto) {
        log.info("*** TaxCategory, controller; update TaxCategory *");
        return ResponseEntity.ok(this.service.save(dto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer id) {
        log.info("*** TaxCategory, controller; delete TaxCategory by id *");
        return ResponseEntity.ok(this.service.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** TaxCategory, controller; fetch TaxCategory by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

}
