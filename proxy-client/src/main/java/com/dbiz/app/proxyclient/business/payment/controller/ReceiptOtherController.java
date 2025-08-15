package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.ReceiptOtherClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.TotalPosOrderCostDto;
import org.common.dbiz.dto.paymentDto.request.BillInfoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReceiptOtherQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/receiptOther")
@Slf4j
@RequiredArgsConstructor
public class ReceiptOtherController {

	private final ReceiptOtherClientService clientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final ReceiptOtherQueryRequest request) {
		log.info("*** ReceiptOther, controller; fetch ReceiptOther all *");
		return ResponseEntity.ok(this.clientService.findAll(request).getBody());
	}



	@PostMapping("/save")
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final ReceiptOtherDto DTO) {
		log.info("*** ReceiptOther, resource; save ReceiptOther *");
		return ResponseEntity.ok(this.clientService.save(DTO).getBody());
	}

	@DeleteMapping("/{receiptId}")
	public ResponseEntity<GlobalReponse> deleteById(@PathVariable("receiptId") final Integer receiptId) {
		return ResponseEntity.ok(this.clientService.deleteById(receiptId).getBody());
	}

	@PostMapping("/calculateAmountROByOrder")
	public ResponseEntity<GlobalReponse> calculateAmountROByOrder(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; calculateAmountROByOrder  {} *",dto);
		return ResponseEntity.ok(this.clientService.calculateAmountROByOrder(dto)).getBody();
	}

	@PostMapping("/calPosOrderCost")
	public ResponseEntity<GlobalReponse> getROByPosOrderId(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; getROByPosOrderId  {} *",dto);
		return ResponseEntity.ok(this.clientService.getROByPosOrderId(dto)).getBody();
	}

	@PostMapping("/bill")
	public ResponseEntity<GlobalReponse> getBillInfo(@RequestBody BillInfoReqDto dto) {
		log.info("*** ReceiptOrder, controller; getBillInfo {} ***", dto);
		return ResponseEntity.ok(this.clientService.getBillInfo(dto).getBody());
	}

	@PostMapping("/getROSelection")
	public ResponseEntity<GlobalReponse> getROSelection(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; getROSelection  {} *",dto);
		return ResponseEntity.ok(this.clientService.getROSelection(dto)).getBody();
	}

	@GetMapping("/getOrgAccess")
	public ResponseEntity<GlobalReponse> getOrgAccess(@SpringQueryMap final ReceiptOtherQueryRequest request) {
		log.info("*** ReceiptOrder, resource; getOrgAccess  {} *",request);
		return ResponseEntity.ok(this.clientService.getOrgAccess(request)).getBody();
	}

	@PostMapping("/org/access")
	public ResponseEntity<GlobalReponsePagination> getOrgAssign(@RequestBody final ReceiptOtherQueryRequest request) {
		log.info("*** ReceiptOrder, resource; getOrgAccess  {} *",request);
		return ResponseEntity.ok(this.clientService.getOrgAssign(request)).getBody();
	}

	@PostMapping("/pos")
	public ResponseEntity<GlobalReponse> createPosReceiptOther(@RequestBody final TotalPosOrderCostDto request) {
		log.info("*** ReceiptOrder, resource; createPosReceiptOther  {} *",request);
		return ResponseEntity.ok(this.clientService.createPosReceiptOther(request)).getBody();
	}

	@PostMapping("/selection")
	public ResponseEntity<GlobalReponse> getROSelection2(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; getROSelection  {} *",dto);
		return ResponseEntity.ok(this.clientService.getROSelection2(dto)).getBody();
	}
}
