package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.RequestOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/requestOrder")
@Slf4j
@RequiredArgsConstructor
public class RequestOrderV2Resource {

    private final RequestOrderService service;

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@Valid @RequestBody RequestOrderDto rq) {
        log.info("RequestOrderResource::save");
        return ResponseEntity.ok().body(this.service.saveV2(rq));
    }
    

}
