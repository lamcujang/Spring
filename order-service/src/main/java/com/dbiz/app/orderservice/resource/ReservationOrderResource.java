package com.dbiz.app.orderservice.resource;


import brave.Response;
import com.dbiz.app.orderservice.service.ReservationOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservationOrder")
@Slf4j
@RequiredArgsConstructor
public class ReservationOrderResource {

    private final ReservationOrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** ReservationOrder, resource; fetch ReservationOrder by id {} *",id);
        return ResponseEntity.ok(this.service.findById(id));
    }
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute ReservationOrderQueryRequest request)
    {
        log.info("*** ReservationOrder, resource; fetch all ReservationOrder *");
        return ResponseEntity.ok(this.service.findAllV(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final ReservationOrderDto DTO) {
        log.info("*** ReservationOrder, resource; save ReservationOrder  *");
        GlobalReponse globalReponse = null;
        try {
          globalReponse= this.service.save(DTO);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(GlobalReponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .errors(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok(globalReponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** ReservationOrder, resource; delete ReservationOrder  *");
        return ResponseEntity.ok(this.service.deleteById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final ReservationOrderDto DTO) {
        log.info("*** ReservationOrder, resource; save ReservationOrder  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @GetMapping("/findAllV")
    public ResponseEntity<GlobalReponsePagination> findAllV(@ModelAttribute ReservationOrderQueryRequest request)
    {
        log.info("*** ReservationOrder, resource; fetch all ReservationOrder *");
        return ResponseEntity.ok(this.service.findAllV(request));
    }
}
