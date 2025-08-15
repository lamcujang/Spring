package com.dbiz.app.orderservice.resource;

import com.dbiz.app.orderservice.service.PosOrderPreService;
import com.dbiz.app.orderservice.service.PosOrderService;
import com.dbiz.app.orderservice.service.PosOrderV2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.BulkPosOrderDto;
import org.common.dbiz.dto.orderDto.GeneratePOSBillNumberDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.common.dbiz.request.orderRequest.PosOrderQueryRequest;
import org.common.dbiz.request.orderRequest.ReportPosQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/posOrders")
@Slf4j
@RequiredArgsConstructor
public class PosOrderV2Resource {

    private final PosOrderV2Service service;
    private final PosOrderPreService preService;

    @GetMapping("/{posOrderId}")
    public ResponseEntity<GlobalReponse > findById(@PathVariable("posOrderId") final Integer posOrderId) {
        log.info("*** PosOrder, resource; fetch PosOrder by id {} *",posOrderId);
        return ResponseEntity.ok(this.service.findById(posOrderId));
    }
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute PosOrderQueryRequest  request)
    {
        log.info("*** PosOrder, resource; fetch all PosOrder *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PosOrderDto  DTO) {
        log.info("*** PosOrder, resource; save PosOrder  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/delete/{priceListId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListId") final Integer id) {
        log.info("*** PosOrder, resource; delete PosOrder id {} *",id);
        return ResponseEntity.ok(this.service.deleteById(id));
    }

    @PostMapping("/cancel/{posOrderId}")
    public ResponseEntity<GlobalReponse> cancelCompletedOrder(@PathVariable("posOrderId") final Integer id) {
        log.info("*** PosOrder, resource; cancel PosOrder id {} *",id);
        return ResponseEntity.ok(this.service.cancelCompletedOrder(id));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final PosOrderDto DTO) {
        log.info("*** PosOrder, resource; save PosOrder  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @GetMapping("/findAllList")
    public ResponseEntity<GlobalReponsePagination> findAllPosList(@ModelAttribute PosOrderListQueryRequest request) {
        log.info("*** PosOrder, resource; fetch all PosOrder List *");
        return ResponseEntity.ok(this.service.findAllPosList(request));
    }

    @GetMapping("/findAllListById/{posOrderId}")
    public ResponseEntity<GlobalReponse> findPosListById(@PathVariable("posOrderId") final Integer posOrderId)
    {
        log.info("*** PosOrder, resource; fetch PosOrder by id {} *",posOrderId);
        return ResponseEntity.ok(this.service.findPosListById(posOrderId));
    }


    @PostMapping("/generateBillNo")
    public ResponseEntity<GlobalReponse> generatePOSBillNumber(@RequestBody GeneratePOSBillNumberDto DTO)
    {
        log.info("*** PosOrder, resource; generatePOSBillNumber  *");
        return ResponseEntity.ok(this.service.generatePOSBillNumber(DTO));
    }


    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody List<PosOrderDto> entityDto) {
        log.info("*** Table, resource; integration save *");
        return ResponseEntity.ok(this.service.intSave(entityDto));
    }


    @GetMapping("/reportOrder")
    public ResponseEntity<GlobalReponsePagination> reportOrder(@ModelAttribute ReportPosQueryRequest request) {
        log.info("*** PosOrder, resource; fetch all PosOrder complete *");
        return ResponseEntity.ok(this.service.reportOrder(request));
    }

    @PostMapping("/saveByRqOrder")
    public ResponseEntity<GlobalReponse> saveByRqOrder(@RequestBody PosOrderDto DTO) {
        log.info("*** PosOrder, resource; saveByRqOrder  *");
        return ResponseEntity.ok(this.service.requestOrder(DTO));
    }

    @GetMapping("/retail")
    public ResponseEntity<GlobalReponsePagination> findPosOrdersRetail(@ModelAttribute PosOrderListQueryRequest DTO) {
        log.info("*** PosOrder, resource; findPosOrdersRetail  *");
        return ResponseEntity.ok(this.service.findPosOrdersRetail(DTO));
    }

    @PostMapping("/retail/fetch/detail")
    public ResponseEntity<GlobalReponse> findPosOrderRetailById(@RequestBody PosOrderReqDto DTO) {
        log.info("*** PosOrder, resource; findPosOrderRetailById  *");
        return ResponseEntity.ok(this.service.findPosOrderRetailById(DTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> deletePosOrder(@PathVariable Integer id) {
        log.info("*** PosOrder, resource; deletePosOrder  *");
        return ResponseEntity.ok(this.service.deletePosOrder(id));
    }

    @PutMapping("/delete/withReason")
    public ResponseEntity<GlobalReponse> deleteWithReason(@RequestBody @Valid final PosOrderDto DTO) {
        log.info("*** PosOrder, resource; delete PosOrder with reason  *");
        return ResponseEntity.ok(this.service.deleteWithReason(DTO));
    }

    @PostMapping("/header/update")
    public ResponseEntity<GlobalReponse> updateOrderHeader(@RequestBody org.common.dbiz.dto.paymentDto.PosOrderDto DTO) {
        log.info("*** PosOrder, resource; deletePosOrder  *");
        return ResponseEntity.ok(this.service.updateOrderHeader(DTO));
    }

    @PostMapping("/bulk")
    public ResponseEntity<GlobalReponse> saveBulkOrder(@RequestBody BulkPosOrderDto DTO) {
        log.info("*** PosOrder, resource; bulk order  *");
        return ResponseEntity.ok(this.preService.createBulkOrder(DTO));
    }
}
