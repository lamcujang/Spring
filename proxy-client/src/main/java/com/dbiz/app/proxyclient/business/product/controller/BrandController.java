package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BrandDto;
import org.common.dbiz.dto.productDto.request.BrandReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/brands")
@Slf4j
@RequiredArgsConstructor
public class BrandController {

    private final BrandService service;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getBrands(@SpringQueryMap BrandReqDto dto) {
        log.info("*** Get, resource; get all brands  *");
        return ResponseEntity.ok(this.service.getBrands(dto)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> createBrand(@RequestBody @Valid final BrandDto DTO) {
        log.info("*** Save, resource; save brand  *");
        return ResponseEntity.ok(this.service.createBrand(DTO)).getBody();
    }
}
