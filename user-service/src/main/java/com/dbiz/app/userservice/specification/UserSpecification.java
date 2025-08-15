package com.dbiz.app.userservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.User;
import org.common.dbiz.request.userRequest.UserQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->keyword == null ? criteriaBuilder.conjunction():  criteriaBuilder.like(
                criteriaBuilder.lower(root.get("fullName")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<User> hasPhoneLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->keyword == null ? criteriaBuilder.conjunction():  criteriaBuilder.like(
                root.get("phone"),
                "%" + keyword + "%"
        );
    }

    public static Specification<User> hasUsername(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->keyword == null ? criteriaBuilder.conjunction(): criteriaBuilder.like(
                criteriaBuilder.lower(root.get("userName")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<User> hasIsActive(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"),keyword);
    }
    public static Specification<User> hasTenantId() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<User> hasId(Integer userId) {
        return (root, query, criteriaBuilder) -> userId ==null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<User> getEntitySpecification(UserQueryRequest queryRequest) {
        Specification<User> spec = Specification.where(null);
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

        spec = spec.and(hasTenantId());
            spec = spec.and(hasNameLike(queryRequest.getKeyword()));
            spec = spec.or(hasUsername(queryRequest.getKeyword()));
            spec = spec.or(hasPhoneLike(queryRequest.getKeyword()));
        if(queryRequest.getIsActive()!=null)
            spec = spec.and(hasIsActive(queryRequest.getIsActive()));
        spec = spec.and(hasId(queryRequest.getUserId()));
        spec= spec.and(hasTenantId());
        return spec;
    }
}
