package com.dbiz.app.productservice.specification;

import com.dbiz.app.productservice.domain.PriceListOrg;
import com.dbiz.app.productservice.domain.view.PriceListV;
import com.dbiz.app.productservice.helper.DateHelper;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Subquery;

public class PriceListViewSpecification {

    public static Specification<PriceListV> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<PriceListV> hasIsActive(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("isActive"), "%" + keyword + "%");
    }

    public static Specification<PriceListV> hasIsSaleprice(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("hasIsSaleprice"), "%" + keyword + "%");
    }

    public static Specification<PriceListV> hasGreaterThanEqualFromDate(String fromDate) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("fromDate"), DateHelper.toInstantDateUTC(fromDate));
    }

    public static Specification<PriceListV> hasLessThanEqualTODate(String toDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("toDate"), DateHelper.toInstantDateUTC(toDate));
    }

    public static Specification<PriceListV> hasGeneralPriceListV(String generalPriceListV) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("generalPriceList"), generalPriceListV);
    }

    public static Specification<PriceListV> hasOrgIds(Integer[] orgIds) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Object, Object> orgJoin = root.join("PriceListOrg", JoinType.INNER);
            return orgJoin.get("id").in(orgIds);
        };
    }

    public static Specification<PriceListV> hasOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> {
            if (orgId == null) {
                return criteriaBuilder.conjunction();
            }

            Subquery<Integer> subquery = query.subquery(Integer.class);
            var receiptOtherOrg = subquery.from(PriceListOrg.class);
            subquery.select(receiptOtherOrg.get("pricelistId"))
                    .where(criteriaBuilder.equal(receiptOtherOrg.get("orgId"), orgId));

            return criteriaBuilder.in(root.get("priceListId")).value(subquery);
        };
//        return (root, query, criteriaBuilder) -> {
//            query.distinct(true);
//            Join<Object, Object> orgJoin = root.join("PriceListOrg", JoinType.INNER);
//            return orgJoin.get("id").in(orgId);
//        };
    }

    public static Specification<PriceListV> hasIsAll() {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> orgJoin = root.join("PriceListOrg", JoinType.LEFT);
            return criteriaBuilder.equal(orgJoin.get("isAll"), "Y");
        };
    }

    public static Specification<PriceListV> hasGeneralPrice() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("generalPriceList"), "Y");

    }

    public static Specification<PriceListV> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<PriceListV> equalPosTerminalId(Integer posTerminalId) {
        return (root, query, criteriaBuilder) -> posTerminalId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("posTerminalId"), posTerminalId);
    }
    public static Specification<PriceListV> hasIsActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), "Y");
    }
    
    public static Specification<PriceListV> getEntitySpecification(PriceListQueryRequest queryRequest) {
        Specification<PriceListV> spec = Specification.where(null);
        if (queryRequest.getName() != null) {
            spec = spec.and(hasNameLike(queryRequest
                    .getName()));
        }
        if (queryRequest.getIsActive() != null) {
            spec = spec.and(hasIsActive(queryRequest.getIsActive()));
        }
        if (queryRequest.getIsSaleprice() != null) {
            spec = spec.and(hasIsSaleprice(queryRequest.getIsSaleprice()));
        }
        if (queryRequest.getFromDate() != null) {
            spec = spec.and(hasGreaterThanEqualFromDate(queryRequest.getFromDate()));
        }
        if (queryRequest.getToDate() != null) {
            spec = spec.and(hasLessThanEqualTODate(queryRequest.getToDate()));
        }

        if (queryRequest.getGeneralPriceList() != null) {
            spec = spec.and(hasGeneralPriceListV(queryRequest.getGeneralPriceList()));
        }
//        if(queryRequest.getOrgIds() != null  )
//        {
//            spec = spec.and(hasIsActive());
//            spec = spec.and(hasOrgIds(queryRequest.getOrgIds()));
//        }

        if (queryRequest.getPosTerminalId() == null) {
            spec = spec.and(hasOrgId(queryRequest.getOrgId()));
        }
        if (queryRequest.getIsAll() != null) {
            spec = spec.and(hasIsAll());
        }

        if(queryRequest.getValidDate()!=null)
        {
            spec = spec.and(hasGreaterThanEqualFromDate(queryRequest.getValidDate()));
            spec = spec.and(hasLessThanEqualTODate(queryRequest.getValidDate()));
        }
        spec = spec.and(equalPosTerminalId(queryRequest.getPosTerminalId()));
        spec = spec.or(hasGeneralPrice());
        spec = spec.and(hasTenantId());
        return spec;
    }
}
