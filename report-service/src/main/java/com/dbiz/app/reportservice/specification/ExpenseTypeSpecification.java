package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.ExpenseType;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.dto.reportDto.request.ExpenseTypeRequest;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;
import org.springframework.data.jpa.domain.Specification;

public class ExpenseTypeSpecification {
    public static Specification<ExpenseType> isId(Integer id)
    {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("id"), id );
    }

    public static Specification<ExpenseType> isActive(String isActive)
    {
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("isActive"), isActive );
    }

    public static Specification<ExpenseType> getEntitySpecification(ExpenseTypeRequest req) {
        Specification<ExpenseType> spec = Specification.where(null);
        spec = spec.and(isId(req.getId()));
        spec = spec.and(isActive("Y"));
        return spec;
    }
}
