package com.dbiz.app.proxyclient.business.payment.service;


import org.common.dbiz.dto.paymentDto.CreateEInvoiceOrgDto;
import org.common.dbiz.dto.paymentDto.einvoice.IssueEInvoiceDto;
import org.common.dbiz.dto.paymentDto.request.EInvoiceSetUpReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.CreateEInvoiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "einvoiceService", path = "/payment-service/api/v1/einvoice")
public interface EInvoiceService {

    @PostMapping("/createEInvoice")
    public ResponseEntity <GlobalReponse> createEInvoice(@RequestBody final CreateEInvoiceRequest  request);

    @PostMapping("/org")
    public ResponseEntity<GlobalReponse > createEInvoiceSetupByOrg(@RequestBody final CreateEInvoiceOrgDto rq);

    @PostMapping("/org/fetch")
    public ResponseEntity<GlobalReponse > getEInvoiceOrgBySetUpIdAndOrgId(@RequestBody final CreateEInvoiceOrgDto rq);

    @GetMapping("/setup")
    public ResponseEntity<GlobalReponsePagination> getEInvoiceSetUp(@SpringQueryMap EInvoiceSetUpReqDto rq);

    
    @PostMapping("/setup/default/{id}")
    public ResponseEntity<GlobalReponse> setDefault(@PathVariable("id") final Integer id);

    @PostMapping("/issue")
    public ResponseEntity<GlobalReponse > issueEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO);

    @PostMapping("/issue/hilo")
    public ResponseEntity<GlobalReponse > issueHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO);

    @PostMapping("/replace/hilo")
    public ResponseEntity<GlobalReponse > replaceHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO);

    @PostMapping("/adjust/hilo")
    public ResponseEntity<GlobalReponse > adjustHiloEInvoice(@RequestBody @Valid final IssueEInvoiceDto DTO);

    @GetMapping("/getInvInfo/hilo/{id}")
    public ResponseEntity<GlobalReponse > getHiloInvInfo(@PathVariable("id") final Integer id);
}
