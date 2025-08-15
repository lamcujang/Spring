package com.dbiz.app.productservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.Note;
import org.common.dbiz.request.productRequest.NoteQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class NoteSpecification {

    public static Specification<Note> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
    public static Specification<Note> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Note> hasProductCategoryId(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productCategoryIds"), "%" + keyword + "%");
    }
    public static Specification<Note> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<Note> hasNoteGroupId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("noteGroupId"), keyword);
    }

    public static Specification<Note> getNoteSpecification(NoteQueryRequest NoteQueryRequest) {
        Specification<Note> spec = Specification.where(null);

        if(NoteQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(NoteQueryRequest.getName()));
        }
        if(NoteQueryRequest.getOrgId()!= null && NoteQueryRequest.getOrgId()!= 0 )
        {
            spec= spec.and(hasOrgId(NoteQueryRequest.getOrgId()));
        }
        if(NoteQueryRequest.getNoteGroupId()!= null)
        {
            spec= spec.and(hasNoteGroupId(NoteQueryRequest.getNoteGroupId()));
        }
        if(NoteQueryRequest.getProductCategoryId()!= null)
        {
            spec= spec.and(hasProductCategoryId(NoteQueryRequest.getProductCategoryId().toString()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
