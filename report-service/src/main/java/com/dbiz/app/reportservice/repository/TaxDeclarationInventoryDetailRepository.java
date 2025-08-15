package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxDeclarationInventoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxDeclarationInventoryDetailRepository extends JpaRepository<TaxDeclarationInventoryDetail, Integer> {
    List<TaxDeclarationInventoryDetail> findByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}