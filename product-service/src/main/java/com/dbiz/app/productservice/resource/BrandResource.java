package com.dbiz.app.productservice.resource;


import com.dbiz.app.productservice.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BrandDto;
import org.common.dbiz.dto.productDto.request.BrandReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/brands")
@Slf4j
@RequiredArgsConstructor
public class BrandResource {

    private final BrandService service;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getBrands(@ModelAttribute BrandReqDto dto) {
        log.info("*** Get, resource; get all brands  *");
        return ResponseEntity.ok(this.service.getBrands(dto));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> createBrand(@RequestBody @Valid final BrandDto DTO) {
        log.info("*** Save, resource; save brand  *");
        return ResponseEntity.ok(this.service.createBrand(DTO));
    }
}
