package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.ERequestClientService;
import com.dbiz.app.proxyclient.business.product.service.ProductClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.*;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/eRequest")
@RequiredArgsConstructor
public class ERequestController {

    private final ERequestClientService eRequestClientService;

    @GetMapping("/getProductCategory")
    public ResponseEntity<GlobalReponsePagination> getProductCategory(@SpringQueryMap final PcErEquestQueryRequest request) {
        return ResponseEntity.ok(this.eRequestClientService.getProductCategory(request).getBody());
    }

    @GetMapping("/getProduct")
    ResponseEntity<GlobalReponsePagination> getProduct(
            @SpringQueryMap final ProductEquestQueryRequest request
    ) {
        return ResponseEntity.ok(this.eRequestClientService.getProduct(request).getBody());
    }

    ;

    @PostMapping("/updatePcERequest")
    public ResponseEntity<GlobalReponse> updatePcERequest(@RequestBody List<PcTerminalAccessDto> request) {
        return ResponseEntity.ok(this.eRequestClientService.updatePcERequest(request).getBody());
    }

    @GetMapping("/getAllPcAccess")
    public ResponseEntity<GlobalReponsePagination > getAllPcAccess(@SpringQueryMap ProductCategoryQueryRequest request)
    {
        return ResponseEntity.ok(this.eRequestClientService.getAllPcAccess(request).getBody());
    }
}




