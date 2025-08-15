package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxBusinessIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaxBusinessIndustryRepository extends JpaRepository<TaxBusinessIndustry, Integer>, JpaSpecificationExecutor<TaxBusinessIndustry> {

    TaxBusinessIndustry findByIndustryCode(String code);

    List<TaxBusinessIndustry> findByIndustryName(String name);

}
