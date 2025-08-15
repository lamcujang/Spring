package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.CancelReasonService;
import com.dbiz.app.orderservice.service.ReturnReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.orderDto.ReturnReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.common.dbiz.request.orderRequest.ReturnReasonQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/returnReasons")
@Slf4j
@RequiredArgsConstructor
public class ReturnReasonResource {
    private final ReturnReasonService entityService;
    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final ReturnReasonQueryRequest req) {
        log.info("*** ReturnReason, resource; fetch returnReason all *");
        return ResponseEntity.ok(this.entityService.findAll(req));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody ReturnReasonDto entityDto)
    {
        log.info("*** ReturnReason, resource; save ReturnReason *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody ReturnReasonDto  entityDto)
    {
        log.info("*** ReturnReason, resource; update returnReason *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** ReturnReason, resource; delete returnReason *");
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** ReturnReason, resource; fetch returnReason by id *");
        return ResponseEntity.ok(this.entityService.findById(id));
    }
}
