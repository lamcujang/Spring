package com.dbiz.app.proxyclient.business.report.controller;


import com.dbiz.app.proxyclient.business.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/sale/getByServiceType")
    public ResponseEntity<GlobalReponse> getReportByServiceType(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByServiceType(dto)).getBody();
    }

    @PostMapping("/sale/getByEmp")
    public ResponseEntity<GlobalReponse> getReportByEmployee(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByEmployee(dto)).getBody();
    }

    @PostMapping("/sale/getByCus")
    public ResponseEntity<GlobalReponse> getReportByCustomer(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByCustomer(dto)).getBody();
    }

    @PostMapping("/sale/getByPayment")
    public ResponseEntity<GlobalReponse> getReportByPayment(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByPayment(dto)).getBody();
    }

    @PostMapping("/sale/getByCancel")
    public ResponseEntity<GlobalReponse> getReportByCancel(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByCancel(dto)).getBody();
    }

    @PostMapping("/product/getByProductRevenue")
    public ResponseEntity<GlobalReponse> getReportByProductRevenue(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportByProductRevenue(dto)).getBody();
    }

    @PostMapping("/inv/getByOnHand")
    public ResponseEntity<GlobalReponse> getReportINVOnHand(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportINVOnHand(dto)).getBody();
    }

    @PostMapping("/inv/getByInOut")
    public ResponseEntity<GlobalReponse> getReportINVInOut(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportINVInOut(dto)).getBody();
    }

    @PostMapping("/production/getByDetail")
    public ResponseEntity<GlobalReponse> getReportProductionDT(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportProductionDT(dto)).getBody();
    }

    @PostMapping("/production/getByDetail/v2")
    public ResponseEntity<GlobalReponse> getReportProductionDTV2(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportProductionDTV2(dto)).getBody();
    }

    @PostMapping("/kitchen/getByDetail")
    public ResponseEntity<GlobalReponse> getReportKitchenOrderDT(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportKitchenOrderDT(dto)).getBody();
    }

    @PostMapping("/shiftcontrol/getByTransfer")
    public ResponseEntity<GlobalReponse> getReportSCTransfer(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportSCTransfer(dto)).getBody();
    }

    @PostMapping("/shiftcontrol/getByClose")
    public ResponseEntity<GlobalReponse> getReportSCToClose(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportSCToClose(dto)).getBody();
    }

    @PostMapping("/acct/getByCus")
    public ResponseEntity<GlobalReponse> getReportAcctByCustomer(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportAcctByCustomer(dto)).getBody();
    }

    @PostMapping("/acct/getByPay")
    public ResponseEntity<GlobalReponse> getReportAcctByPayment(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportAcctByPayment(dto)).getBody();
    }

    @PostMapping("/cash")
    public ResponseEntity<GlobalReponse> getReportCashBook(@RequestBody ReportReqDto dto) {
        return  ResponseEntity.ok(this.reportService.getReportCashBook(dto)).getBody();
    }

    @PostMapping("/customer/debit")
    public ResponseEntity<GlobalReponse> getReportCustomerDebit(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportCustomerDebit(dto)).getBody();
    }

    @PostMapping("/vendor/debit")
    public ResponseEntity<GlobalReponse> getReportVendorDebit(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportVendorDebit(dto)).getBody();
    }

    @PostMapping("/invoice/revenue")
    public ResponseEntity<GlobalReponse> getReportInvoiceRevenue(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportInvoiceRevenue(dto)).getBody();
    }

    @PostMapping("/ledge/material")
    public ResponseEntity<GlobalReponse> getReportMaterialLedger(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportMaterialLedger(dto)).getBody();
    }

    @PostMapping("/expenses/business")
    public ResponseEntity<GlobalReponse> getReportCalculateTotalProduction(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportCalculateTotalProduction(dto)).getBody();
    }

    @PostMapping("/business/performance")
    public ResponseEntity<GlobalReponse> getReportBusinessPerformance(@RequestBody ReportReqDto dto) {
        return ResponseEntity.ok(this.reportService.getReportBusinessPerformance(dto)).getBody();
    }

}

