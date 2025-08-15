package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.WDSDetailHistory;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.orderRequest.GetKolSameProductVRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WDSDetaiHistorySpecification {

    public static Specification<WDSDetailHistory> orgId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("orgId"),  id );
    }

    public static Specification<WDSDetailHistory> equalProductId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("productId"),  id );
    }

    public static Specification<WDSDetailHistory> equalKitchenOrderId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("kitchenOrderId"),  id );
    }


    public static Specification<WDSDetailHistory> equalDate(String dateString) {
        if(dateString == null || dateString.isEmpty())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        return (Root<WDSDetailHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return (dateString==null || dateString.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(
                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
                    Date.valueOf(localDate)
            );
        };
    }


    public static Specification<WDSDetailHistory> greaterOrdered(String dateString) {
        if(dateString == null || dateString.isEmpty())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        return (Root<WDSDetailHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return (dateString == null || dateString.isEmpty())? criteriaBuilder.conjunction(): criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
                    Date.valueOf(localDate)
            );
        };
    }


    public static Specification<WDSDetailHistory> lessOrdered(String dateString) {
        if(dateString == null || dateString.isEmpty())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        return (Root<WDSDetailHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return (dateString == null || dateString.isEmpty())? criteriaBuilder.conjunction(): criteriaBuilder.lessThanOrEqualTo(
                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
                    Date.valueOf(localDate)
            );
        };
    }
    public static Specification<WDSDetailHistory> hasTenantId( ) {
        return (root, query, criteriaBuilder) ->   criteriaBuilder.equal(root.get("tenantId"),  AuditContext.getAuditInfo().getTenantId() );
    }

    public static Specification<WDSDetailHistory> warehouseId( Integer warehouseId, String role) {
        if( role.equals("KST"))
            return (root, query, criteriaBuilder) ->warehouseId == null ? criteriaBuilder.conjunction():criteriaBuilder.equal(root.get("warehouseId"), warehouseId );

        return (root, query, criteriaBuilder) ->criteriaBuilder.conjunction();
    }

    public static Specification<WDSDetailHistory> equalStatus(String status) {
        return (root, query, criteriaBuilder) -> status == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("orderLineStatus"),  status );
    }

    public static Specification<WDSDetailHistory> getEntity(GetKolSameProductVRequest request) {
        Specification<WDSDetailHistory> spec = Specification.where(null);
        spec = spec.and(orgId(request.getOrgId()));
        spec = spec.and(equalStatus(request.getOrderLineStatus()));
        spec = spec.and(hasTenantId());
        spec=spec.and(equalKitchenOrderId(request.getKitchenOrderId()));
        spec=spec.and(equalDate(request.getCurrentDate()));
        spec = spec.and(greaterOrdered(request.getDateFrom()));
        spec = spec.and(lessOrdered(request.getDateTo()));
        spec= spec.and(equalProductId(request.getEqualProductId()));
        spec= spec.and(warehouseId(request.getWarehouseId(), request.getRole()));
        return spec;
    }
}
