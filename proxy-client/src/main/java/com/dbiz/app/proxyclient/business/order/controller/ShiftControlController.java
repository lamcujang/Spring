package com.dbiz.app.proxyclient.business.order.controller;

import com.dbiz.app.proxyclient.business.order.service.ShiftControlClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ShiftControlDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ShiftControlQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/shiftControl")
@Slf4j
@RequiredArgsConstructor
public class ShiftControlController {
	
	private final ShiftControlClientService orderClientService;

	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final ShiftControlQueryRequest entityQueryRequest) {
		log.info("*** Pos Closed Cash, controller; fetch Pos Closed Cash all *");
		return ResponseEntity.ok(this.orderClientService.findAll(entityQueryRequest).getBody());
	}


	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@Valid @RequestBody final ShiftControlDto entityDto) {
		log.info("*** Pos Closed Cash, controller; save Pos Closed Cash *");
		return ResponseEntity.ok(this.orderClientService.save(entityDto).getBody());
	}

	@GetMapping("/findPosOrderRp/{shiftId}")
	ResponseEntity<GlobalReponse> findByShiftId(@PathVariable final Integer shiftId){
		log.info("*** Pos Closed Cash, controller; fetch Pos Closed Cash by shift id *");
		return ResponseEntity.ok(this.orderClientService.findByShiftId(shiftId).getBody());
	}

	@GetMapping("/findPaymentRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findPaymentByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderClientService.findPaymentByShiftId(shiftId).getBody());
	}


	@GetMapping("/findPurchaseRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findfindPurchaseRpByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderClientService.findPurchaseByShiftId(shiftId).getBody());
	}

	@GetMapping("/findReturnRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findfindReturnRpByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderClientService.findReturnByShiftId(shiftId).getBody());
	}

	@GetMapping("/findShiftInfo/{shiftId}")
	public ResponseEntity<GlobalReponse> findShiftInfo(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderClientService.findShiftInfo(shiftId)).getBody();
	}

	@GetMapping("/findNewestShift/{orgId}/{posTerminalId}")
	public ResponseEntity<GlobalReponse> getNewestShiftInfo(@PathVariable final Integer orgId, @PathVariable final Integer posTerminalId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderClientService.getNewestShiftInfo(orgId, posTerminalId)).getBody();
	}

}
