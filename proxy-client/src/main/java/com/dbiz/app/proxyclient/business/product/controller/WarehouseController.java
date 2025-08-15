package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.WarehouseClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.ListWarehouseDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.WarehouseQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseClientService warehouseClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer locatorId){
        return ResponseEntity.ok(this.warehouseClientService.findById(locatorId).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap WarehouseQueryRequest warehouseQueryRequest){
        return ResponseEntity.ok(this.warehouseClientService.findAll(warehouseQueryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final WarehouseDto  warehouseDto) {
        return ResponseEntity.ok(this.warehouseClientService.save(warehouseDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer locatorId) {
        return ResponseEntity.ok(this.warehouseClientService.deleteById(locatorId).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final WarehouseDto warehouseDto) {
        return ResponseEntity.ok(this.warehouseClientService.update(warehouseDto).getBody());
    }

    @PutMapping("/updateAll")
    public ResponseEntity<GlobalReponse> updateAll(@RequestBody ListWarehouseDto warehouseDto)
    {
        return ResponseEntity.ok(this.warehouseClientService.updateAll(warehouseDto)).getBody();
    }
}
