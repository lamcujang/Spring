package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "kitchenOrderLineClientService", path = "/order-service/api/v1/kitchenOrderLines")
public interface KitchenOrderLineClientService {
	@GetMapping("/{id}")
	ResponseEntity<GlobalReponse > findById(@PathVariable("id") final Integer id);
	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination > findAll(
			@SpringQueryMap final KitchenOrderLineRequest entityQueryRequest
	);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse>save(@RequestBody @Valid final KitchenOrderlineDto  entityDto);


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody @Valid final KitchenOrderlineDto entityDto);

	@PostMapping("/cancelQty")
	ResponseEntity<GlobalReponse> cancelQty(@RequestBody @Valid final KitchenOrderLineCancelRq rq);

	@PostMapping("/updateAllById")
	ResponseEntity<GlobalReponse> updateAllById(@RequestBody @Valid final UpdateAllKitchenLineByIdRequest rq);

	@GetMapping("/findAllProductSameSelected")
	ResponseEntity<GlobalReponse> findAllProductSameSelected(@SpringQueryMap GetKolSameProductVRequest request);


	@GetMapping("/getKcHistory")
	public ResponseEntity<GlobalReponse>findKcHistory(@SpringQueryMap KitchenOrderRequest request);

	@PostMapping("/sendNotifyRemind")
	public ResponseEntity<GlobalReponse> sendNotifyRemind(@RequestBody SendNotifycationRq request);
	@GetMapping("/getProductComboTest")
	public ResponseEntity<GlobalReponse> getProductCombo(@RequestParam("productId") Integer productId);
}










