package com.dbiz.app.integrationservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.helper.DateHelper;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Date;

public class IntegrationHistorySpecification {

    public static Specification<IntegrationHistory> equalFlow(String flow) {
        return (root, query, criteriaBuilder) -> flow == null ? null : criteriaBuilder.equal(root.get("intFlow"), flow);
    }

    public static Specification<IntegrationHistory> equalStatus(String status) {
        return (root, query, criteriaBuilder) -> status == null ? null : criteriaBuilder.equal(root.get("intStatus"), status);
    }
    private static Specification<IntegrationHistory> historyDate(String date) {
//        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }  Instant reservationInstant = DateHelper.toInstant(date);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("intDate"));
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            return criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
        };
    }

    private static Specification<IntegrationHistory> historyDateTo(String date) {
//        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }  Instant reservationInstant = DateHelper.toInstant(date);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("intDate"));
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            return criteriaBuilder.lessThanOrEqualTo(reservationTimeDate, reservationDateOnly);
        };
    }

    private static Specification<IntegrationHistory> historyDateFrom(String date) {
//        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }  Instant reservationInstant = DateHelper.toInstant(date);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("intDate"));
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            return criteriaBuilder.greaterThanOrEqualTo(reservationTimeDate, reservationDateOnly);
        };
    }
    public static Specification<IntegrationHistory> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<IntegrationHistory> getErpIntegrationSpecification(IntegrationHistoryQueryRequest ErpIntegrationQueryRequest) {
        Specification<IntegrationHistory> spec = Specification.where(null);

        if(ErpIntegrationQueryRequest.getDateHistory() != null && !ErpIntegrationQueryRequest.getDateHistory().isEmpty())
            spec = spec.and(historyDate(ErpIntegrationQueryRequest.getDateHistory()));
        if(ErpIntegrationQueryRequest.getIntStatus() != null && !ErpIntegrationQueryRequest.getIntStatus().isEmpty())
            spec = spec.and(equalStatus(ErpIntegrationQueryRequest.getIntStatus()));
        if(ErpIntegrationQueryRequest.getIntFlow() != null && !ErpIntegrationQueryRequest.getIntFlow().isEmpty())
            spec = spec.and(equalFlow(ErpIntegrationQueryRequest.getIntFlow()));
        if(ErpIntegrationQueryRequest.getIntDateFrom() != null)
            spec = spec.and(historyDateFrom(ErpIntegrationQueryRequest.getIntDateFrom()));
        if(ErpIntegrationQueryRequest.getIntDateTo() != null)
            spec = spec.and(historyDateTo(ErpIntegrationQueryRequest.getIntDateTo()));
        spec = spec.and(hasTenantId());

        return spec;
    }
}
