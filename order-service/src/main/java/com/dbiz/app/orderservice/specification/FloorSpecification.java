package com.dbiz.app.orderservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.Floor;
import org.common.dbiz.request.orderRequest.FloorQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class FloorSpecification {
    public static Specification<Floor> hasFlooNoLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("floorNo")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<Floor> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<Floor> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<Floor> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Floor> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }

    public static   Specification<Floor>equalPosTerminalId(Integer keyword) {
//        return (root, query, criteriaBuilder) ->keyword == null ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("posTerminalId"), keyword);

        return (root, query, criteriaBuilder) -> {

            if (keyword == null) {

                return criteriaBuilder.conjunction();  // Return all if posterminalId is null
            }

            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Floor> floorRoot = subquery.from(Floor.class);
            subquery.select(floorRoot.get("id"))
                    .where(criteriaBuilder.equal(
                            criteriaBuilder.coalesce(floorRoot.get("posTerminalId"), keyword),
                            keyword
                    ));
            return criteriaBuilder.in(root.get("id")).value(subquery);
        };

    }

    public static Specification<Floor> getFloorSpecification(FloorQueryRequest floorQueryRequest) {
        Specification<Floor> spec = Specification.where(null);

        if(floorQueryRequest.getKeyword() != null){
            spec = spec.or(hasFlooNoLike(floorQueryRequest.getKeyword()));
            spec = spec.or(hasNameLike(floorQueryRequest.getKeyword()));
        }
        if(floorQueryRequest.getFloorNo() != null){
            spec= spec.and(hasFlooNoLike(floorQueryRequest.getFloorNo()));
        }
        if(floorQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(floorQueryRequest.getName()));
        }
//        if(floorQueryRequest.getOrgId()!= null && floorQueryRequest.getOrgId()!=0) //
//        {
//            spec= spec.and(hasOrgId(floorQueryRequest.getOrgId()));
//
//        }
        if(floorQueryRequest.getOrgId()!= null) //
        {
            spec= spec.and(hasOrgId(floorQueryRequest.getOrgId()));

        }
        if(floorQueryRequest.getIsActive()!= null)
        {
            spec= spec.and(equaIsActive(floorQueryRequest.getIsActive()));

        }
//        spec = spec.and(equalPosTerminalId(floorQueryRequest.getPosTerminalId()));
        spec = spec.and(hasTenantId());
        return spec;
    }
}
