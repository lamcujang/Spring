package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.orderDto.request.ApplicablePromoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PromotionQueryRequest;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "promotionClientService", path = "/order-service/api/v1/promotion")
public interface PromotionClientService {


	@PostMapping("/save")
	ResponseEntity<GlobalReponse> save(@RequestBody PromotionDto entityDto);

	@PutMapping("/update")
	ResponseEntity<GlobalReponse> update(@RequestBody PromotionDto entityDto);

	@DeleteMapping("/delete/{id}")
	ResponseEntity<GlobalReponse> delete(@PathVariable Integer id);

	@DeleteMapping("/deletePromoMethod/{id}")
	ResponseEntity<GlobalReponse> deletePromoMethod(@PathVariable Integer id);


	@DeleteMapping("/deletePromoTime/{id}")
	ResponseEntity<GlobalReponse> deletePromoTime(@PathVariable Integer id);

	@GetMapping("/getAll")
	 ResponseEntity<GlobalReponsePagination>getAllPromotion(@SpringQueryMap PromotionQueryRequest request);

	@DeleteMapping("/deleteAssignOrg/{assignOrgId}")
	ResponseEntity<GlobalReponse> deleteAssignOrg(@PathVariable Integer assignOrgId);

	@DeleteMapping("/deleteAssignBpartnerGroup/{partnerGroupId}")
	ResponseEntity<GlobalReponse> deleteAssignBpartnerGroup(@PathVariable Integer partnerGroupId);

	@GetMapping("/fromCashier")
	ResponseEntity<GlobalReponsePagination> fromCashier(@SpringQueryMap PromotionQueryRequest request);

	@PostMapping("/apply")
	ResponseEntity<GlobalReponse> getApplicablePromos(@RequestBody ApplicablePromoReqDto reqDto);

}










