package com.dbiz.app.paymentservice.resource;


import com.dbiz.app.paymentservice.service.EInvoiceService;
import org.common.dbiz.dto.paymentDto.CreateEInvoiceOrgDto;
import org.common.dbiz.dto.paymentDto.einvoice.IssueEInvoiceDto;
import org.common.dbiz.dto.paymentDto.request.EInvoiceSetUpReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.CreateEInvoiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/einvoice")
@Slf4j
@RequiredArgsConstructor
public class EInvoiceResource {

    private final EInvoiceService eInvoiceService;

    @PostMapping("/createEInvoice")
    public ResponseEntity<GlobalReponse > createEInvoice(@RequestBody final CreateEInvoiceRequest rq) {
        return ResponseEntity.ok(this.eInvoiceService.createEInvoice(rq));
    }

    @PostMapping("/org")
    public ResponseEntity<GlobalReponse > createEInvoiceSetupByOrg(@RequestBody final CreateEInvoiceOrgDto rq) {
        return ResponseEntity.ok(this.eInvoiceService.createEInvoiceSetupByOrg(rq));
    }

    @PostMapping("/org/fetch")
    public ResponseEntity<GlobalReponse > getEInvoiceOrgBySetUpIdAndOrgId(@RequestBody final CreateEInvoiceOrgDto rq) {
        return ResponseEntity.ok(this.eInvoiceService.getEInvoiceOrgBySetUpIdAndOrgId(rq));
    }

    @GetMapping("/setup")
    public ResponseEntity<GlobalReponsePagination> getEInvoiceSetUp(@ModelAttribute EInvoiceSetUpReqDto rq) {
        return ResponseEntity.ok(this.eInvoiceService.getEInvoiceSetUp(rq));
    }

    @PostMapping("/setup/default/{id}")
    public ResponseEntity<GlobalReponse> setDefault(@PathVariable("id") final Integer id) {
        return ResponseEntity.ok(this.eInvoiceService.setDefault(id));
    }

    @PostMapping("/issue")
    public ResponseEntity<GlobalReponse > issueEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; save InvoiceDto *");
        return ResponseEntity.ok(this.eInvoiceService.issueEInvoice(DTO));
    }

    @PostMapping("/issue/hilo")
    public ResponseEntity<GlobalReponse > issueHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; save Hilo InvoiceDto *");
        return ResponseEntity.ok(this.eInvoiceService.issueHiloEInvoice(DTO));
    }

    @PostMapping("/replace/hilo")
    public ResponseEntity<GlobalReponse > replaceHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; save Hilo InvoiceDto *");
        return ResponseEntity.ok(this.eInvoiceService.replaceHiloEInvoice(DTO));
    }

    @GetMapping("/getInvInfo/hilo/{id}")
    public ResponseEntity<GlobalReponse > getHiloInvInfo(@PathVariable("id") final Integer id) {
        log.info("*** InvoiceDto, resource; getInvInfo Hilo InvoiceDto *");
        return ResponseEntity.ok(this.eInvoiceService.getHiloInvInfo(id));
    }
}
