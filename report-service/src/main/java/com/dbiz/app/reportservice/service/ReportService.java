package com.dbiz.app.reportservice.service;

import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;

public interface ReportService {

    GlobalReponse getReportByServiceType(ReportReqDto dto);
    GlobalReponse getReportByEmployee(ReportReqDto dto);
    GlobalReponse getReportByCustomer(ReportReqDto dto);
    GlobalReponse getReportByPayment(ReportReqDto dto);
    GlobalReponse getReportByCancel(ReportReqDto dto);
    GlobalReponse getReportByProductRevenue(ReportReqDto dto);

    GlobalReponse getReportINVOnHand(ReportReqDto dto);
    GlobalReponse getReportINVInOut(ReportReqDto dto);
    GlobalReponse getReportProductionDT(ReportReqDto dto);
    GlobalReponse getReportProductionDTV2(ReportReqDto dto);

    GlobalReponse getReportKitchenOrderDT(ReportReqDto dto);
    GlobalReponse getReportSCTransfer(ReportReqDto dto);
    GlobalReponse getReportSCToClose(ReportReqDto dto);
    GlobalReponse getReportAcctByCustomer(ReportReqDto dto);
    GlobalReponse getReportAcctByPayment(ReportReqDto dto);

    GlobalReponse getReportCashBook(ReportReqDto dto);
    GlobalReponse getReportCustomerDebit(ReportReqDto req);
    GlobalReponse getReportVendorDebit(ReportReqDto req);
    GlobalReponse getRevenueProduction(ReportReqDto req);
    GlobalReponse getReportDetailedMaterialLedger(ReportReqDto req);
    GlobalReponse getReportCalculateTotalProduction(ReportReqDto dto);
    GlobalReponse getReportBusinessPerformance(ReportReqDto dto);
}
