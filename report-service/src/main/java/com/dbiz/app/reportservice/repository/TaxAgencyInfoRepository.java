package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.TaxAgencyInfo;
import com.dbiz.app.reportservice.domain.TaxBusinessIndustry;
import com.dbiz.app.reportservice.domain.TaxHouseholdProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public interface TaxAgencyInfoRepository extends JpaRepository<TaxAgencyInfo, Integer>, JpaSpecificationExecutor<TaxAgencyInfo> {

    @Transactional
    @Modifying
    @Query(value = "update TaxAgencyInfo a set a.isActive = ?1 where 1 = 1")
    void updateInactiveAllOldTaxAgencyInfo(String isActive);

    @Query(value = "SELECT * FROM d_tax_agency_info WHERE is_active = 'Y' ORDER BY created DESC LIMIT 1", nativeQuery = true)
    Optional<TaxAgencyInfo> findLatestActiveNative();
}
