package com.dbiz.app.paymentservice.resource;




import org.common.dbiz.dto.paymentDto.InvoiceLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceLineQueryRequest;
import com.dbiz.app.paymentservice.service.InvoiceLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/invoiceLines")
@Slf4j
@RequiredArgsConstructor
public class InvoiceLineResource {

    private final InvoiceLineService invoiceLineService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final InvoiceLineQueryRequest entityQueryRequest) {
        log.info("*** Invoice Line, controller; fetch Invoice Lines all *");
        return ResponseEntity.ok(this.invoiceLineService.findAll(entityQueryRequest));
    }


    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final InvoiceLineListDto DTO) {
        log.info("*** InvoiceLineDto, resource; save InvoiceLineDto *");
        return ResponseEntity.ok(this.invoiceLineService.save(DTO));
    }
}
