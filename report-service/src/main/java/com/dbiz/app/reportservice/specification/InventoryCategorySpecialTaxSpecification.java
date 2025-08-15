package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.InventoryCategorySpecialTax;
import org.common.dbiz.request.reportRequest.InventoryCategorySpecialTaxQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class InventoryCategorySpecialTaxSpecification {

    public static Specification<InventoryCategorySpecialTax> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<InventoryCategorySpecialTax> byItemCode(String itemCode) {
        return (root, query, criteriaBuilder) -> itemCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("code"), itemCode);
    }

    public static Specification<InventoryCategorySpecialTax> bySubsectionCode(String itemCode) {
        return (root, query, criteriaBuilder) -> itemCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("subsectionCode"), itemCode);
    }

    public static Specification<InventoryCategorySpecialTax> hasNameLike(String itemName) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + itemName.toLowerCase() + "%"
        );
    }

    public static Specification<InventoryCategorySpecialTax> hasSubsectionNameLike(String itemName) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("subsectionName")),
                "%" + itemName.toLowerCase() + "%"
        );
    }

    private static Specification<InventoryCategorySpecialTax> byGrade(Integer grade) {
        return (root, query, criteriaBuilder) -> grade == null ? null : criteriaBuilder.equal(root.get("grade"), grade);
    }

    private static Specification<InventoryCategorySpecialTax> byParentId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? null : criteriaBuilder.equal(root.get("parentId"), id);
    }

    public static Specification<InventoryCategorySpecialTax> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<InventoryCategorySpecialTax> hasIsParent(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isParent"), isActive);
    }

    public static Specification<InventoryCategorySpecialTax> getSpecification(InventoryCategorySpecialTaxQueryRequest request) {
        Specification<InventoryCategorySpecialTax> spec = Specification.where(null);

        if (request.getId() != null) {
            spec = spec.and(byId(request.getId()));
        }
        if (request.getCode() != null) {
            spec = spec.and(byItemCode(request.getCode()));
        }
        if (request.getParentId() != null) {
            spec = spec.and(byParentId(request.getParentId()));
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            spec = spec.and(hasNameLike(request.getName()));
        }
        if (request.getGrade() != null) {
            spec = spec.and(byGrade(request.getGrade()));
        }
        if (request.getIsActive() != null) {
            spec = spec.and(hasIsActive(request.getIsActive()));
        }
        if (request.getIsParent() != null) {
            spec = spec.and(hasIsParent(request.getIsParent()));
        }
        if (request.getSubsectionCode() != null) {
            spec = spec.and(bySubsectionCode(request.getSubsectionCode()));
        }
        if (request.getSubsectionName() != null && !request.getSubsectionName().isEmpty()) {
            spec = spec.and(hasSubsectionNameLike(request.getSubsectionName()));
        }

        return spec;
    }
}