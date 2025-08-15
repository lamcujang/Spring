package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderLineQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/orderLines")
@Slf4j
@RequiredArgsConstructor
public class OrderLineResource {

    private final OrderLineService orderLineService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final OrderLineQueryRequest  entityQueryRequest) {
        log.info("*** OrderLine, controller; fetch OrderLine all *");
        return ResponseEntity.ok(this.orderLineService.findAll(entityQueryRequest));
    }


    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final OrderLineListDto  DTO) {
        log.info("*** OrderLineDto, resource; save OrderLineDto *");
        return ResponseEntity.ok(this.orderLineService.save(DTO));
    }
}
