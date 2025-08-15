package com.dbiz.app.proxyclient.business.report.service;

import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "REPORT-SERVICE", contextId = "reportService", path = "/report-service/api/v1/report")
public interface ReportService {

    @PostMapping("/sale/getByServiceType")
    public ResponseEntity<GlobalReponse> getReportByServiceType(ReportReqDto dto);

    @PostMapping("/sale/getByEmp")
    public ResponseEntity<GlobalReponse> getReportByEmployee(@RequestBody ReportReqDto dto);

    @PostMapping("/sale/getByCus")
    public ResponseEntity<GlobalReponse> getReportByCustomer(@RequestBody ReportReqDto dto);
    @PostMapping("/sale/getByPayment")
    public ResponseEntity<GlobalReponse> getReportByPayment(@RequestBody ReportReqDto dto);

    @PostMapping("/sale/getByCancel")
    public ResponseEntity<GlobalReponse> getReportByCancel(@RequestBody ReportReqDto dto);

    @PostMapping("/product/getByProductRevenue")
    public ResponseEntity<GlobalReponse> getReportByProductRevenue(@RequestBody ReportReqDto dto);

    @PostMapping("/inv/getByOnHand")
    public ResponseEntity<GlobalReponse> getReportINVOnHand(@RequestBody ReportReqDto dto) ;

    @PostMapping("/inv/getByInOut")
    public ResponseEntity<GlobalReponse> getReportINVInOut(@RequestBody ReportReqDto dto);

    @PostMapping("/production/getByDetail")
    public ResponseEntity<GlobalReponse> getReportProductionDT(@RequestBody ReportReqDto dto);

    @PostMapping("/production/getByDetail/v2")
    public ResponseEntity<GlobalReponse> getReportProductionDTV2(@RequestBody ReportReqDto dto);

    @PostMapping("/kitchen/getByDetail")
    public ResponseEntity<GlobalReponse> getReportKitchenOrderDT(@RequestBody ReportReqDto dto);

    @PostMapping("/shiftcontrol/getByTransfer")
    public ResponseEntity<GlobalReponse> getReportSCTransfer(@RequestBody ReportReqDto dto);

    @PostMapping("/shiftcontrol/getByClose")
    public ResponseEntity<GlobalReponse> getReportSCToClose(@RequestBody ReportReqDto dto);

    @PostMapping("/acct/getByCus")
    public ResponseEntity<GlobalReponse> getReportAcctByCustomer(@RequestBody ReportReqDto dto);

    @PostMapping("/acct/getByPay")
    public ResponseEntity<GlobalReponse> getReportAcctByPayment(@RequestBody ReportReqDto dto);

    @PostMapping("/cash")
    public ResponseEntity<GlobalReponse> getReportCashBook(@RequestBody ReportReqDto dto);

    @PostMapping("/customer/debit")
    public ResponseEntity<GlobalReponse> getReportCustomerDebit(@RequestBody ReportReqDto dto);

    @PostMapping("/vendor/debit")
    public ResponseEntity<GlobalReponse> getReportVendorDebit(@RequestBody ReportReqDto dto);

    @PostMapping("/invoice/revenue")
    public ResponseEntity<GlobalReponse> getReportInvoiceRevenue(@RequestBody ReportReqDto dto);

    @PostMapping("/ledge/material")
    public ResponseEntity<GlobalReponse> getReportMaterialLedger(@RequestBody ReportReqDto dto);
    @PostMapping("/expenses/business")
    public ResponseEntity<GlobalReponse> getReportCalculateTotalProduction(@RequestBody ReportReqDto dto);

    @PostMapping("/business/performance")
    public ResponseEntity<GlobalReponse> getReportBusinessPerformance(@RequestBody ReportReqDto dto);
}
