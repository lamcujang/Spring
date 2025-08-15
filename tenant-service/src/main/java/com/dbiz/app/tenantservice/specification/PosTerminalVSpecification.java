package com.dbiz.app.tenantservice.specification;


import com.dbiz.app.tenantservice.domain.view.PosTerminalV;
import org.common.dbiz.request.tenantRequest.PosTerminalQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class PosTerminalVSpecification {

    public static Specification<PosTerminalV> getSpecification(PosTerminalQueryRequest posTerminalRequest) {
        return new Specification<PosTerminalV>() {
            @Override
            public Predicate toPredicate(Root<PosTerminalV> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (posTerminalRequest.getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), posTerminalRequest.getId()));
                }
                if (posTerminalRequest.getTenantId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), posTerminalRequest.getTenantId()));
                }
                if (posTerminalRequest.getOrgId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("orgId"), posTerminalRequest.getOrgId()));
                }
                if(posTerminalRequest.getIsActive() != null){
                    predicates.add(criteriaBuilder.equal(root.get("isActive"), posTerminalRequest.getIsActive()));
                }
                if(posTerminalRequest.getKeyword() != null){
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + posTerminalRequest.getKeyword().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("fullName")), "%" + posTerminalRequest.getKeyword().toLowerCase() + "%")
                    ));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
