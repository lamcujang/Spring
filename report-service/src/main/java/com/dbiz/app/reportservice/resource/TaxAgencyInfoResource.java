package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.TaxAgencyInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.reportDto.TaxAgencyInfoDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxAgencyInfoQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/taxAgencyInfo")
@Slf4j
@RequiredArgsConstructor
public class TaxAgencyInfoResource {

    private final TaxAgencyInfoService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** TaxAgencyInfoResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/current")
    public ResponseEntity<GlobalReponse> getCurrentTaxAgencySetup() {
        log.info("*** TaxAgencyInfoResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.getCurrentTaxAgencySetup());
    }

    @GetMapping("/getTaxAgentInfo/{code}")
    public ResponseEntity<GlobalReponse> getTaxAgentInfo(@PathVariable("code") final String code) {
        log.info("*** TaxAgencyInfoResourceDto, controller; fetch getTaxAgentInfo *");
        return ResponseEntity.ok(this.service.getTaxAgentInfo(code));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute TaxAgencyInfoQueryRequest request) {
        log.info("*** TaxAgencyInfoResourceDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final TaxAgencyInfoDto dto) {
        log.info("*** TaxAgencyInfo, resource; save TaxAgencyInfo  *");
        return ResponseEntity.ok(this.service.save(dto));
    }
}










