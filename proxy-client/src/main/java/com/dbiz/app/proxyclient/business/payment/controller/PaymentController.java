package com.dbiz.app.proxyclient.business.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.dto.paymentDto.request.PaymentReqDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dbiz.app.proxyclient.business.payment.service.PaymentClientService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentClientService paymentClientService;

	@GetMapping
	public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final PaymentQueryRequest entityQueryRequest) {
		log.info("*** Payment, controller; fetch Payment all *");
		return ResponseEntity.ok(this.paymentClientService.findAll(entityQueryRequest).getBody());
	}

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > getPayments(@SpringQueryMap final PaymentReqDto rq) {
		log.info("*** Invoice, controller; fetch Invoices all *");
		return ResponseEntity.ok(this.paymentClientService.getPayments(rq)).getBody();
	}

	@PostMapping
	public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final PaymentDto DTO) {
		log.info("*** PaymentDto, resource; save PaymentDto *");
		return ResponseEntity.ok(this.paymentClientService.save(DTO).getBody());
	}


	@PostMapping("/voucher/transaction")
	public ResponseEntity<GlobalReponse > saveVoucherTransaction(@RequestBody VoucherTransactionDto DTO) {
		log.info("*** VoucherDto, resource; save Voucher *");
		return ResponseEntity.ok(this.paymentClientService.saveVoucherTransaction(DTO)).getBody();
	}

	@GetMapping("/methods")
	public ResponseEntity<GlobalReponse > getMethodForPayment(@SpringQueryMap final PaymentReqDto rq) {
		log.info("*** Payment method, controller; fetch Payment method all *");
		return ResponseEntity.ok(this.paymentClientService.getMethodForPayment()).getBody();
	}

	@PostMapping("/summary")
	public ResponseEntity<GlobalReponse> getPaymentSummary(@RequestBody ReportReqDto dto) {
		return ResponseEntity.ok(this.paymentClientService.getPaymentSummary(dto)).getBody();
	}
}










