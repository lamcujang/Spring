package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.Table;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TableSpecification {

    public static Specification<Table> hasTableLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("tableNo")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<Table> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<Table> hasTableStatusEqua(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableStatus"), "%" + keyword + "%");
    }

    public static Specification<Table> equaFloorId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("floorId"), keyword );
    }
    public static Specification<Table> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<Table> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }

    public static Specification<Table> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }


    public static Specification<Table>hasFloorByPosterminalId(Integer posterminalId)
    {
        return (root, query, criteriaBuilder) -> posterminalId==null? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("floor").get("posTerminalId"), posterminalId);
    }
    public static Specification<Table> getTableSpecification(TableQueryRequest  tableQueryRequest) {
        Specification<Table> spec = Specification.where(null);

        if(tableQueryRequest.getTableNo() != null){
            spec= spec.and(hasTableLike(tableQueryRequest.getTableNo()));
        }
        if(tableQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(tableQueryRequest.getName()));
        }
        if(tableQueryRequest.getTableStatus()!= null){
            spec= spec.and(hasTableStatusEqua(tableQueryRequest.getTableStatus()));
        }
        if(tableQueryRequest.getOrgId()!= null)
        {
            spec= spec.and(hasOrgId(tableQueryRequest.getOrgId()));

        }
        if(tableQueryRequest.getFloorId()!=null)
        {
            spec= spec.and(equaFloorId(tableQueryRequest.getFloorId()));
        }
        if(tableQueryRequest.getIsActive() != null)
        {
            spec= spec.and(equaIsActive(tableQueryRequest.getIsActive()));
        }
        spec = spec.and(hasFloorByPosterminalId(tableQueryRequest.getPosTerminalId()));
        spec = spec.and(hasTenantId());
        return spec;
    }
}
