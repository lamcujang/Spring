package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.TaxHouseholdProfileService;
import com.dbiz.app.reportservice.service.TaxPaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.dto.reportDto.TaxHouseholdProfileDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/taxHouseholdProfile")
@Slf4j
@RequiredArgsConstructor
public class TaxHouseholdProfileResource {

    private final TaxHouseholdProfileService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** TaxPaymentMethodResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final TaxHouseholdProfileDto dto) {
        log.info("*** TaxHouseholdProfile, resource; update TaxHouseholdProfile  *");
        return ResponseEntity.ok(this.service.save(dto));
    }
}










