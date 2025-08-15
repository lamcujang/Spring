package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.UomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.UomQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/uom")
@Slf4j
@RequiredArgsConstructor
public class UomResource {
    private final UomService uomService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final UomQueryRequest uomQueryRequest) {
        log.info("*** Uom, controller; fetch product by id *");
        return ResponseEntity.ok(this.uomService.findAll(uomQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody final UomDto  uomDto) {
        log.info("*** Uom, controller; save product *");
        return ResponseEntity.ok(this.uomService.save(uomDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final UomDto uomDto) {
        log.info("*** Uom, controller; update product *");
        return ResponseEntity.ok(this.uomService.save(uomDto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer id) {
        log.info("*** Uom, controller; delete product by id *");
        return ResponseEntity.ok(this.uomService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** Uom, controller; fetch product by id *");
        return ResponseEntity.ok(this.uomService.findById(id));
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody final UomDto uomDto) {
        log.info("*** Uom, controller; save product *");
        return ResponseEntity.ok(this.uomService.intSave(uomDto));
    }

}
