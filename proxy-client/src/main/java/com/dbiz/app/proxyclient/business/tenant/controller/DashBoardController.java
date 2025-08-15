package com.dbiz.app.proxyclient.business.tenant.controller;

    import com.dbiz.app.proxyclient.business.tenant.service.DashBoardClientService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.common.dbiz.dto.tenantDto.dashboard.request.TotalRevenueReqDto;
    import org.common.dbiz.payload.GlobalReponse;
    import org.common.dbiz.request.tenantRequest.DashBoardCredential;
    import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashBoard")
@RequiredArgsConstructor
@Slf4j
public class DashBoardController {
    private final DashBoardClientService service;

    @GetMapping("/getSummaryToday")
    ResponseEntity<GlobalReponse> getSummaryToday(@RequestParam(name = "orgId") Integer orgId)
    {
        log.info("*** Get Summary, Get Summary Today *");
        return service.getSummaryToday(orgId);
    };
    @PostMapping("/getSalesSummary")
    ResponseEntity<GlobalReponse> getSalesSummary(@RequestBody DashBoardCredential request){
        log.info("*** Get Summary, Get Sales Summary *");
        return service.getSalesSummary(request);
    };

    @PostMapping("/getSalesRevenue")
    ResponseEntity<GlobalReponse> getSalesRevenue(@RequestBody DashBoardCredential request){
        log.info("*** Get Summary, Get Sales Revenue *");
        return service.getSalesRevenue(request);
    };

    @PostMapping("/getTotalRevenue")
    public ResponseEntity<GlobalReponse> getRpSalesRevenue(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Report Sales Revenue *");
        return ResponseEntity.ok(service.getRpSalesRevenue(request)).getBody();
    }

    @PostMapping("/getRevenueByEmp")
    public ResponseEntity<GlobalReponse> getRevenueByEmp(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Revenue By Employee*");
        return ResponseEntity.ok(service.getRevenueByEmp(request)).getBody();
    }

    @PostMapping("/getRevenueByServiceType")
    public ResponseEntity<GlobalReponse> getRevenueByServiceType(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Revenue By Service Type*");
        return ResponseEntity.ok(service.getRevenueByServiceType(request)).getBody();
    }

    @PostMapping("/getCancelPrSummary")
    public ResponseEntity<GlobalReponse> getCancelPrSummary(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get Cancel By Summary*");
        return ResponseEntity.ok(service.getCancelPrSummary(request)).getBody();
    }

    @PostMapping("/getTop10Product")
    public ResponseEntity<GlobalReponse> getTop10Product(@RequestBody TotalRevenueReqDto request) {
        log.info("*** Get Summary, Get TOP 10 Product*");
        return ResponseEntity.ok(service.getTop10Product(request)).getBody();
    }


}
