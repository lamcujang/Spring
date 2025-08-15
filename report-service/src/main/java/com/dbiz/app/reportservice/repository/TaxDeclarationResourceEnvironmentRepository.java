package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxDeclarationResourceEnvironment;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaxDeclarationResourceEnvironmentRepository extends JpaRepository<TaxDeclarationResourceEnvironment, Integer>, JpaSpecificationExecutor<TaxDeclarationResourceEnvironment> {

    List<TaxDeclarationResourceEnvironment> findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
