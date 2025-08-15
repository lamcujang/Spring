package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.CouponClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.coupon.CouponDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/coupon")
@Slf4j
@RequiredArgsConstructor
public class CouponController {

    private final CouponClientService service;

    @PostMapping
    public GlobalReponse save(@RequestBody CouponDto dto){
        log.info("*** Coupon, resource; save Coupon ***");
        return this.service.save(dto);

    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap CouponParamDto dto){
        log.info("*** Coupon, resource; Find All Coupon ***");
        return ResponseEntity.ok(this.service.findAll(dto)).getBody();
    }

    @GetMapping("/findAllForPayment")
    public ResponseEntity<GlobalReponsePagination> findAllForPayment(@SpringQueryMap CouponParamDto dto){
        log.info("*** Coupon, resource; Find All Coupon ***");
        return ResponseEntity.ok(this.service.findAllForPayment(dto)).getBody();
    }
}
