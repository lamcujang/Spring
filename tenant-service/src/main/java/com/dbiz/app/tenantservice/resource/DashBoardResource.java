package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.dashboard.request.TotalRevenueReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.tenantRequest.DashBoardCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/dashBoard"})
@Slf4j
@RequiredArgsConstructor
public class DashBoardResource {

    private final DashBoardService service;


    @GetMapping("/getSummaryToday")
    public ResponseEntity<GlobalReponse> getSummaryToday(@RequestParam(name = "orgId") Integer orgId) {
        log.info("*** Get Summary, Get Summary Today *");
        GlobalReponse response = service.getSummaryToday(AuditContext.getAuditInfo().getTenantId(), orgId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getSalesSummary")
    public ResponseEntity<GlobalReponse> getSalesSummary(@RequestBody DashBoardCredential request) {
        log.info("*** Get Summary, Get Sales Summary *");
        GlobalReponse response = service.salesSummary(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getSalesRevenue")
    public ResponseEntity<GlobalReponse> getSalesRevenue(@RequestBody DashBoardCredential request) {
        log.info("*** Get Summary, Get Sales Revenue *");
        GlobalReponse response = service.salesRevenue(request);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/getTotalRevenue")
    public ResponseEntity<GlobalReponse> getRpSalesRevenue(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Report Sales Revenue *");
        return ResponseEntity.ok(service.getTotalRevenue(request));
    }

    @PostMapping("/getRevenueByEmp")
    public ResponseEntity<GlobalReponse> getRevenueByEmp(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Revenue By Employee*");
        return ResponseEntity.ok(service.getRevenueByEmp(request));
    }

    @PostMapping("/getRevenueByServiceType")
    public ResponseEntity<GlobalReponse> getRevenueByServiceType(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Revenue By Service Type*");
        return ResponseEntity.ok(service.getRevenueByServiceType(request));
    }

    @PostMapping("/getCancelPrSummary")
    public ResponseEntity<GlobalReponse> getCancelPrSummary(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Cancel By Summary*");
        return ResponseEntity.ok(service.getCancelPrSummary(request));
    }

    @PostMapping("/getTop10Product")
    public ResponseEntity<GlobalReponse> getTop10Product(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get TOP 10 Product*");
        return ResponseEntity.ok(service.getTop10Product(request));
    }
}
