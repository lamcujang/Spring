package com.dbiz.app.paymentservice.resource;

import com.dbiz.app.paymentservice.service.PaymentService;
import com.dbiz.app.paymentservice.service.PaymentTypeService;
import com.dbiz.app.paymentservice.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.PaymentTypeDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments-type")
@Slf4j
@RequiredArgsConstructor
public class PaymentTypeResource {


	private final PaymentTypeService paymentTypeService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponse> getAllPaymentTypes() {
		log.info("*** Payment Type, controller; fetch all Payment Type *");
		return ResponseEntity.ok(this.paymentTypeService.getAllPaymentTypes());
	}


	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final List<PaymentTypeDto> DTO) {
		log.info("*** PaymentTypeDto, resource; save PaymentTypeDto *");
		return ResponseEntity.ok(this.paymentTypeService.save(DTO));
	}

}










