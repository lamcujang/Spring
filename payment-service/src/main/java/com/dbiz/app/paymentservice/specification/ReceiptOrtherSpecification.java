package com.dbiz.app.paymentservice.specification;


import com.dbiz.app.paymentservice.domain.ReceiptOther;
import com.dbiz.app.paymentservice.domain.ReceiptOtherOrg;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.orderRequest.ReceiptOtherQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Subquery;

public class ReceiptOrtherSpecification {

    private static Specification<ReceiptOther> equalId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    private static Specification<ReceiptOther> likeName(String name) {
        return (root, query, criteriaBuilder) -> name == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<ReceiptOther> likeCode(String code) {
        return (root, query, criteriaBuilder) -> code == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + code.toLowerCase() + "%");
    }

    private static Specification<ReceiptOther> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }



//    public static Specification<ReceiptOther> orgId(Integer orgId) {
//        return (root, query, criteriaBuilder) -> {
//            if (orgId == null) {
//                return criteriaBuilder.conjunction(); // Nếu orgId là null, không áp dụng điều kiện lọc
//            }
//
//            // Thực hiện phép join từ ReceiptOther tới ReceiptOtherOrg thông qua trường 'id'
//            Join<ReceiptOther, ReceiptOtherOrg> receiptOtherOrgJoin = root.join("id", JoinType.INNER);
//
//            // So sánh orgId trong ReceiptOtherOrg với giá trị orgId được truyền vào
//            return criteriaBuilder.equal(receiptOtherOrgJoin.get("orgId"), orgId);
//        };
//    }

        public static Specification<ReceiptOther> orgId(Integer orgId) {
            return (root, query, criteriaBuilder) -> {
                if (orgId == null) {
                    return criteriaBuilder.conjunction();
                }

                Subquery<Integer> subquery = query.subquery(Integer.class);
                var receiptOtherOrg = subquery.from(ReceiptOtherOrg.class);
                subquery.select(receiptOtherOrg.get("receiptOtherId"))
                        .where(criteriaBuilder.equal(receiptOtherOrg.get("orgId"), orgId));

                return criteriaBuilder.in(root.get("id")).value(subquery);
            };
        }


    private static Specification<ReceiptOther> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isActive"), isActive);
    }


    public static Specification<ReceiptOther> getSpecification(ReceiptOtherQueryRequest request) {
        return Specification.where(equalId(request.getId()))
                .and(likeName(request.getKeyWord()))
                .and(likeCode(request.getKeyWord()))
                .and(tenantId(AuditContext.getAuditInfo().getTenantId()))
                .and(orgId(request.getOrgId()))
                .and(isActive(request.getIsActive()));
    }
}
