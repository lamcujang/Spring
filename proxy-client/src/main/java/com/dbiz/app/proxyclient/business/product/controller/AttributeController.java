package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.AttributeClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.AttributeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeClientService entityClientService;

    @GetMapping("/getAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final AttributeQueryRequest queryRequest
    )
    {
        return ResponseEntity.ok(this.entityClientService.findAll(queryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final AttributeDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }



    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final AttributeDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> getALl(
            @SpringQueryMap final AttributeQueryRequest queryRequest
    )
    {
        return ResponseEntity.ok(this.entityClientService.findAll(queryRequest).getBody());
    }

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody final AttributeDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.saveAll(entityDto).getBody());
    }
}
