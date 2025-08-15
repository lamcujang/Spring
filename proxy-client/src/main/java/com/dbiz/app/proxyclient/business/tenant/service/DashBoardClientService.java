package com.dbiz.app.proxyclient.business.tenant.service;


import org.common.dbiz.dto.tenantDto.dashboard.request.TotalRevenueReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.tenantRequest.DashBoardCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TENANT-SERVICE", contextId = "dashBoardClientService", path = "/tenant-service/api/v1/dashBoard", decode404 = true)
public interface DashBoardClientService {
	@GetMapping("/getSummaryToday")
	ResponseEntity<GlobalReponse> getSummaryToday(@RequestParam(name = "orgId") Integer orgId);
	@PostMapping("/getSalesSummary")
	ResponseEntity<GlobalReponse> getSalesSummary(@RequestBody DashBoardCredential request);

	@PostMapping("/getSalesRevenue")
	ResponseEntity<GlobalReponse> getSalesRevenue(@RequestBody DashBoardCredential request);

	@PostMapping("/getTotalRevenue")
	public ResponseEntity<GlobalReponse> getRpSalesRevenue(@RequestBody TotalRevenueReqDto request);

	@PostMapping("/getRevenueByEmp")
	public ResponseEntity<GlobalReponse> getRevenueByEmp(@RequestBody TotalRevenueReqDto request);

	@PostMapping("/getRevenueByServiceType")
	public ResponseEntity<GlobalReponse> getRevenueByServiceType(@RequestBody TotalRevenueReqDto request);

	@PostMapping("/getCancelPrSummary")
	public ResponseEntity<GlobalReponse> getCancelPrSummary(@RequestBody TotalRevenueReqDto request);

	@PostMapping("/getTop10Product")
	public ResponseEntity<GlobalReponse> getTop10Product(@RequestBody TotalRevenueReqDto request);
}










