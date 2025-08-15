package com.dbiz.app.reportservice.resource;


import com.dbiz.app.reportservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
@Slf4j
@RequiredArgsConstructor
public class ReportResource {

    private final ReportService reportService;


    @PostMapping("/sale/getByServiceType")
    public ResponseEntity<GlobalReponse> getReportByServiceType(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByServiceType(dto));
    }

    @PostMapping("/sale/getByEmp")
    public ResponseEntity<GlobalReponse> getReportByEmployee(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByEmployee(dto));
    }

    @PostMapping("/sale/getByCus")
    public ResponseEntity<GlobalReponse> getReportByCustomer(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByCustomer(dto));
    }

    @PostMapping("/sale/getByPayment")
    public ResponseEntity<GlobalReponse> getReportByPayment(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByPayment(dto));
    }

    @PostMapping("/sale/getByCancel")
    public ResponseEntity<GlobalReponse> getReportByCancel(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByCancel(dto));
    }

    @PostMapping("/product/getByProductRevenue")
    public ResponseEntity<GlobalReponse> getReportByProductRevenue(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByProductRevenue(dto));
    }

    @PostMapping("/inv/getByOnHand")
    public ResponseEntity<GlobalReponse> getReportINVOnHand(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportINVOnHand(dto));
    }

    @PostMapping("/inv/getByInOut")
    public ResponseEntity<GlobalReponse> getReportINVInOut(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportINVInOut(dto));
    }

    @PostMapping("/production/getByDetail")
    public ResponseEntity<GlobalReponse> getReportProductionDT(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportProductionDT(dto));
    }

    @PostMapping("/production/getByDetail/v2")
    public ResponseEntity<GlobalReponse> getReportProductionDTV2(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportProductionDTV2(dto));
    }

    @PostMapping("/kitchen/getByDetail")
    public ResponseEntity<GlobalReponse> getReportKitchenOrderDT(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportKitchenOrderDT(dto));
    }

    @PostMapping("/shiftcontrol/getByTransfer")
    public ResponseEntity<GlobalReponse> getReportSCTransfer(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportSCTransfer(dto));
    }

    @PostMapping("/shiftcontrol/getByClose")
    public ResponseEntity<GlobalReponse> getReportSCToClose(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportSCToClose(dto));
    }


    @PostMapping("/acct/getByCus")
    public ResponseEntity<GlobalReponse> getReportAcctByCustomer(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportAcctByCustomer(dto));
    }

    @PostMapping("/acct/getByPay")
    public ResponseEntity<GlobalReponse> getReportAcctByPayment(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportAcctByPayment(dto));
    }

    @PostMapping("/cash")
    public ResponseEntity<GlobalReponse> getReportCashBook(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportCashBook(dto));
    }

    @PostMapping("/customer/debit")
    public ResponseEntity<GlobalReponse> getReportCustomerDebit(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportCustomerDebit(dto));
    }

    @PostMapping("/vendor/debit")
    public ResponseEntity<GlobalReponse> getReportVendorDebit(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportVendorDebit(dto));
    }

    @PostMapping("/invoice/revenue")
    public ResponseEntity<GlobalReponse> getReportInvoiceRevenue(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getRevenueProduction(dto));
    }

    @PostMapping("/ledge/material")
    public ResponseEntity<GlobalReponse> getReportMaterialLedger(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportDetailedMaterialLedger(dto));
    }

    @PostMapping("/expenses/business")
    public ResponseEntity<GlobalReponse> getReportCalculateTotalProduction(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportCalculateTotalProduction(dto));
    }

    @PostMapping("/business/performance")
    public ResponseEntity<GlobalReponse> getReportBusinessPerformance(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportBusinessPerformance(dto));
    }
}
