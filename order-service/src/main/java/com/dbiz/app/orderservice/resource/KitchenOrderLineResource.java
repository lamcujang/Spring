package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.KitchenOrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/kitchenOrderLines")
@Slf4j
@RequiredArgsConstructor
public class KitchenOrderLineResource {
    private final KitchenOrderLineService entityService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final KitchenOrderLineRequest  entityQueryRequest) {
        log.info("*** KitchenOrderLine, resource; fetch KitchenOrderLine all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody KitchenOrderlineDto  entityDto)
    {
        log.info("*** KitchenOrderLine, resource; save KitchenOrderLine *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody KitchenOrderlineDto entityDto)
    {
        log.info("*** KitchenOrderLine, resource; update KitchenOrderLine *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** KitchenOrderLine, resource; delete by id KitchenOrderLine {} *",id);
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** KitchenOrderLine, resource; findBy KitchenOrderLine by id {} *",id);
        return ResponseEntity.ok(this.entityService.findById(id));
    }
    @PostMapping("/cancelQty")
    public ResponseEntity<GlobalReponse> cancelQty(@RequestBody KitchenOrderLineCancelRq  rq)
    {
        log.info("*** KitchenOrderLine, resource; update KitchenOrderLine *");
        return ResponseEntity.ok(this.entityService.cancelQty(rq));
    }

    @PostMapping("/updateAllById")
    public ResponseEntity<GlobalReponse> updateAllById(@RequestBody UpdateAllKitchenLineByIdRequest  rq)
    {
        log.info("*** KitchenOrderLine, resource; update KitchenOrderLine *");
        return ResponseEntity.ok(this.entityService.updateAllById(rq));
    }


    @GetMapping("/findAllProductSameSelected")
    public ResponseEntity<GlobalReponse> findAllProductSameSelected(@ModelAttribute GetKolSameProductVRequest request)
    {
        log.info("*** KitchenOrderLine, resource; fetch KitchenOrderLine all *");
        return ResponseEntity.ok(this.entityService.findAllProductSameSelected(request));
    }

    @GetMapping("/getKcHistory")
    public ResponseEntity<GlobalReponse>findKcHistory(@ModelAttribute KitchenOrderRequest request)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder history *");
        return ResponseEntity.ok(this.entityService.getHistoryKichen(request));
    }

    @PostMapping("/sendNotifyRemind")
    public ResponseEntity<GlobalReponse> sendNotifyRemind(@RequestBody SendNotifycationRq request)
    {
        log.info("*** KitchenOrderLine, resource; send notify *");
        return ResponseEntity.ok(this.entityService.sendNotify(request));
    }

    @GetMapping("/getProductComboTest")
    public ResponseEntity<GlobalReponse> getProductCombo(@RequestParam("productId") Integer productId)
    {
        return ResponseEntity.ok(this.entityService.getProductComboTest(productId));
    }


}
