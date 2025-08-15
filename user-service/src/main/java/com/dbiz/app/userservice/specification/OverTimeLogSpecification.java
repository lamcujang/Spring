package com.dbiz.app.userservice.specification;

import com.dbiz.app.userservice.domain.OverTimeLog;
import org.common.dbiz.dto.userDto.request.OverTimeLogRequest;
import org.springframework.data.jpa.domain.Specification;

public class OverTimeLogSpecification {
    private static Specification<OverTimeLog> isId(Integer id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }


    public static Specification<OverTimeLog> getSpecification(OverTimeLogRequest request) {
        return Specification.where(isId(request.getId())
//                .and(orgId(request.getOrgId()))
        );
    }
}
