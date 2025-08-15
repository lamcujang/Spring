package com.dbiz.app.userservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.Customer;
import org.common.dbiz.request.userRequest.CustomerQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class CustomerSpecification {
    public static Specification<Customer> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(  criteriaBuilder.lower(root.get("name")),"%" + keyword.toLowerCase() + "%"
        );
    }

//    public static Specification<Customer> hasNameOrEmailLike(String keyword) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            // Thêm điều kiện kiểm tra name
//            if (keyword != null && !keyword.isEmpty()) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"));
//            }
//
//            // Kết hợp các điều kiện bằng OR
//            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
//        };
//    }
    public static Specification<Customer> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Customer>equalCode(String keyword)
    {
        return (root, query, criteriaBuilder) -> keyword == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("code"), keyword );
    }

    public static Specification<Customer> hasPhone1Equal(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("phone1"), "%" + keyword + "%" );
    }

    public static Specification<Customer> equalIsActive(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword );
    }

    public static Specification<Customer> hasAreaLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("area")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Customer> hasPartnerGroupIdEqual(int keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("partnerGroupId"), keyword );
    }

    public static Specification<Customer> hasDebitAmountFromEqual(BigDecimal debitAmountFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("debitAmount"),debitAmountFrom);
    }
    public static Specification<Customer> hasDebitAmountToEqual( BigDecimal debitAmountTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("debitAmount"),debitAmountTo);
    }


    public static Specification<Customer> hasTaxCodeEqual(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taxCode"),   keyword );
    }

    public static Specification<Customer> hasEmailLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + keyword + "%");
    }

    public static Specification<Customer> hasTenantId() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Customer> getCustomerSpecification(CustomerQueryRequest customerQueryRequest) {
        Specification<Customer> spec = Specification.where(null);
        spec = spec.and(equalCode(customerQueryRequest.getCode()));
        spec = spec.and(hasTenantId());

        if(customerQueryRequest.getIsActive() != null)
        {
            spec = spec.and(equalIsActive(customerQueryRequest
                    .getIsActive()));
        }
        if (customerQueryRequest.getArea() != null) {
            spec = spec.and(hasAreaLike(customerQueryRequest
                    .getArea()));
        }
        if(customerQueryRequest.getDebitAmountFrom() != null && customerQueryRequest.getDebitAmountTo() != null)
        {
            spec = spec.and(hasDebitAmountFromEqual(customerQueryRequest
                    .getDebitAmountFrom()));
            spec = spec.and(hasDebitAmountToEqual(customerQueryRequest
                    .getDebitAmountTo()));
        }
        if(customerQueryRequest.getPartnerGroupId() !=null)
        {
            spec = spec.and(hasPartnerGroupIdEqual(customerQueryRequest
                    .getPartnerGroupId()));
        }

        if(customerQueryRequest.getKeyword() != null){
            spec = spec.and(hasNameLike(customerQueryRequest
                    .getKeyword()).or(hasCodeLike(customerQueryRequest
                    .getKeyword()).or(hasPhone1Equal(customerQueryRequest
                    .getKeyword()))));

        }

        return spec;
    }
}
