package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.CancelReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cancelReasons")
@Slf4j
@RequiredArgsConstructor
public class CancelReasonResource {
    private final CancelReasonService entityService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final CancelReasonQueryRequest  entityQueryRequest) {
        log.info("*** CancelReason, resource; fetch CancelReason all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody CancelReasonDto entityDto)
    {
        log.info("*** CancelReason, resource; save CancelReason *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody CancelReasonDto  entityDto)
    {
        log.info("*** CancelReason, resource; update CancelReason *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** CancelReason, resource; delete CancelReason *");
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** CancelReason, resource; fetch CancelReason by id *");
        return ResponseEntity.ok(this.entityService.findById(id));
    }
}
