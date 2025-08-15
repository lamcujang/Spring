package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.HouseholdIndustry;
import com.dbiz.app.reportservice.domain.IndividualIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface HouseholdIndustryRepository extends JpaRepository<HouseholdIndustry, Integer>, JpaSpecificationExecutor<HouseholdIndustry> {

    List<HouseholdIndustry> findAllByTaxHouseholdProfileIdAndIsActive(Integer householdProfileId, String isActive);

    List<HouseholdIndustry> findAllByIsActive(String isActive);

    @Transactional
    @Modifying
    @Query(value = "update HouseholdIndustry a set a.isActive = 'N' where 1 = 1")
    void updateInactiveAllOldHouseholdIndustry();
}
