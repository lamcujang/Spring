package com.dbiz.app.paymentservice.resource;


import com.dbiz.app.paymentservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.coupon.CouponDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon")
@Slf4j
@RequiredArgsConstructor
public class CouponResource {

    private final CouponService service;

    @PostMapping
    public GlobalReponse save(@RequestBody CouponDto dto){
        log.info("*** Coupon, resource; save Coupon ***");
        return this.service.save(dto);
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute CouponParamDto dto){
        log.info("*** Coupon, resource; Find All Coupon ***");
        return ResponseEntity.ok(this.service.findAll(dto));
    }

    @GetMapping("/findAllForPayment")
    public ResponseEntity<GlobalReponsePagination> findAllForPayment(@ModelAttribute CouponParamDto dto){
        log.info("*** Coupon, resource; Find All Coupon ***");
        return ResponseEntity.ok(this.service.findAllForPayment(dto));
    }

    @PostMapping("/intSave")
    public GlobalReponse intSave(@RequestBody List<CouponDto> dto){
        log.info("*** Coupon, resource; save Coupon ***");
        return this.service.intSave(dto);
    }
}
