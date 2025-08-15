package com.dbiz.app.paymentservice.resource;

import javax.validation.Valid;

import com.dbiz.app.paymentservice.service.PaymentService;
import com.dbiz.app.paymentservice.service.PaymentTypeService;
import com.dbiz.app.paymentservice.service.VoucherService;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.dto.paymentDto.request.PaymentReqDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentResource {


	private final PaymentService paymentService;
	private final VoucherService voucherService;
	@GetMapping
	public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final PaymentQueryRequest entityQueryRequest) {
		log.info("*** Invoice, controller; fetch Invoices all *");
		return ResponseEntity.ok(this.paymentService.findAll(entityQueryRequest));
	}

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > getPayments(@ModelAttribute final PaymentReqDto rq) {
		log.info("*** Invoice, controller; fetch Invoices all *");
		return ResponseEntity.ok(this.paymentService.getPayments(rq));
	}


	@PostMapping
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final PaymentDto DTO) {
		log.info("*** InvoiceDto, resource; save InvoiceDto *");
		return ResponseEntity.ok(this.paymentService.save(DTO));
	}


	@PostMapping("/voucher/transaction")
	public ResponseEntity<GlobalReponse > saveVoucherTransaction(@RequestBody VoucherTransactionDto DTO) {
		log.info("*** VoucherDto, resource; save Voucher *");
		return ResponseEntity.ok(this.voucherService.saveVoucherTransaction(DTO));
	}

	@GetMapping("/methods")
	public ResponseEntity<GlobalReponse > getMethodForPayment(@ModelAttribute final PaymentReqDto rq) {
		log.info("*** Payment method, controller; fetch Payment method all *");
		return ResponseEntity.ok(this.paymentService.getMethodForPayment());
	}

	@PostMapping("/summary")
	public ResponseEntity<GlobalReponse> getPaymentSummary(@RequestBody ReportReqDto dto) {
		return ResponseEntity.ok(this.paymentService.getPaymentSummary(dto));
	}

}










