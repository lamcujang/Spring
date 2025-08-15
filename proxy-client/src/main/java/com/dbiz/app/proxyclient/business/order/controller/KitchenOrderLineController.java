package com.dbiz.app.proxyclient.business.order.controller;

import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import com.dbiz.app.proxyclient.business.order.service.KitchenOrderLineClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/kitchenOrderLines")
@RequiredArgsConstructor
@Slf4j
public class KitchenOrderLineController {
    private final KitchenOrderLineClientService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** KitchenOrderLine, controller; fetch KitchenOrderLine By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination > findAll(
            @SpringQueryMap final KitchenOrderLineRequest  entityQueryRequest
    ){
        log.info("*** KitchenOrderLine, controller; fetch KitchenOrderLine All *");
        return ResponseEntity.ok(this.entityClientService.findAll(entityQueryRequest).getBody());

    };

    @PostMapping("/save")
    ResponseEntity<GlobalReponse>save(@RequestBody @Valid final KitchenOrderlineDto  entityDto){
        log.info("*** KitchenOrderLine, controller; save KitchenOrderLine *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());

    };


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id){
        log.info("*** KitchenOrderLine, controller; delete KitchenOrderLine By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    };

    @PutMapping("/update")
    ResponseEntity<GlobalReponse > update(@RequestBody @Valid final KitchenOrderlineDto entityDto){
        log.info("*** KitchenOrderLine, controller; update KitchenOrderLine By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    };
    @PostMapping("/cancelQty")
    ResponseEntity<GlobalReponse> cancelQty(@RequestBody @Valid final KitchenOrderLineCancelRq  rq){
        log.info("*** KitchenOrderLine, controller; cancelQty KitchenOrderLine *");
        return ResponseEntity.ok(this.entityClientService.cancelQty(rq).getBody());
    };

    @PostMapping("/updateAllById")
ResponseEntity<GlobalReponse> updateAllById(@RequestBody @Valid final UpdateAllKitchenLineByIdRequest rq){
        log.info("*** KitchenOrderLine, controller; updateAllById KitchenOrderLine *");
        return ResponseEntity.ok(this.entityClientService.updateAllById(rq).getBody());
    };

    @GetMapping("/findAllProductSameSelected")
    public ResponseEntity<GlobalReponse> findAllProductSameSelected(@SpringQueryMap GetKolSameProductVRequest request) {
        log.info("*** KitchenOrderLine, resource; fetch KitchenOrderLine all *");
        return ResponseEntity.ok(this.entityClientService.findAllProductSameSelected(request).getBody());
    }


    @GetMapping("/getKcHistory")
    public ResponseEntity<GlobalReponse>findKcHistory(@SpringQueryMap KitchenOrderRequest request)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder history *");
        return ResponseEntity.ok(this.entityClientService.findKcHistory(request).getBody());
    }

    @PostMapping("/sendNotifyRemind")
    public ResponseEntity<GlobalReponse> sendNotifyRemind(@RequestBody SendNotifycationRq request)
    {
        log.info("*** KitchenOrder, resource; sendNotifyRemind *");
        return ResponseEntity.ok(this.entityClientService.sendNotifyRemind(request).getBody());
    }

    @GetMapping("/getProductComboTest")
    public ResponseEntity<GlobalReponse> getProductCombo(@RequestParam("productId") Integer productId)
    {
        return ResponseEntity.ok(this.entityClientService.getProductCombo(productId).getBody());
    }

}
