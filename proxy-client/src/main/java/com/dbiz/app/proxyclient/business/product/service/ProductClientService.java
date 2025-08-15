package com.dbiz.app.proxyclient.business.product.service;

import javax.validation.Valid;

import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.ProductIntDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "productClientService", path = "/product-service/api/v1/products")
public interface ProductClientService {
    @GetMapping("/{productId}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("productId") final Integer productId);

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final ProductQueryRequest request
    );

    @PostMapping("/saveProduct")
    ResponseEntity<GlobalReponse> save(@RequestBody @Valid final ProductDto dproductDto);


    @DeleteMapping("/deleteProduct/{productId}")
    ResponseEntity<GlobalReponse> delete(@PathVariable("productId") final Integer productId);

    @PutMapping("/updateProduct")
    ResponseEntity<GlobalReponse> update(@RequestBody @Valid final ProductDto dproductDto);

    @PostMapping("/saveAll")
    ResponseEntity<GlobalReponse> saveAll(@RequestBody final SaveAllProductAttr productDtos);

    @PostMapping("/saveAll2")
    public ResponseEntity<GlobalReponse> saveAllProduct(@RequestBody final ListProductDto productDtos);

    @GetMapping("/KcGetProduct")
    public ResponseEntity<GlobalReponsePagination> KcGetProduct(@SpringQueryMap ProductQueryRequest request);

    @GetMapping("/findByIdOrgAccess")
    public ResponseEntity<GlobalReponse> findByIdOrgAccess(@SpringQueryMap ProductQueryRequest request);

    @GetMapping("/findByIdLocator")
    public ResponseEntity<GlobalReponse> findByIdLocator(@SpringQueryMap ProductQueryRequest request);

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody final List<ProductIntDto> productDto) ;

    @GetMapping("/findAllByWarehouse")
    public ResponseEntity<GlobalReponsePagination> findAllByWarehouse(@SpringQueryMap ProductQueryRequest request);

    @GetMapping("/getOnHand")
    public ResponseEntity<GlobalReponsePagination> getOnHand(@SpringQueryMap ProductQueryRequest request);

    @PostMapping("/savePart")
    public ResponseEntity<GlobalReponse> savePart(@RequestBody final SaveAllProductAttr  productDto);

    @PostMapping("/org")
    public ResponseEntity<GlobalReponsePagination> getOrgAssign(@RequestBody final ProductQueryRequest  productDto);

    @PostMapping("/saveIntefaceProduct")
    public ResponseEntity<GlobalReponse> saveIntefaceProduct(@RequestBody ProductIntDto productIntDto);

    @GetMapping("/findAllSimple")
    public ResponseEntity<GlobalReponsePagination> findAllSimple(@SpringQueryMap ProductQueryRequest request);

    @GetMapping
    ResponseEntity<GlobalReponsePagination> findAll2(
            @SpringQueryMap final ProductQueryRequest request
    );
}










