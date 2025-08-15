package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.IconDto;
import org.common.dbiz.dto.productDto.request.IconReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "iconService", path = "/product-service/api/v1/icons")
public interface IconService {

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getIcons(@SpringQueryMap IconReqDto dto);

    @PostMapping
    public ResponseEntity<GlobalReponse> createIcon(@RequestBody @Valid final IconDto DTO);
}
