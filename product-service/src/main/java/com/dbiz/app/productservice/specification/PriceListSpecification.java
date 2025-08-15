package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.helper.DateHelper;
import com.dbiz.app.tenantservice.domain.PosTerminal;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Subquery;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class PriceListSpecification {

    public static Specification<PriceList> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<PriceList> hasIsActive(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("isActive"), "%" + keyword + "%");
    }

    public static Specification<PriceList> hasIsSaleprice(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("hasIsSaleprice"), "%" + keyword + "%");
    }

    public static Specification<PriceList> hasGreaterThanEqualFromDate(String fromDate) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("fromDate"), DateHelper.toInstantDateUTC(fromDate));
    }

    public static Specification<PriceList> hasLessThanEqualTODate(String toDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("toDate"), DateHelper.toInstantDateUTC(toDate));
    }

    public static Specification<PriceList> hasGeneralPriceList(String generalPriceList) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("generalPriceList"), generalPriceList);
    }

    public static Specification<PriceList> hasOrgIds(Integer[] orgIds) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Object, Object> orgJoin = root.join("priceListOrg", JoinType.INNER);
            return orgJoin.get("id").in(orgIds);
        };
    }

    public static Specification<PriceList> hasOrgId(Integer orgId) {

        return (root, query, criteriaBuilder) -> {
            if(orgId == 0)
                return criteriaBuilder.conjunction();
            query.distinct(true);
            Join<Object, Object> orgJoin = root.join("priceListOrg", JoinType.INNER);
            return orgJoin.get("id").in(orgId);
        };
    }

    public static Specification<PriceList> hasOrgIds(List<Integer> orgIds) {

        return (root, query, criteriaBuilder) -> {
            if(orgIds.isEmpty())
                return criteriaBuilder.conjunction();
            query.distinct(true);
            Join<Object, Object> orgJoin = root.join("priceListOrg", JoinType.INNER);
            return orgJoin.get("id").in(orgIds);
        };
    }
    public static Specification<PriceList> validDate(String validDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("toDate"), DateHelper.toInstantDateUTC(validDate));
    }

    public static Specification<PriceList> hasValidDateInRangeAndNotGeneralPrice(String validDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get("generalPriceList"), "Y"),
                criteriaBuilder.greaterThanOrEqualTo(root.get("toDate"), DateHelper.toInstantDateUTC(validDate))
        );
    }


    public static Specification<PriceList> hasIsAll() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
//        return (root, query, criteriaBuilder) -> {
//            Join<Object, Object> orgJoin = root.join("priceListOrg", JoinType.LEFT);
//            return criteriaBuilder.equal(orgJoin.get("isAll"), "Y");
//        };
    }

    public static Specification<PriceList> hasGeneralPrice() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("generalPriceList"), "Y");

    }

    public static Specification<PriceList> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<PriceList> hasIsActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), "Y");
    }

    public static Specification<PriceList> inPriceListId( Integer id )
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }
    
    public static Specification<PriceList> getEntitySpecification(PriceListQueryRequest queryRequest) {
        Specification<PriceList> spec = Specification.where(null);
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
            spec = spec.and(hasGeneralPriceList(queryRequest.getGeneralPriceList()));
        }
//        if(queryRequest.getOrgIds() != null  )
//        {
//            spec = spec.and(hasIsActive());
//            spec = spec.and(hasOrgIds(queryRequest.getOrgIds()));
//        }
//        if (queryRequest.getOrgId() != null ) {
//            spec = spec.and(hasOrgId(queryRequest.getOrgId()));
//        }
        if (queryRequest.getIsAll() != null) {
            spec = spec.and(hasIsAll());
        }

        if(queryRequest.getValidDate()!=null)
        {
            spec = spec.and(hasGreaterThanEqualFromDate(queryRequest.getValidDate()));
            spec = spec.and(hasLessThanEqualTODate(queryRequest.getValidDate()));
        }
        spec = spec.and(hasTenantId());
        spec = spec.or(hasGeneralPrice());
        return spec;
    }
}
