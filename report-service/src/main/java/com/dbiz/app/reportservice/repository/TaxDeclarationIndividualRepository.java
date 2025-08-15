package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.EnvironmentFee;
import com.dbiz.app.reportservice.domain.TaxDeclarationIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaxDeclarationIndividualRepository extends JpaRepository<TaxDeclarationIndividual, Integer>, JpaSpecificationExecutor<TaxDeclarationIndividual> {
}
