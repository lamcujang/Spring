package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.IndividualIndustry;
import com.dbiz.app.reportservice.domain.TaxDeclarationExcise;
import com.dbiz.app.reportservice.domain.TaxDeclarationResourceEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IndividualIndustryRepository extends JpaRepository<IndividualIndustry, Integer>, JpaSpecificationExecutor<IndividualIndustry> {

    List<IndividualIndustry> findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
