package com.dbiz.app.paymentservice.resource;


import com.dbiz.app.paymentservice.service.ReceiptOtherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.TotalPosOrderCostDto;
import org.common.dbiz.dto.paymentDto.request.BillInfoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReceiptOtherQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/receiptOther")
@Slf4j
@RequiredArgsConstructor
public class ReceiptOtherResource {

	private final ReceiptOtherService service;


	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final ReceiptOtherQueryRequest request) {
		log.info("*** ReceiptOrder, resource; findAll ReceiptOther  {} *",request);
		return ResponseEntity.ok(this.service.findAll(request));
	}


	@PostMapping("/save")
	public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final ReceiptOtherDto DTO) {
		log.info("*** ReceiptOrder, resource; save ReceiptOther  {} *",DTO);
		return ResponseEntity.ok(this.service.save(DTO));
	}

	@DeleteMapping("/{receiptId}}")
	public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer receiptId) {
		log.info("*** ReceiptOrder, resource; findById  {} *",receiptId);
		return ResponseEntity.ok(this.service.deleteById(receiptId));
	}

	@PostMapping("/calculateAmountROByOrder")
	public ResponseEntity<GlobalReponse> calculateAmountROByOrder(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; calculateAmountROByOrder  {} *",dto);
		return ResponseEntity.ok(this.service.calculateAmountROByOrder(dto));
	}

	@PostMapping("/calPosOrderCost")
	public ResponseEntity<GlobalReponse> getROByPosOrderId(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; getROByPosOrderId  {} *",dto);
		return ResponseEntity.ok(this.service.getPosCost(dto));
	}

	@PostMapping("/bill")
	public ResponseEntity<GlobalReponse> getBillInfo(@RequestBody BillInfoReqDto dto) {
		log.info("*** ReceiptOrder, resource; getBillInfo {} ***", dto);
		return ResponseEntity.ok(this.service.getBillInfo(dto));
	}

	@PostMapping("/getROSelection")
	public ResponseEntity<GlobalReponse> getROSelection(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; getROSelection  {} *",dto);
		return ResponseEntity.ok(this.service.getROSelection(dto));
	}

	@GetMapping("/getOrgAccess")
	public ResponseEntity<GlobalReponse> getOrgAccess(@ModelAttribute final ReceiptOtherQueryRequest request) {
		log.info("*** ReceiptOrder, resource; getOrgAccess  {} *",request);
		return ResponseEntity.ok(this.service.getOrgAccess(request));
	}

	@PostMapping("/org/access")
	public ResponseEntity<GlobalReponsePagination> getOrgAssign(@RequestBody final ReceiptOtherQueryRequest request) {
		log.info("*** ReceiptOrder, resource; getOrgAccess  {} *",request);
		return ResponseEntity.ok(this.service.getOrgAssign(request));
	}


	@PostMapping("/pos")
	public ResponseEntity<GlobalReponse> createPosReceiptOther(@RequestBody final TotalPosOrderCostDto request) {
		log.info("*** ReceiptOrder, resource; createPosReceiptOther  {} *",request);
		return ResponseEntity.ok(this.service.createPosReceiptOther(request));
	}

	@PostMapping("/selection")
	public ResponseEntity<GlobalReponse> getROSelection2(@RequestBody CalculateAmountRODto dto) {
		log.info("*** ReceiptOrder, resource; getROSelection  {} *",dto);
		return ResponseEntity.ok(this.service.getROSelection2(dto));
	}
}










