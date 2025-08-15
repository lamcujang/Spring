package com.dbiz.app.proxyclient.business.payment.service;


import org.common.dbiz.dto.paymentDto.coupon.CouponDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "couponClientService", path = "/payment-service/api/v1/coupon")
public interface CouponClientService {

    @PostMapping
    public GlobalReponse save(@RequestBody CouponDto dto);

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap CouponParamDto dto);

    @GetMapping("/findAllForPayment")
    public ResponseEntity<GlobalReponsePagination> findAllForPayment(@SpringQueryMap CouponParamDto dto);
}
