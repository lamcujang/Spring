package com.dbiz.app.orderservice.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.dbiz.app.orderservice.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.productDto.JsonView.JsonViewKitchenOrder;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kitchenOrders")
@Slf4j
@RequiredArgsConstructor
public class KitchenOrderResource {
    private final KitchenOrderService entityService;
    @GetMapping("/findAll")
    @JsonView(JsonViewKitchenOrder.viewJsonFindAll.class)
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final KitchenOrderRequest  entityQueryRequest) {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    @JsonView(JsonViewKitchenOrder.viewJsonSaveUpdateEntity.class)
    public ResponseEntity<GlobalReponse > save(@RequestBody KitchenOrderDto  entityDto)
    {
        log.info("*** KitchenOrder, resource; save KitchenOrder *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    @JsonView(JsonViewKitchenOrder.viewJsonSaveUpdateEntity.class)
    public ResponseEntity<GlobalReponse> update(@RequestBody KitchenOrderDto entityDto)
    {
        log.info("*** KitchenOrder, resource; update KitchenOrder *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** KitchenOrder, resource; delete by id KitchenOrder {} *",id);
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    @JsonView(JsonViewKitchenOrder.viewJsonSaveUpdateEntity.class)
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder by id {} *",id);
        return ResponseEntity.ok(this.entityService.findById(id));
    }



    @GetMapping("/findOrderByStatus")
    @JsonView(JsonViewKitchenOrder.viewJsonFindAllByStatus.class)
    public ResponseEntity<GlobalReponsePagination> findByStatus(@ModelAttribute final KitchenOrderRequest entityQueryRequest)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder by status *");
        return ResponseEntity.ok(this.entityService.findKOrderByStatus(entityQueryRequest));
    }

    @GetMapping("/findKOrderGroupByProduct")
    public ResponseEntity<GlobalReponsePagination> findKOrderGroupByOrder(@ModelAttribute KitchenOrderRequest kOrderId)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder group by order *");
        return ResponseEntity.ok(this.entityService.findKOrderGroupByOrder(kOrderId));
    }

    @GetMapping("/getDocNo")
    public ResponseEntity<GlobalReponsePagination>findDocNo(@ModelAttribute KitchenOrderRequest kOrderId)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder document no *");
        return ResponseEntity.ok(this.entityService.findAllDocNo(kOrderId));
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody List<KitchenOrderDto> entityDto) {
        log.info("*** Kitchen Order, resource; integration save *");
        return ResponseEntity.ok(this.entityService.intSave(entityDto));
    }

    @GetMapping("/status")
    public ResponseEntity<GlobalReponse>getLimitedKitchenOrderStatus(@ModelAttribute KitchenOrderRequest dto)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder document no *");
        return ResponseEntity.ok(this.entityService.getLimitedKitchenOrderStatus());
    }



}
