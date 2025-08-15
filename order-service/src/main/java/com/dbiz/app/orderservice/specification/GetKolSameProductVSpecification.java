package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.view.GetKolSameProductV;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.orderRequest.GetKolSameProductVRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GetKolSameProductVSpecification {
    public static Specification<GetKolSameProductV> notEqual(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.notEqual(root.get("id"),  id );
    }
    public static Specification<GetKolSameProductV> orgId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("orgId"),  id );
    }
    public static Specification<GetKolSameProductV> notEqualProductId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("productId"),  id );
    }
    public static Specification<GetKolSameProductV> equalProductId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("productId"),  id );
    }

    public static Specification<GetKolSameProductV> equalKitchenOrderId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("kitchenOrderId"),  id );
    }

    public static Specification<GetKolSameProductV> equalFloorId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("floorId"),  id );
    }

    public static Specification<GetKolSameProductV> equalTableId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("tableId"),  id );
    }

//    public static Specification<GetKolSameProductV> hasOrderDate(String dateString) {
//        return (Root<GetKolSameProductV> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate localDate = LocalDate.parse(dateString, formatter);
//
//            return criteriaBuilder.equal(
//                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
//                    Date.valueOf(localDate)
//            );
//        };
//    }

    public static Specification<GetKolSameProductV> equalDate(String dateString) {
        if(dateString == null || dateString.isEmpty())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        return (Root<GetKolSameProductV> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return (dateString==null || dateString.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(
                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
                    Date.valueOf(localDate)
            );
        };
    }


    public static Specification<GetKolSameProductV> greaterOrdered(String dateString) {
        if(dateString == null || dateString.isEmpty())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        return (Root<GetKolSameProductV> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return (dateString == null || dateString.isEmpty())? criteriaBuilder.conjunction(): criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
                    Date.valueOf(localDate)
            );
        };
    }


    public static Specification<GetKolSameProductV> lessOrdered(String dateString) {
        if(dateString == null || dateString.isEmpty())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        return (Root<GetKolSameProductV> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return (dateString == null || dateString.isEmpty())? criteriaBuilder.conjunction(): criteriaBuilder.lessThanOrEqualTo(
                    criteriaBuilder.function("DATE", Date.class, root.get("dateOrdered")),
                    Date.valueOf(localDate)
            );
        };
    }
    public static Specification<GetKolSameProductV> hasTenantId( ) {
        return (root, query, criteriaBuilder) ->   criteriaBuilder.equal(root.get("tenantId"),  AuditContext.getAuditInfo().getTenantId() );
    }

    public static Specification<GetKolSameProductV> warehouseId( Integer warehouseId, String role) {
        if( role.equals("KST"))
            return (root, query, criteriaBuilder) ->warehouseId == null ? criteriaBuilder.conjunction():criteriaBuilder.equal(root.get("warehouseId"), warehouseId );

        return (root, query, criteriaBuilder) ->criteriaBuilder.conjunction();
    }

    public static Specification<GetKolSameProductV> equalStatus(String status) {
        return (root, query, criteriaBuilder) -> status == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("orderLineStatus"),  status );
    }
    public static Specification<GetKolSameProductV> getEntity(GetKolSameProductVRequest request) {
        Specification<GetKolSameProductV> spec = Specification.where(null);
        spec = spec.and(notEqual(request.getId()));
        spec = spec.and(orgId(request.getOrgId()));
        spec = spec.and(notEqualProductId(request.getProductId()));
        spec = spec.and(equalStatus(request.getOrderLineStatus()));
        spec = spec.and(hasTenantId());
        spec=spec.and(equalKitchenOrderId(request.getKitchenOrderId()));
        spec=spec.and(equalDate(request.getCurrentDate()));
        spec = spec.and(greaterOrdered(request.getDateFrom()));
        spec = spec.and(lessOrdered(request.getDateTo()));
        spec= spec.and(equalProductId(request.getEqualProductId()));
        spec= spec.and(warehouseId(request.getWarehouseId(), request.getRole()));
        spec= spec.and(equalFloorId(request.getFloorId()));
        spec= spec.and(equalTableId(request.getTableId()));
        return spec;
    }
}
