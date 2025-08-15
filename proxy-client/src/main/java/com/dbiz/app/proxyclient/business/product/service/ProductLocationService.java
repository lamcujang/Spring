package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.SaveProductLocationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "productLocationService", path = "/product-service/api/v1/productLocation")
public interface ProductLocationService {

    @DeleteMapping("/delete/{productLocationId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("productLocationId") final Integer productLocationId);

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProductLocation(@RequestBody @Valid final SaveProductLocationDto request);

    @PostMapping("/internal")
    public ResponseEntity<GlobalReponse> saveInternalProductLocation(@RequestBody @Valid final SaveProductLocationDto request);
}
