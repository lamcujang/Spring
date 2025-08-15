package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxDeclarationExcise;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaxDeclarationExciseRepository extends JpaRepository<TaxDeclarationExcise, Integer>, JpaSpecificationExecutor<TaxDeclarationExcise> {

    List<TaxDeclarationExcise> findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
