package com.dbiz.app.proxyclient.business.payment.controller;


import com.dbiz.app.proxyclient.business.payment.service.EInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.CreateEInvoiceOrgDto;
import org.common.dbiz.dto.paymentDto.einvoice.IssueEInvoiceDto;
import org.common.dbiz.dto.paymentDto.request.EInvoiceSetUpReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.CreateEInvoiceRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/einvoice")
@Slf4j
@RequiredArgsConstructor
public class EInvoiceController {

    private final EInvoiceService einvoiceService;

    @PostMapping("/createEInvoice")
    public ResponseEntity<GlobalReponse> createEInvoice(@RequestBody final CreateEInvoiceRequest  request) {
        return ResponseEntity.ok(this.einvoiceService.createEInvoice(request).getBody());
    }

    @PostMapping("/org")
    public ResponseEntity<GlobalReponse > createEInvoiceSetupByOrg(@RequestBody final CreateEInvoiceOrgDto rq) {
        return ResponseEntity.ok(this.einvoiceService.createEInvoiceSetupByOrg(rq)).getBody();
    }

    @PostMapping("/org/fetch")
    public ResponseEntity<GlobalReponse > getEInvoiceOrgBySetUpIdAndOrgId(@RequestBody final CreateEInvoiceOrgDto rq) {
        return ResponseEntity.ok(this.einvoiceService.getEInvoiceOrgBySetUpIdAndOrgId(rq)).getBody();
    }

    @GetMapping("/setup")
    public ResponseEntity<GlobalReponsePagination> getEInvoiceSetUp(@SpringQueryMap EInvoiceSetUpReqDto rq) {
        return ResponseEntity.ok(this.einvoiceService.getEInvoiceSetUp(rq)).getBody();
    }

    @PostMapping("/setup/default/{id}")
    public ResponseEntity<GlobalReponse> setDefault(@PathVariable("id") final Integer id) {

        log.info("*** default, resource; default default default *");
        return ResponseEntity.ok(this.einvoiceService.setDefault(id)).getBody();
    }

    @PostMapping("/issue")
    public ResponseEntity<GlobalReponse > issueEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; save InvoiceDto *");
        return ResponseEntity.ok(this.einvoiceService.issueEInvoice(DTO)).getBody();
    }

    @PostMapping("/issue/hilo")
    public ResponseEntity<GlobalReponse> issueHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; save Hilo InvoiceDto *");
        return ResponseEntity.ok(this.einvoiceService.issueHiloEInvoice(DTO)).getBody();
    }

    @PostMapping("/replace/hilo")
    public ResponseEntity<GlobalReponse> replaceHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; replace Hilo InvoiceDto *");
        return ResponseEntity.ok(this.einvoiceService.replaceHiloEInvoice(DTO)).getBody();
    }

    @PostMapping("/adjust/hilo")
    public ResponseEntity<GlobalReponse> adjustHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; adjust Hilo InvoiceDto *");
        return ResponseEntity.ok(this.einvoiceService.adjustHiloEInvoice(DTO)).getBody();
    }

    @GetMapping("/getInvInfo/hilo/{id}")
    public ResponseEntity<GlobalReponse> getInvInfo(@PathVariable("id") final Integer id) {
        log.info("*** InvoiceDto, resource; getInvInfo Hilo InvoiceDto *");
        return ResponseEntity.ok(this.einvoiceService.getHiloInvInfo(id)).getBody();
    }
}
