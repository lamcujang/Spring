package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.BulkPosOrderDto;
import org.common.dbiz.dto.orderDto.GeneratePOSBillNumberDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelPosOrderRequest;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.common.dbiz.request.orderRequest.PosOrderQueryRequest;
import org.common.dbiz.request.orderRequest.ReportPosQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "ORDER-SERVICE", contextId = "posOrderV2ClientService", path = "/order-service/api/v2/posOrders")
public interface PosOrderV2ClientService {
	@GetMapping("/{posOrderId}")
	ResponseEntity<GlobalReponse > findById(@PathVariable("posOrderId") final Integer posOrderId);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final PosOrderQueryRequest  posOrderQueryRequest
			);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final PosOrderDto entityDto);


	@DeleteMapping("/delete/{posOrderId}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("posOrderId") final Integer posOrderId);

	@PostMapping("/cancel/{posOrderId}")
	public ResponseEntity<GlobalReponse> cancelOrder(@PathVariable("posOrderId") final Integer posOrderId);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PosOrderDto entityDto);
	@PostMapping("/cancel")
	public ResponseEntity<GlobalReponse> cancelOrder(@RequestBody @Valid final CancelPosOrderRequest  DTO);
	@GetMapping("/findAllList")
	ResponseEntity<GlobalReponsePagination> findAllPosList(@SpringQueryMap final PosOrderListQueryRequest request);

	@PostMapping("/generateBillNo")
	public ResponseEntity<GlobalReponse> generatePOSBillNumber(@RequestBody GeneratePOSBillNumberDto DTO) ;

	@PostMapping("/intSave")
	public ResponseEntity<GlobalReponse> intSave(@RequestBody List<PosOrderDto> entityDto);

	@GetMapping("/reportOrder")
	public ResponseEntity<GlobalReponsePagination> reportOrder(@SpringQueryMap final ReportPosQueryRequest request);

	@PostMapping("/saveByRqOrder")
	public ResponseEntity<GlobalReponse> requestOrder(@RequestBody PosOrderDto DTO);

	@GetMapping("/findAllListById/{posOrderId}")
	public ResponseEntity<GlobalReponse> findPosListById(@PathVariable("posOrderId") final Integer posOrderId);

	@GetMapping("/retail")
	public ResponseEntity<GlobalReponsePagination> findPosOrdersRetail(@SpringQueryMap PosOrderListQueryRequest DTO);

	@PostMapping("/retail/fetch/detail")
	public ResponseEntity<GlobalReponse> findPosOrderRetailById(@RequestBody PosOrderReqDto DTO);

	@DeleteMapping("/{id}")
	public ResponseEntity<GlobalReponse> deletePosOrder(@PathVariable Integer id);

	@PutMapping("/delete/withReason")
	ResponseEntity<GlobalReponse> deleteWithReason(@RequestBody @Valid final PosOrderDto DTO);

	@PostMapping("/header/update")
	public ResponseEntity<GlobalReponse> updateOrderHeader(@RequestBody org.common.dbiz.dto.paymentDto.PosOrderDto DTO);

	@PostMapping("/bulk")
	public ResponseEntity<GlobalReponse> saveBulkOrder(@RequestBody BulkPosOrderDto DTO);
}










