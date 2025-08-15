package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.IndividualIndustry;
import com.dbiz.app.reportservice.domain.TaxDeclarationIndividual;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import org.common.dbiz.dto.reportDto.response.TaxDeclarationVatPitLineInitDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface TaxDeclarationVatPitLineRepository extends JpaRepository<TaxDeclarationVatPitLine, Integer>, JpaSpecificationExecutor<TaxDeclarationVatPitLine> {

    List<TaxDeclarationVatPitLine> findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
