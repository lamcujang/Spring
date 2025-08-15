package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.TaxPaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taxPaymentMethod")
@Slf4j
@RequiredArgsConstructor
public class TaxPaymentMethodResource {

    private final TaxPaymentMethodService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** TaxPaymentMethodResourceDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute TaxPaymentMethodQueryRequest request) {
        log.info("*** TaxPaymentMethodResourceDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }
}










