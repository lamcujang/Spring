package com.dbiz.app.paymentservice.resource;

import com.dbiz.app.paymentservice.service.InvoiceService;
import com.dbiz.app.paymentservice.service.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PurchaseInvoiceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import org.common.dbiz.request.paymentRequest.PurchaseInvoiceQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchaseInvoices")
@Slf4j
@RequiredArgsConstructor
public class PurchaseInvoiceResource {
    private final PurchaseInvoiceService purchaseInvoiceService;

    @GetMapping("/po")
    public ResponseEntity<GlobalReponsePagination> getPo(@ModelAttribute final POHeaderVRequest req) {
        log.info("*** Invoice, controller; get purchase order all *");
        return ResponseEntity.ok(this.purchaseInvoiceService.getPO(req));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody final PurchaseInvoiceDto req) {
        log.info("*** Invoice, controller; save purchase invoice *");
        return ResponseEntity.ok(this.purchaseInvoiceService.save(req));
    }

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final PurchaseInvoiceQueryRequest req) {
        log.info("*** Invoice, controller; get purchase invoice all *");
        return ResponseEntity.ok(this.purchaseInvoiceService.findAll(req));
    }

    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** Invoice, controller; get purchase invoice by ID*");
        return ResponseEntity.ok(this.purchaseInvoiceService.findById(id));
    }
}
