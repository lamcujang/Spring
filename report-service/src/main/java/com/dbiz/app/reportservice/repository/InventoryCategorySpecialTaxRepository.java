package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.InventoryCategorySpecialTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface InventoryCategorySpecialTaxRepository extends JpaRepository<InventoryCategorySpecialTax, Integer>, JpaSpecificationExecutor<InventoryCategorySpecialTax> {

    List<InventoryCategorySpecialTax> findBySubsectionCode(BigDecimal id);

    List<InventoryCategorySpecialTax> findAllByParentId(Integer parentId);

    List<InventoryCategorySpecialTax> findAllByGrade(Integer grade);

    InventoryCategorySpecialTax findByCode(String code);

    List<InventoryCategorySpecialTax> findByName(String name);

}
