package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.orderDto.ReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.TotalPosOrderCostDto;
import org.common.dbiz.dto.paymentDto.request.BillInfoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReceiptOtherQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "receiptOtherClientService", path = "/payment-service/api/v1/receiptOther")
public interface ReceiptOtherClientService {

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final ReceiptOtherQueryRequest entityQueryRequest);



	@PostMapping("/save")
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final ReceiptOtherDto DTO);

	@DeleteMapping("/{receiptId}}")
	public ResponseEntity<GlobalReponse	> deleteById(@PathVariable("receiptId") final Integer receiptId);

	@PostMapping("/calculateAmountROByOrder")
	public ResponseEntity<GlobalReponse> calculateAmountROByOrder(@RequestBody CalculateAmountRODto dto);

	@PostMapping("/calPosOrderCost")
	public ResponseEntity<GlobalReponse> getROByPosOrderId(@RequestBody CalculateAmountRODto dto);

	@PostMapping("/bill")
	ResponseEntity<GlobalReponse> getBillInfo(@RequestBody BillInfoReqDto dto);

	@PostMapping("/getROSelection")
	public ResponseEntity<GlobalReponse> getROSelection(@RequestBody CalculateAmountRODto dto);

	@GetMapping("/getOrgAccess")
	public ResponseEntity<GlobalReponse> getOrgAccess(@SpringQueryMap final ReceiptOtherQueryRequest request);

	@PostMapping("/org/access")
	public ResponseEntity<GlobalReponsePagination> getOrgAssign(@RequestBody final ReceiptOtherQueryRequest request);

	@PostMapping("/pos")
	public ResponseEntity<GlobalReponse> createPosReceiptOther(@RequestBody final TotalPosOrderCostDto request);

	@PostMapping("/selection")
	public ResponseEntity<GlobalReponse> getROSelection2(@RequestBody CalculateAmountRODto dto);
}










