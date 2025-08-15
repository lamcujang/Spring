package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.OrderLine;
import org.common.dbiz.request.orderRequest.OrderLineQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class OrderLineSpecification {

    public static Specification<OrderLine> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<OrderLine> byTenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    public static Specification<OrderLine> byOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<OrderLine> byOrderId(Integer orderId) {
        return (root, query, criteriaBuilder) -> orderId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orderId"), orderId);
    }

    public static Specification<OrderLine> byProductId(Integer productId) {
        return (root, query, criteriaBuilder) -> productId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("productId"), productId);
    }

    public static Specification<OrderLine> byQuantity(BigDecimal quantity) {
        return (root, query, criteriaBuilder) -> quantity == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("qty"), quantity);
    }

    public static Specification<OrderLine> byTaxId(Integer taxId) {
        return (root, query, criteriaBuilder) -> taxId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("taxId"), taxId);
    }

    public static Specification<OrderLine> byPrice(BigDecimal price) {
        return (root, query, criteriaBuilder) -> price == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("priceEntered"), price);
    }

    private static Specification<OrderLine> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }


    public static Specification<OrderLine> getSpecification(OrderLineQueryRequest request) {
        return Specification.where(byId(request.getId()))
                .and(byTenantId(AuditContext.getAuditInfo().getTenantId()))
                .and(byOrgId(request.getOrgId()))
                .and(isActive(request.getIsActive()))
                .and(byOrderId(request.getOrderId()))
                .and(byProductId(request.getProductId()))
                .and(byQuantity(request.getQuantity()))
                .and(byTaxId(request.getTaxId()))
                .and(byPrice(request.getPrice()));
    }
}
