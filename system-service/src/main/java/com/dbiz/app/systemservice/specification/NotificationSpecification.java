package com.dbiz.app.systemservice.specification;

import com.dbiz.app.systemservice.domain.Notification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.systemRequest.NotificationQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {

    public static Specification<Notification> hasTenantId() {
      return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<Notification>hasStatus(String status) {
      return (root, query, criteriaBuilder) ->status == null ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Notification>hasType(String type) {
      return (root, query, criteriaBuilder) ->type == null ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("notificationType"), type);
    }
    public static Specification<Notification> getEntitySpecification(NotificationQueryRequest queryRequest) {
        Specification<Notification> spec = Specification.where(null);

        spec = spec.and(hasTenantId());
        spec = spec.and(hasStatus(queryRequest.getStatus()));
        spec = spec.and(hasType(queryRequest.getType()));
        return spec;
    }



}
