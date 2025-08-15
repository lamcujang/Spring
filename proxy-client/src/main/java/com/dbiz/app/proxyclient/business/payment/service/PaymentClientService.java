package com.dbiz.app.proxyclient.business.payment.service;

import javax.validation.Valid;

import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.dto.paymentDto.request.PaymentReqDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "PAYMENT-SERVICE", contextId = "paymentClientService", path = "/payment-service/api/v1/payments")
public interface PaymentClientService {

	@GetMapping
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final PaymentQueryRequest  entityQueryRequest);

	@PostMapping
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final PaymentDto DTO);

	@PostMapping("/voucher/transaction")
	public ResponseEntity<GlobalReponse > saveVoucherTransaction(@RequestBody VoucherTransactionDto DTO);

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > getPayments(@SpringQueryMap final PaymentReqDto rq);

	@GetMapping("/methods")
	public ResponseEntity<GlobalReponse > getMethodForPayment();

	@PostMapping("/summary")
	public ResponseEntity<GlobalReponse> getPaymentSummary(@RequestBody ReportReqDto dto);
}










