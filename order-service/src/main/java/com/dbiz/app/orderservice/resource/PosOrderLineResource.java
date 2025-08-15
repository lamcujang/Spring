package com.dbiz.app.orderservice.resource;
import com.dbiz.app.orderservice.service.PosOrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PosOrderlineDto;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelPosOrderRequest;
import org.common.dbiz.request.orderRequest.PosOrderLineQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posOrderLine")
@Slf4j
@RequiredArgsConstructor
public class PosOrderLineResource {

    private final PosOrderLineService service;

    @GetMapping("/{posOrderLineId}")
    public ResponseEntity<GlobalReponse > findById(@PathVariable("posOrderLineId") final Integer posOrderLineId) {
        log.info("*** PosOrderLine, resource; fetch PosOrderLine by id {} *",posOrderLineId);
        return ResponseEntity.ok(this.service.findById(posOrderLineId));
    }
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute PosOrderLineQueryRequest  request)
    {
        log.info("*** PosOrderLine resource; fetch all PosOrderLine *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PosOrderlineDto  DTO) {
        log.info("*** PosOrderLine, resource; save PosOrderLine  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/delete/{posOrderLineId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("posOrderLineId") final Integer posOrderLineId) {
        log.info("*** PosOrderLine, resource; delete PosOrderLine {}  *",posOrderLineId);
        return ResponseEntity.ok(this.service.deleteById(posOrderLineId));
    }

    @PutMapping("/delete/withReason")
    public ResponseEntity<GlobalReponse> deleteWithReason(@RequestBody final PosOrderlineDto DTO) {
        log.info("*** PosOrderLine, resource; delete PosOrderLine with reason  *");
        return ResponseEntity.ok(this.service.deleteWithReason(DTO));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final PosOrderlineDto DTO) {
        log.info("*** PosOrderLine, resource; save resource  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @PostMapping("/cancel")
    public ResponseEntity<GlobalReponse> cancelOrder(@RequestBody @Valid final CancelPosOrderRequest  DTO) {
        log.info("*** PosOrderLine, resource; cancel order  *");
        return ResponseEntity.ok(this.service.CancelPosOrder(DTO));
    }
}
