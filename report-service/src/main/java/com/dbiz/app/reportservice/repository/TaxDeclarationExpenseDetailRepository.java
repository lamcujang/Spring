package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxDeclarationExpenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxDeclarationExpenseDetailRepository extends JpaRepository<TaxDeclarationExpenseDetail, Integer> {
    List<TaxDeclarationExpenseDetail> findByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}