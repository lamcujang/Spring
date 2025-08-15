package com.dbiz.app.tenantservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.PrintReport;
import org.common.dbiz.request.tenantRequest.PrintReportQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PrintReportSpecification {

    public static Specification<PrintReport> hasReportType(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("reportType"),  keyword);
    }
    public static Specification<PrintReport> hasDefault(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDefault"),  keyword);
    }
    public static Specification<PrintReport> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<PrintReport> getPrintReportSpecification(PrintReportQueryRequest PrintReportQueryRequest) {
        Specification<PrintReport> spec = Specification.where(null);

        if(PrintReportQueryRequest.getReportType() != null){
            spec= spec.and(hasReportType(PrintReportQueryRequest.getReportType()));
        }
        if(PrintReportQueryRequest.getIsDefault() != null){
            spec= spec.and(hasDefault(PrintReportQueryRequest.getIsDefault().toString()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
