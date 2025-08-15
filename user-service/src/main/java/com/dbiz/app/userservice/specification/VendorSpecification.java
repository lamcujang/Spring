package com.dbiz.app.userservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.Vendor;
import org.common.dbiz.request.userRequest.VendorQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class VendorSpecification {
    public static Specification<Vendor> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }
    public static Specification<Vendor> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + keyword.toLowerCase() + "%");
    }


    public static Specification<Vendor> hasPhone1Equal(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone1"),  keyword );
    }

    public static Specification<Vendor> hasTaxCodeEqual(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taxCode"),keyword );
    }

    public static Specification<Vendor> hasEmailLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + keyword + "%");
    }
    public static Specification<Vendor> hasAddress1(String keyword){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("address1"), keyword);
    }
    public static Specification<Vendor> hasTenantId() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Vendor> hasDebitAmountFromEqual(BigDecimal debitAmountFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("debitAmount"),debitAmountFrom);
    }
    public static Specification<Vendor> hasDebitAmountToEqual( BigDecimal debitAmountTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("debitAmount"),debitAmountTo);
    }
    public static Specification<Vendor> hasAreaLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("area"), "%" + keyword + "%");
    }
    public static Specification<Vendor> hasPartnerGroup(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("partnerGroupId"),   keyword );
    }

    public static Specification<Vendor> getCustomerSpecification(VendorQueryRequest vendorQueryRequest) {
        Specification<Vendor> spec = Specification.where(null);
//        if(vendorQueryRequest.getName() != null){
//            spec = spec.and(hasNameLike(vendorQueryRequest
//                    .getName()));
//        }
//        if(vendorQueryRequest.getCode() != null){
//            spec= spec.and(hasCodeLike(vendorQueryRequest.getCode()));
//        }
//        if(vendorQueryRequest.getPhone1() != null){
//            spec = spec.and(hasPhone1Equal(vendorQueryRequest.getPhone1()));
//        }
//        if(vendorQueryRequest.getTaxCode() != null){
//            spec = spec.and(hasTaxCodeEqual(vendorQueryRequest.getTaxCode()));
//        }
//        if(vendorQueryRequest.getEmail() != null){
//            spec = spec.and(hasEmailLike(vendorQueryRequest.getEmail()));
//        }
        if(vendorQueryRequest.getKeyword() != null)
        {
            spec = spec.or(hasNameLike(vendorQueryRequest
                    .getKeyword()));
            spec = spec.or(hasCodeLike(vendorQueryRequest
                    .getKeyword()));
            spec = spec.or(hasPhone1Equal(vendorQueryRequest
                    .getKeyword()));
        }
        if(vendorQueryRequest.getArea()!= null)
        {
            spec = spec.and(hasAreaLike(vendorQueryRequest
                    .getArea()));
        }
        if(vendorQueryRequest.getAddress1()!=null){
            spec = spec.and(hasAddress1(vendorQueryRequest.getAddress1()));
        }
        if(vendorQueryRequest.getDebitAmountFrom() != null && vendorQueryRequest.getDebitAmountTo() != null)
        {
            spec = spec.and(hasDebitAmountFromEqual(vendorQueryRequest
                    .getDebitAmountFrom()));
            spec = spec.and(hasDebitAmountToEqual(vendorQueryRequest
                    .getDebitAmountTo()));
        }
        if(vendorQueryRequest.getPartnerGroupId()!=null)
        {
            spec = spec.and(hasPartnerGroup(vendorQueryRequest.getPartnerGroupId().toString()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
