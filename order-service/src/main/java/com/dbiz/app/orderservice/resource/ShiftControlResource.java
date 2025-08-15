package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.ShiftControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.ShiftControlDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ShiftControlQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/shiftControl")
@Slf4j
@RequiredArgsConstructor
public class ShiftControlResource {
	
	private final ShiftControlService orderService;


	@GetMapping("/findAll")
	public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final ShiftControlQueryRequest entityQueryRequest) {
		log.info("*** Pos Close Cash, controller; fetch all *");
		return ResponseEntity.ok(this.orderService.findAll(entityQueryRequest));
	}

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> save(@Valid @RequestBody final ShiftControlDto entityDto) {
		log.info("*** Pos Close Cash, controller; save *");
		return ResponseEntity.ok(this.orderService.save(entityDto));
	}


	@GetMapping("/findPosOrderRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch by shift id *");
		return ResponseEntity.ok(this.orderService.findAllOrderByShiftId(shiftId));
	}

	@GetMapping("/findPaymentRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findPaymentByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderService.findAllPaymentByShiftId(shiftId));
	}

	@GetMapping("/findPurchaseRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findPurchaseByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderService.findAllPaymentByShiftId(shiftId));
	}

	@GetMapping("/findReturnRp/{shiftId}")
	public ResponseEntity<GlobalReponse> findReturnByShiftId(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderService.findAllPaymentByShiftId(shiftId));
	}

	@GetMapping("/findShiftInfo/{shiftId}")
	public ResponseEntity<GlobalReponse> findShiftInfo(@PathVariable final Integer shiftId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderService.getShiftInfoById(shiftId));
	}

	@GetMapping("/findNewestShift/{orgId}/{posTerminalId}")
	public ResponseEntity<GlobalReponse> getNewestShiftInfo(@PathVariable final Integer orgId, @PathVariable final Integer posTerminalId) {
		log.info("*** Pos Close Cash, controller; fetch payment by shift id *");
		return ResponseEntity.ok(this.orderService.getNewestShiftInfo(orgId, posTerminalId));
	}
}










