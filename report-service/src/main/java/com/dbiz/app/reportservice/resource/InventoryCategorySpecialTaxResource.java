package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.InventoryCategorySpecialTaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

import org.common.dbiz.request.reportRequest.InventoryCategorySpecialTaxQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventoryCategorySpecialTax")
@Slf4j
@RequiredArgsConstructor
public class InventoryCategorySpecialTaxResource {

    private final InventoryCategorySpecialTaxService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** InventoryCategorySpecialTaxResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute InventoryCategorySpecialTaxQueryRequest request) {
        log.info("*** InventoryCategorySpecialTaxResourceDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }
}










