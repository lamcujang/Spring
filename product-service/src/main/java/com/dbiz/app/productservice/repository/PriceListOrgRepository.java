package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.PriceListOrg;
import com.dbiz.app.productservice.domain.PriceListProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PriceListOrgRepository extends JpaRepository<PriceListOrg, Integer>, JpaSpecificationExecutor<PriceListOrg> {

    Page<PriceListOrg> findAll(Specification specification, Pageable pageable);

    Page<PriceListOrg> findAllByTenantId(Pageable pageable, Integer tenantId);

    @Query("SELECT p FROM PriceListOrg p WHERE p.id = :pricelistOrgId")
    Optional<PriceListOrg> findById(Integer pricelistOrgId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PriceListOrg p WHERE p.id = :pricelistOrgId")
    void deleteById(Integer pricelistOrgId);

    List<PriceListOrg>findAllByPricelistId(Integer pricelistId);

    @Query("SELECT p  FROM PriceListOrg p WHERE p.orgId in  (:orgId) and p.tenantId = :tenantId and p.isActive = 'Y'")
    Page<PriceListOrg>findAllByOrgId(@Param("orgId") Integer[] orgId,@Param("tenantId") Integer tenantId,Pageable pageable);

    @Query("SELECT p.pricelistId  FROM PriceListOrg p WHERE  p.tenantId = :tenantId and p.isAll = 'Y'")
    List<Integer>findAllIsAll(@Param("tenantId") Integer tenantId);


    void deleteAllByPricelistId(Integer pricelistId);

    Optional<PriceListOrg> findByOrgIdAndPricelistId(Integer orgId, Integer pricelistId);

    @Modifying
    @Transactional
    @Query("UPDATE PriceListOrg p SET p.isActive = :isActive WHERE p.pricelistId = :pricelistId")
    void updateIsActiveByPricelistId(Integer pricelistId, String isActive);



}