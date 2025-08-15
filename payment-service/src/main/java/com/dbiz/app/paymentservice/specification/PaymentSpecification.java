package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.paymentservice.domain.Payment;
import com.dbiz.app.paymentservice.helper.DateHelper;
import org.common.dbiz.request.paymentRequest.PaymentQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PaymentSpecification {

    public static Specification<Payment> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Payment> byCustomerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> customerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("customerId"), customerId);
    }

    public static Specification<Payment> byVendorId(Integer vendorId) {
        return (root, query, criteriaBuilder) -> vendorId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("vendorId"), vendorId);
    }

    public static Specification<Payment> byInvoiceId(Integer invoiceId) {
        return (root, query, criteriaBuilder) -> invoiceId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceId"), invoiceId);
    }

    public static Specification<Payment> byBankAccountId(Integer bankAccountId) {
        return (root, query, criteriaBuilder) -> bankAccountId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("bankAccountId"), bankAccountId);
    }

    public static Specification<Payment> byPaymentDate(String paymentDate) {
        return (root, query, criteriaBuilder) -> paymentDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("paymentDate"), DateHelper.toLocalDate(paymentDate));
    }

    public static Specification<Payment> byPaymentStatus(String paymentStatus) {
        return (root, query, criteriaBuilder) -> paymentStatus == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus);
    }

    public static Specification<Payment> byPaymentAmount(BigDecimal paymentAmount) {
        return (root, query, criteriaBuilder) -> paymentAmount == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("paymentAmount"), paymentAmount);
    }

    public static Specification<Payment> byOrderId(Integer orderId) {
        return (root, query, criteriaBuilder) -> orderId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orderId"), orderId);
    }

    private static Specification<Payment> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<Payment> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }
    public static Specification<Payment> getSpecification(PaymentQueryRequest request) {
        return Specification.where(byId(request.getId()))
                .and(tenantId(AuditContext.getAuditInfo().getTenantId()))
                .and(orgId(request.getOrgId()))
                .and(byCustomerId(request.getCustomerId()))
                .and(byVendorId(request.getVendorId()))
                .and(byInvoiceId(request.getInvoiceId()))
                .and(byBankAccountId(request.getBankAccountId()))
                .and(byPaymentDate(request.getPaymentDate()))
                .and(byPaymentStatus(request.getPaymentStatus()))
                .and(byPaymentAmount(request.getPaymentAmount()))
                .and(byOrderId(request.getOrderId()));
    }
}