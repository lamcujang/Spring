package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.Order;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    private static Specification<Order> id(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    private static Specification<Order> documentNo(String documentNo) {
        return (root, query, criteriaBuilder) -> documentNo == null ? null : criteriaBuilder.like(root.get("documentNo"), "%"+documentNo+"%");
    }

    private static Specification<Order> customerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> customerId == null ? null : criteriaBuilder.equal(root.get("customerId"), customerId);
    }

    private static Specification<Order> orderDate(String orderDate) {
        return (root, query, criteriaBuilder) -> orderDate == null ? null : criteriaBuilder.equal(root.get("orderDate"),   DateHelper.toLocalDate(orderDate));
    }

    private static Specification<Order> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    private static Specification<Order> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<Order> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    private static Specification<Order> floorId(Integer floorId) {
        return (root, query, criteriaBuilder) -> floorId == null ? null : criteriaBuilder.equal(root.get("floorId"), floorId);
    }

    private static Specification<Order> tableId(Integer tableId) {
        return (root, query, criteriaBuilder) -> tableId == null ? null : criteriaBuilder.equal(root.get("tableId"), tableId);
    }

    public static Specification<Order> getSpecification(OrderQueryRequest request) {
        return Specification.where(id(request.getId()))
                .and(tenantId(AuditContext.getAuditInfo().getTenantId())
                .and(orgId(request.getOrgId()))
                .and(documentNo(request.getDocumentNo()))
                .and(customerId(request.getCustomerId()))
                .and(orderDate(request.getOrderDate()))
                .and(isActive(request.getIsActive()))
                .and(orgId(request.getOrgId()))
                .and(floorId(request.getFloorId()))
                .and(tableId(request.getTableId())));
    }
}
