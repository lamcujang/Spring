package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.TaxOfficeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.TaxOfficeQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = {"/api/v1/taxOffice"})
@Slf4j
@RequiredArgsConstructor
public class TaxOfficeController {

    private final TaxOfficeClientService taxOfficeClientService;

    @PostMapping//consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    public ResponseEntity<GlobalReponse> loadTaxOffice(
            @RequestPart(value = "taxOffice") MultipartFile taxOffice,
            @RequestPart(value = "taxRegion") MultipartFile taxRegion) {
        log.info("*** TaxOffice - controller : load ***");
        return ResponseEntity.ok(taxOfficeClientService.loadTaxOffice(taxOffice, taxRegion).getBody());
    }

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAllTaxOffice(@ModelAttribute final TaxOfficeQueryRequest request) {
        log.info("*** TaxOffice - controller : findAll ***");
        return ResponseEntity.ok(taxOfficeClientService.findAllTaxOffice(request).getBody());
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final TaxOfficeQueryRequest request) {
        log.info("*** TaxOffice - controller : findAll ***");
        return ResponseEntity.ok(taxOfficeClientService.findAll(request).getBody());
    }

}
