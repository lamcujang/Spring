package com.dbiz.app.systemservice.resource;

import com.dbiz.app.systemservice.service.TaxOfficeService;
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
public class TaxOfficeResource {

    private final TaxOfficeService taxOfficeService;

    @PostMapping//consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    public ResponseEntity<GlobalReponse> loadTaxOffice(
            @RequestPart(value = "taxOffice") MultipartFile taxOffice,
            @RequestPart(value = "taxRegion") MultipartFile taxRegion) {
        log.info("*** TaxOffice - resource : load ***");
        return ResponseEntity.ok(taxOfficeService.loadTaxOffice(taxOffice, taxRegion));
    }

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAllTaxOffice(@ModelAttribute final TaxOfficeQueryRequest request) {
        log.info("*** TaxOffice - resource : findAll ***");
        return ResponseEntity.ok(taxOfficeService.findAllTaxOffice(request));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final TaxOfficeQueryRequest request) {
        log.info("*** TaxOffice - resource : findAll ***");
        return ResponseEntity.ok(taxOfficeService.findAll(request));
    }

}
