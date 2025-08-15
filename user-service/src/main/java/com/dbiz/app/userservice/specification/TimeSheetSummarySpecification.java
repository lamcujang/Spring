package com.dbiz.app.userservice.specification;

import com.dbiz.app.userservice.domain.TimeSheetSummary;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TimeSheetSummarySpecification {
    private static Specification<TimeSheetSummary> isId(Integer id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }


    public static Specification<TimeSheetSummary> getSpecification(TimeSheetSummaryRequest request) {
        return Specification.where(isId(request.getId())
//                .and(orgId(request.getOrgId()))
        );
    }
}
