package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.KitchenOrderClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/kitchenOrders")
@RequiredArgsConstructor
@Slf4j
public class KitchenOrderController {
    private final KitchenOrderClientService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id)
    {
        log.info("*** KitchenOrder, controller; fetch KitchenOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination > findAll(

            @SpringQueryMap final KitchenOrderRequest  entityQueryRequest
    ){
        log.info("*** KitchenOrder, controller; fetch KitchenOrder All *");
        return ResponseEntity.ok(this.entityClientService.findAll(entityQueryRequest).getBody());

    };

    @PostMapping("/save")
    ResponseEntity<GlobalReponse>save(@RequestBody @Valid final KitchenOrderDto  entityDto){
        log.info("*** KitchenOrder, controller; save KitchenOrder *");
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());

    };


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id){
        log.info("*** KitchenOrder, controller; delete KitchenOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    };

    @PutMapping("/update")
    ResponseEntity<GlobalReponse> update(@RequestBody @Valid final KitchenOrderDto entityDto){
        log.info("*** KitchenOrder, controller; update KitchenOrder By ID *");
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    };

    @GetMapping("/findOrderByStatus")
    public ResponseEntity<GlobalReponsePagination> findByStatus(@ModelAttribute final KitchenOrderRequest entityQueryRequest)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder by status *");
        return ResponseEntity.ok(this.entityClientService.findByStatus(entityQueryRequest).getBody());
    }

    @GetMapping("/findKOrderGroupByProduct")
    ResponseEntity<GlobalReponsePagination> findKOrderGroupByOrder(
            @SpringQueryMap final KitchenOrderRequest entityQueryRequest
    )
    {
        log.info("*** KitchenOrder, controller; fetch KitchenOrder Group By Order *");
        return ResponseEntity.ok(this.entityClientService.findKOrderGroupByOrder(entityQueryRequest).getBody());
    }
    @GetMapping("/getDocNo")
    public ResponseEntity<GlobalReponsePagination>findDocNo(@SpringQueryMap KitchenOrderRequest kOrderId)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder document no *");
        return ResponseEntity.ok(this.entityClientService.findDocNo(kOrderId).getBody());
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody List<KitchenOrderDto> entityDto) {
        log.info("*** Kitchen Order, resource; integration save *");
        return ResponseEntity.ok(this.entityClientService.intSave(entityDto)).getBody();
    }

    @GetMapping("/status")
    public ResponseEntity<GlobalReponse>getLimitedKitchenOrderStatus(@SpringQueryMap KitchenOrderRequest dto)
    {
        log.info("*** KitchenOrder, resource; fetch KitchenOrder document no *");
        return ResponseEntity.ok(this.entityClientService.getLimitedKitchenOrderStatus(dto)).getBody();
    }

}
