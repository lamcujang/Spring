package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.PaymentClientService;
import com.dbiz.app.proxyclient.business.payment.service.PaymentTypeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.PaymentTypeDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments-type")
@Slf4j
@RequiredArgsConstructor
public class PaymentTypeController {
	
	private final PaymentTypeClientService clientService;


	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponse> getAllPaymentTypes() {
		log.info("*** Payment Type, controller; fetch all Payment Type *");
		return clientService.getAllPaymentTypes();
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final List<PaymentTypeDto> DTO) {
		log.info("*** PaymentTypeDto, resource; save PaymentTypeDto *");
		return clientService.save(DTO);
	}
	
}










