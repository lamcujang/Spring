package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.coupon.CouponDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

import java.util.List;

public interface CouponService extends BaseService{

    GlobalReponsePagination findAllForPayment(CouponParamDto queryRequest);

    GlobalReponse intSave(List<CouponDto> param);

     String updateCouponStatus(String couponCode, String status);

}
