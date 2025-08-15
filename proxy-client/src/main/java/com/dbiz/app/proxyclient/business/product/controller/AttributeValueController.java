package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.AttributeValueClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.AttributeValueDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attributeValues")
@RequiredArgsConstructor
public class AttributeValueController {
    private final AttributeValueClientService entityClientService;


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final AttributeValueDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }



    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final AttributeValueDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> getALl(
            @SpringQueryMap final AttributeQueryRequest queryRequest
    )
    {
        return ResponseEntity.ok(this.entityClientService.findAll(queryRequest).getBody());
    }
}
