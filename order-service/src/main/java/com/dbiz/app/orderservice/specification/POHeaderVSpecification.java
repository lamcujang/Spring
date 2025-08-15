package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.view.POHeaderV;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.Instant;

public class POHeaderVSpecification {

    private static Specification<POHeaderV> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    private static Specification<POHeaderV> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<POHeaderV> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    private static Specification<POHeaderV> id(Integer poId) {
        return (root, query, criteriaBuilder) -> poId == null ? null : criteriaBuilder.equal(root.get("id"), poId);
    }

    private static Specification<POHeaderV> nameReference(String nameReference) {
        return (root, query, criteriaBuilder) -> nameReference == null ? null : criteriaBuilder.equal(root.get("nameReference"), nameReference);
    }

    private static Specification<POHeaderV> status(String status) {
        return (root, query, criteriaBuilder) -> status == null ? null : criteriaBuilder.equal(root.get("orderStatus"), status);
    }

    private static Specification<POHeaderV> vendornameOrDocumentNo(String searchKey) {
        return (root, query, criteriaBuilder) -> {
            if (searchKey == null) {
                return null;
            }
            // Create the predicates for vendorName and documentNo
            Predicate vendorNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("vendorDto").get("vendorName")),
                    "%" + searchKey.toLowerCase() + "%"
            );

            Predicate documentNoPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("documentNo")),
                    "%" + searchKey.toLowerCase() + "%"
            );

            // Combine the predicates using OR logical operator
            return criteriaBuilder.or(vendorNamePredicate, documentNoPredicate);
        };
    }

    private static Specification<POHeaderV> vendornameOrDocumentNoOrUsername(String searchKey) {
        return (root, query, criteriaBuilder) -> {
            if (searchKey == null) {
                return null;
            }
            // Create the predicates for vendorName and documentNo
            Predicate vendorNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("vendorDto").get("vendorName")),
                    "%" + searchKey.toLowerCase() + "%"
            );

            Predicate documentNoPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("documentNo")),
                    "%" + searchKey.toLowerCase() + "%"
            );

            Predicate userNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("userDto").get("fullName")),
                    "%" + searchKey.toLowerCase() + "%"
            );

            // Combine the predicates using OR logical operator
            return criteriaBuilder.or(vendorNamePredicate, documentNoPredicate,userNamePredicate);
        };
    }


    private static Specification<POHeaderV> betweenAt(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null || toDate == null) {
                return null;
            }
            Instant from = DateHelper.toInstantUTC(fromDate + " 00:00:00");
            Instant to = DateHelper.toInstantUTC(toDate + " 23:59:59");

            // Use `between` on `orderDate` with Instant values
            Expression<Instant> orderDateExpression = root.get("orderDate");
            return criteriaBuilder.between(orderDateExpression, from, to);
        };
    }


    public static Specification<POHeaderV> getSpecification(POHeaderVRequest request) {
        return Specification.where(tenantId(AuditContext.getAuditInfo().getTenantId())
                .and(orgId(request.getOrgId()))
                .and(isActive(request.getIsActive()))
                .and(id(request.getId()))
                .and(nameReference(request.getNameReference()))
                //.and(vendornameOrDocumentNo(request.getSearchKey()))
                .and(vendornameOrDocumentNoOrUsername(request.getSearchKey()))
                .and(betweenAt(request.getDateFrom(), request.getDateTo()))
                .and(status(request.getOrderStatus()))
                .and(id(request.getId())));
    }
}
