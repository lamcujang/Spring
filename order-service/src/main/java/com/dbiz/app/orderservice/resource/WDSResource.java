package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.GetKolSameProductVRequest;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wds")
@Slf4j
@RequiredArgsConstructor
public class WDSResource {


     private final KitchenOrderService wdsService;

     @GetMapping("/fetch/detail")
     public ResponseEntity<GlobalReponsePagination> getWDSList(@ModelAttribute final KitchenOrderRequest entityQueryRequest) {
         log.info("*** WDS, resource; fetch WDS all *");
         return ResponseEntity.ok(this.wdsService.getWDSList(entityQueryRequest));
     }

    @PostMapping("/send")
    public ResponseEntity<GlobalReponse> sendWDS(@RequestBody final KitchenOrderDto dto) {
        log.info("*** WDS, resource; fetch WDS all *");
        return ResponseEntity.ok(this.wdsService.sendWDS(dto));
    }

    @GetMapping("/fetch/detail/history")
    public ResponseEntity<GlobalReponse> getWDSDetailHistory(@ModelAttribute final GetKolSameProductVRequest entityQueryRequest) {
        log.info("*** WDS, resource; fetch WDS all *");
        return ResponseEntity.ok(this.wdsService.getWDSDetailHistory(entityQueryRequest));
    }
}
