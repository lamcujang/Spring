package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.BrandDto;
import org.common.dbiz.dto.productDto.request.BrandReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "brandService", path = "/product-service/api/v1/brands")
public interface BrandService {

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getBrands(@SpringQueryMap BrandReqDto dto);

    @PostMapping
    public ResponseEntity<GlobalReponse> createBrand(@RequestBody @Valid final BrandDto DTO);
}
