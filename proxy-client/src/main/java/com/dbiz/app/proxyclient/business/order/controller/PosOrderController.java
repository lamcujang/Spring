package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.PosOrderClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.BulkPosOrderDto;
import org.common.dbiz.dto.orderDto.GeneratePOSBillNumberDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelPosOrderRequest;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.common.dbiz.request.orderRequest.PosOrderQueryRequest;
import org.common.dbiz.request.orderRequest.ReportPosQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posOrders")
@RequiredArgsConstructor
@Slf4j
public class PosOrderController {
    private final PosOrderClientService entityClientService;

    @GetMapping("/{posOrderId}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("posOrderId") final Integer posOrderId){
        log.info("*** PosOrder, controller; fetch PosOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(posOrderId).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final PosOrderQueryRequest  posOrderQueryRequest
            ){
        log.info("*** PosOrder, controller; fetch PosOrder All *");
        return ResponseEntity.ok(this.entityClientService.findAll(posOrderQueryRequest).getBody());
    }

    @PostMapping("/cancel/{posOrderId}")
    public ResponseEntity<GlobalReponse> cancelOrder(@PathVariable("posOrderId") final Integer posOrderId) {
        log.info("*** PosOrderLine, resource; cancel order  *");
        return ResponseEntity.ok(this.entityClientService.cancelOrder(posOrderId).getBody());
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final PosOrderDto entityDto) {
        log.info("*** PosOrder, controller; save PosOrder *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** PosOrder, controller; delete PosOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final PosOrderDto entityDto) {
        log.info("*** PosOrder, controller; update PosOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @PostMapping("/cancel")
    public ResponseEntity<GlobalReponse> cancelOrder(@RequestBody @Valid final CancelPosOrderRequest  DTO) {
        log.info("*** PosOrderLine, resource; cancel order  *");
        return ResponseEntity.ok(this.entityClientService.cancelOrder(DTO).getBody());
    }
    @GetMapping("/findAllList")
    ResponseEntity<GlobalReponsePagination> findAllPos(
            @SpringQueryMap final PosOrderListQueryRequest  posOrderQueryRequest
    ){
        log.info("*** PosOrder, controller; fetch PosOrder All *");
        return ResponseEntity.ok(this.entityClientService.findAllPosList(posOrderQueryRequest).getBody());
    }

    @PostMapping("/generateBillNo")
    public ResponseEntity<GlobalReponse> generatePOSBillNumber(@RequestBody GeneratePOSBillNumberDto DTO) {
        log.info("*** PosOrder, resource; generatePOSBillNumber  *");
        return ResponseEntity.ok(this.entityClientService.generatePOSBillNumber(DTO)).getBody();
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody List<PosOrderDto> entityDto) {
        log.info("*** Table, resource; integration save *");
        return ResponseEntity.ok(this.entityClientService.intSave(entityDto)).getBody();
    }

    @GetMapping("/reportOrder")
    public ResponseEntity<GlobalReponsePagination> reportOrder(@SpringQueryMap final ReportPosQueryRequest request) {
        log.info("*** PosOrder, resource; fetch all PosOrder complete *");
        return ResponseEntity.ok(this.entityClientService.reportOrder(request)).getBody();
    }

    @PostMapping("/saveByRqOrder")
    public ResponseEntity<GlobalReponse> saveByRqOrder(@RequestBody PosOrderDto DTO) {
        log.info("*** PosOrder, resource; saveByRqOrder  *");

        return  ResponseEntity.ok(this.entityClientService.requestOrder(DTO).getBody());
    }

    @GetMapping("/findAllListById/{posOrderId}")
    public ResponseEntity<GlobalReponse> findPosListById(@PathVariable("posOrderId") final Integer posOrderId) {
        log.info("*** PosOrder, resource; fetch PosOrder by id {} *",posOrderId);
        return ResponseEntity.ok(this.entityClientService.findPosListById(posOrderId)).getBody();
    }

    @GetMapping("/retail")
    public ResponseEntity<GlobalReponsePagination> findPosOrdersRetail(@SpringQueryMap PosOrderListQueryRequest DTO) {
        log.info("*** PosOrder, resource; findPosOrdersRetail  *");
        return ResponseEntity.ok(this.entityClientService.findPosOrdersRetail(DTO)).getBody();
    }

    @PostMapping("/retail/fetch/detail")
    public ResponseEntity<GlobalReponse> findPosOrderRetailById(@RequestBody PosOrderReqDto DTO) {
        log.info("*** PosOrder, resource; findPosOrderRetailById  *");
        return ResponseEntity.ok(this.entityClientService.findPosOrderRetailById(DTO)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> deletePosOrder(@PathVariable Integer id) {
        log.info("*** PosOrder, resource; findPosOrderRetailById  *");
        return ResponseEntity.ok(this.entityClientService.deletePosOrder(id)).getBody();
    }

    @PutMapping("/delete/withReason")
    public ResponseEntity<GlobalReponse> deleteWithReason(@RequestBody final PosOrderDto DTO) {
        log.info("*** PosOrder, controller; delete PosOrder with reason *");
        return ResponseEntity.ok(this.entityClientService.deleteWithReason(DTO).getBody());
    }

    @PostMapping("/header/update")
    public ResponseEntity<GlobalReponse> updateOrderHeader(@RequestBody org.common.dbiz.dto.paymentDto.PosOrderDto DTO) {
        log.info("*** PosOrder, resource; deletePosOrder  *");
        return ResponseEntity.ok(this.entityClientService.updateOrderHeader(DTO)).getBody();
    }

    @PostMapping("/bulk")
    public ResponseEntity<GlobalReponse> saveBulkOrder(@RequestBody BulkPosOrderDto DTO) {
        log.info("*** PosOrder, resource; bulk order  *");
        return ResponseEntity.ok(this.entityClientService.saveBulkOrder(DTO)).getBody();
    }
}
