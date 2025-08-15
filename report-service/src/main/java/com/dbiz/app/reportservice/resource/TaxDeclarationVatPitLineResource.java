package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.TaxDeclarationVatPitLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationVatPitLineQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taxDeclarationVatPitLine")
@Slf4j
@RequiredArgsConstructor
public class TaxDeclarationVatPitLineResource {

    private final TaxDeclarationVatPitLineService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** TaxDeclarationVatPitLineResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute TaxDeclarationVatPitLineQueryRequest request) {
        log.info("*** TaxDeclarationVatPitLineResourceDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @GetMapping("/findAllByTaxDeclarationIndividualId/{id}")
    public ResponseEntity<GlobalReponse> findAllByTaxDeclarationIndividualId(@PathVariable("id") final Integer id) {
        log.info("*** IndividualIndustryResourceDto, controller; fetch findAllByTaxDeclarationIndividualId *");
        return ResponseEntity.ok(this.service.findAllByTaxDeclarationIndividualId(id));
    }
}










