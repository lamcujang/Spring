package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "tableClientServiceV2", path = "/order-service/api/v2/tables")
public interface TableV2ClientService {

	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final TableQueryRequest  tableQueryRequest);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final TableDto  entityDto);

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@GetMapping("/findAllTableAndReservationByDate")
	ResponseEntity<GlobalReponsePagination>findAllTableAndReservationByDate(@SpringQueryMap final TableQueryRequest tableQueryRequest);

}










