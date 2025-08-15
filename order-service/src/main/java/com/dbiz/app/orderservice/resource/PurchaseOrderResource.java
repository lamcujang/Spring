package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PODetailVRequest;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/po")
@Slf4j
@RequiredArgsConstructor
public class PurchaseOrderResource {

    private final PurchaseOrderService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute POHeaderVRequest  request)
    {
        log.info("*** Purchase Order Header, resource; fetch all Purchase Order Header *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @GetMapping("/{poId}")
    public ResponseEntity<GlobalReponse > findById(@PathVariable("poId") final Integer poId) {
        log.info("*** PO, resource; fetch PO by id {} *",poId);
        return ResponseEntity.ok(this.service.findById(poId));
    }

    @GetMapping("/findAllDetail")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute PODetailVRequest  request)
    {
        log.info("*** Purchase Order Detail, resource; fetch all Purchase Order Detail *");
        return ResponseEntity.ok(this.service.findAllDetail(request));
    }

    @PostMapping()
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PurchaseOrderDto  DTO) {
        log.info("*** Purchase Order, resource; save Purchase Order  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse > deletePurchaseOrder(@PathVariable("id") final Integer id) {
        log.info("*** PO, resource; delete PO by id {} *",id);
        return ResponseEntity.ok(this.service.deleteById(id));
    }
}
