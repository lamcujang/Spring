package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxAgencyInfo;
import com.dbiz.app.reportservice.domain.TaxDeclarationIndividual;
import com.dbiz.app.reportservice.domain.TaxHouseholdProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TaxHouseholdProfileRepository extends JpaRepository<TaxHouseholdProfile, Integer>, JpaSpecificationExecutor<TaxHouseholdProfile> {
}
