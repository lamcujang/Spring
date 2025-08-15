package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.ProductLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.SaveProductLocationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/productLocation")
@RequiredArgsConstructor
@Slf4j
public class ProductLocationController {

    private final ProductLocationService productLocationService;

    @DeleteMapping("/delete/{productLocationId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("productLocationId") final Integer productLocationId) {
        log.info("*** Delete, controller; delete product  *");
        return ResponseEntity.ok(this.productLocationService.delete(productLocationId)).getBody();
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProductLocation(@RequestBody @Valid final SaveProductLocationDto request) {
        log.info("*** Save, controller; save product  *");
        return ResponseEntity.ok(this.productLocationService.saveProductLocation(request)).getBody();
    }


    @PostMapping("/internal")
    public ResponseEntity<GlobalReponse> saveInternalProductLocation(@RequestBody @Valid final SaveProductLocationDto request) {
        log.info("*** Save, controller; save product  *");
        return ResponseEntity.ok(this.productLocationService.saveInternalProductLocation(request)).getBody();
    }
}
