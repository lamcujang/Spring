package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.ProductComboService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ProductComboDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductComboQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/productCombo")
@Slf4j
@RequiredArgsConstructor
public class ProductComboResource {
    private final ProductComboService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** ProductComboDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute ProductComboQueryRequest  request)
    {
        log.info("*** ProductDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProductCombo(@RequestBody @Valid final ProductComboDto  DTO) {
        log.info("*** Save, controller; save product  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** Delete, controller; delete product  *");
        return ResponseEntity.ok(this.service.deleteById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final ProductComboDto DTO) {
        log.info("*** Save, controller; save product  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @GetMapping("/findByProductId/{id}")
    public ResponseEntity<GlobalReponse> findByProductId(@PathVariable("id") final Integer id) {
        log.info("*** ProductComboDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findByProductId(id));
    }

    @GetMapping("/component/{id}")
    public ResponseEntity<GlobalReponse> findByProductIsComponentById(@PathVariable("id") final Integer id) {
        log.info("*** ProductComboDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findByProductIsComponentById(id));
    }
}
