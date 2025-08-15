package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "ORDER-SERVICE", contextId = "kitchenOrderClientService", path = "/order-service/api/v1/kitchenOrders")
public interface KitchenOrderClientService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final KitchenOrderRequest  entityQueryRequest
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final KitchenOrderDto  entityDto);


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final KitchenOrderDto entityDto);

	@GetMapping("/findOrderByStatus")
	ResponseEntity<GlobalReponsePagination> findByStatus(
			@SpringQueryMap final KitchenOrderRequest entityQueryRequest
	);

	@GetMapping("/findKOrderGroupByProduct")
	ResponseEntity<GlobalReponsePagination> findKOrderGroupByOrder(
			@SpringQueryMap final KitchenOrderRequest entityQueryRequest
	);


	@GetMapping("/getDocNo")
	ResponseEntity<GlobalReponsePagination>findDocNo(@SpringQueryMap KitchenOrderRequest kOrderId);

	@PostMapping("/intSave")
	public ResponseEntity<GlobalReponse> intSave(@RequestBody List<KitchenOrderDto> entityDto);

	@GetMapping("/status")
	public ResponseEntity<GlobalReponse>getLimitedKitchenOrderStatus(@SpringQueryMap KitchenOrderRequest dto);
}










