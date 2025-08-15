package com.dbiz.app.tenantservice.service;

import org.common.dbiz.dto.tenantDto.dashboard.request.TotalRevenueReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.tenantRequest.DashBoardCredential;

public interface DashBoardService {
    GlobalReponse getSummaryToday(Integer tenantId,Integer orgId);
    // d_top_product_procedure -> d_temp_sales_summary
    GlobalReponse salesSummary(DashBoardCredential rq);

    // d_revenue_procedure -> d_temp_sales_revenue
    // d_top_product_procedure -> d_temp_sales_summary
    GlobalReponse salesRevenue(DashBoardCredential rq);

    GlobalReponse getTotalRevenue(TotalRevenueReqDto rq);

    GlobalReponse getRevenueByEmp(TotalRevenueReqDto rq);

    GlobalReponse getRevenueByServiceType(TotalRevenueReqDto rq);

    GlobalReponse getCancelPrSummary(TotalRevenueReqDto rq);

    GlobalReponse getTop10Product(TotalRevenueReqDto rq);
}
