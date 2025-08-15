package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.NoteGroup;
import org.common.dbiz.request.productRequest.NoteGroupQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class NoteGroupSpecification {

    public static Specification<NoteGroup> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("groupName"), "%" + keyword + "%");
    }
    public static Specification<NoteGroup> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<NoteGroup> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<NoteGroup> getNoteGroupSpecification(NoteGroupQueryRequest NoteGroupQueryRequest) {
        Specification<NoteGroup> spec = Specification.where(null);

        if(NoteGroupQueryRequest.getGroupName() != null){
            spec= spec.and(hasNameLike(NoteGroupQueryRequest.getGroupName()));
        }
        if(NoteGroupQueryRequest.getOrgId()!= null  && NoteGroupQueryRequest.getOrgId()!= 0 )
        {
            spec= spec.and(hasOrgId(NoteGroupQueryRequest.getOrgId()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
