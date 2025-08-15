package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.PaymentTypeDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;


@FeignClient(name = "PAYMENT-SERVICE", contextId = "paymentTypeClientService", path = "/payment-service/api/v1/payments-type")
public interface PaymentTypeClientService {
	@GetMapping("/findAll")
	  ResponseEntity<GlobalReponse> getAllPaymentTypes();


	@PostMapping("/save")
	  ResponseEntity<GlobalReponse> save(@RequestBody @Valid final List<PaymentTypeDto> DTO);


}










