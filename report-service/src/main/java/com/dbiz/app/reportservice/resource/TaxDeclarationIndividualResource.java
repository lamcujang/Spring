package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.TaxDeclarationIndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationIndividualDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationIndividualQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/taxDeclarationIndividual")
@Slf4j
@RequiredArgsConstructor
public class TaxDeclarationIndividualResource {

    private final TaxDeclarationIndividualService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** TaxDeclarationIndividualResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute TaxDeclarationIndividualQueryRequest request) {
        log.info("*** TaxDeclarationIndividualResourceDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @GetMapping("/findAllHeader")
    public ResponseEntity<GlobalReponsePagination> findAllHeader(@ModelAttribute TaxDeclarationIndividualQueryRequest request) {
        log.info("*** TaxDeclarationIndividualResourceDto List, controller; fetch all header *");
        return ResponseEntity.ok(this.service.findAllHeader(request));
    }

    @GetMapping("/init")
    public ResponseEntity<GlobalReponse> init(@ModelAttribute TaxDeclarationIndividualQueryRequest request) {
        log.info("*** TaxDeclarationIndividualResourceDto List, controller; fetch all header *");
        return ResponseEntity.ok(this.service.init(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final TaxDeclarationInforDto dto) {
        log.info("*** TaxDeclarationIndividual, resource; save TaxDeclarationIndividual  *");
        return ResponseEntity.ok(this.service.saveAllInfo(dto));
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final TaxDeclarationInforDto dto) {
        log.info("*** TaxDeclarationIndividual, resource; update TaxDeclarationIndividual  *");
        return ResponseEntity.ok(this.service.saveAllInfo(dto));
    }
}










