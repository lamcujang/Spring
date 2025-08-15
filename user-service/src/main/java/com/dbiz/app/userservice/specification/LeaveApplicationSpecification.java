package com.dbiz.app.userservice.specification;

import com.dbiz.app.userservice.domain.LeaveApplication;
import org.common.dbiz.dto.userDto.request.LeaveApplicationRequest;
import org.springframework.data.jpa.domain.Specification;

public class LeaveApplicationSpecification {
    private static Specification<LeaveApplication> isId(Integer id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }


    public static Specification<LeaveApplication> getSpecification(LeaveApplicationRequest request) {
        return Specification.where(isId(request.getId())
//                .and(orgId(request.getOrgId()))
                    );
    }
}
