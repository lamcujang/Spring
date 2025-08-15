package com.dbiz.app.productservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.domain.view.OrgPriceListV;
import org.springframework.data.jpa.domain.Specification;

public class OrgPriceListVSpecification {

    public static Specification<OrgPriceListV> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }
    public static Specification<OrgPriceListV> hasCode(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<OrgPriceListV> hasId(Integer keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        return (root, query, criteriaBuilder) ->criteriaBuilder.equal(root.get("id"), keyword);
    }

    public static Specification<OrgPriceListV> hasIsAll(Integer keyword) {
        return (root, query, criteriaBuilder) ->criteriaBuilder.equal(root.get("isAll"), keyword);
    }
    public static Specification<PriceList> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

}
