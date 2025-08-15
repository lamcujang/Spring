package com.dbiz.app.integrationservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.integrationservice.domain.view.IntegrationHistoryGetV;
import com.dbiz.app.integrationservice.helper.DateHelper;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Date;

public class IntegrationHistoryGetVSpecification{

    public static Specification<IntegrationHistoryGetV> equalFlow(String flow) {
        return (root, query, criteriaBuilder) -> flow == null ? null : criteriaBuilder.equal(root.get("flowValue"), flow);
    }

    public static Specification<IntegrationHistoryGetV> equalStatus(String status) {
        return (root, query, criteriaBuilder) -> status == null ? null : criteriaBuilder.equal(root.get("statusValue"), status);
    }

    public static Specification<IntegrationHistoryGetV> equalIntegrationType(String type) {
        return (root, query, criteriaBuilder) -> (type == null|| type.isEmpty()) ? null : criteriaBuilder.equal(root.get("integrationType"), type);
    }
    private static Specification<IntegrationHistoryGetV> historyDate(String date) {
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
    public static Specification<IntegrationHistoryGetV> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<IntegrationHistoryGetV> getErpIntegrationSpecification(IntegrationHistoryQueryRequest ErpIntegrationQueryRequest) {
        Specification<IntegrationHistoryGetV> spec = Specification.where(null);
        spec = spec.and(hasTenantId());
        if(ErpIntegrationQueryRequest.getDateHistory() != null && !ErpIntegrationQueryRequest.getDateHistory().isEmpty())
            spec = spec.and(historyDate(ErpIntegrationQueryRequest.getDateHistory()));
        if(ErpIntegrationQueryRequest.getIntStatus() != null && !ErpIntegrationQueryRequest.getIntStatus().isEmpty())
            spec = spec.and(equalStatus(ErpIntegrationQueryRequest.getIntStatus()));
        if(ErpIntegrationQueryRequest.getIntFlow() != null && !ErpIntegrationQueryRequest.getIntFlow().isEmpty())
            spec = spec.and(equalFlow(ErpIntegrationQueryRequest.getIntFlow()));
        spec=spec.and(equalIntegrationType(ErpIntegrationQueryRequest.getIntegrationType()));
        return spec;
    }

}

