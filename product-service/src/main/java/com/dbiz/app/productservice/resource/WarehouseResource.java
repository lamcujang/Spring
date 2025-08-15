package com.dbiz.app.productservice.resource;


import com.dbiz.app.productservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ListWarehouseDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.WarehouseIntDto;
import org.common.dbiz.request.productRequest.WarehouseQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
@Slf4j
@RequiredArgsConstructor
public class WarehouseResource {
    private final WarehouseService warehouseService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final WarehouseQueryRequest  warehouseQueryRequest) {
        log.info("*** warehouse, controller; fetch product all *");
        return ResponseEntity.ok(this.warehouseService.findAll(warehouseQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody WarehouseDto warehouseDto)
    {
        log.info("*** warehouse, controller; save warehouse *");
        return ResponseEntity.ok(this.warehouseService.save(warehouseDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody WarehouseDto warehouseDto)
    {
        log.info("*** warehouse, controller; update warehouse *");
        return ResponseEntity.ok(this.warehouseService.save(warehouseDto));
    }

    @PutMapping("/updateAll")
    public ResponseEntity<GlobalReponse> updateAll(@RequestBody ListWarehouseDto warehouseDto)
    {
        log.info("*** warehouse, controller; update warehouse *");
        return ResponseEntity.ok(this.warehouseService.updateAll(warehouseDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** warehouse, controller; delete warehouse *");
        GlobalReponse response = new GlobalReponse();
        try {
            response = this.warehouseService.deleteById(id);
        }catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setErrors(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** warehouse, controller; fetch warehouse by id *");
        GlobalReponse reponse = new GlobalReponse();
//        try {
            reponse = this.warehouseService.findById(id);
//        }catch (Exception e) {
//            reponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            reponse.setErrors(e.getMessage());
//        }
        return ResponseEntity.ok(reponse);
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody WarehouseIntDto warehouseDto)
    {
        GlobalReponse response = new GlobalReponse();
        try {
            this.warehouseService.intSave(warehouseDto);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Success");
        response.setData(warehouseDto);
        }catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setErrors(e.getMessage());
            e.printStackTrace();
        }
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/getByErpId/{erpId}")
    public ResponseEntity<GlobalReponse> findByErpId(@PathVariable("erpId") final Integer erpId)
    {
        log.info("*** warehouse, controller; fetch warehouse by erpId *");
        GlobalReponse reponse = new GlobalReponse();
        try {
            reponse = this.warehouseService.findByErpId(erpId);
        }catch (Exception e) {
            reponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            reponse.setErrors(e.getMessage());
        }
        return ResponseEntity.ok(reponse);
    }
}
