package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.PriceList;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PriceListRepository extends JpaRepository<PriceList, Integer>, JpaSpecificationExecutor<PriceList> {

    Page<PriceList> findAll(Specification specification, Pageable pageable);

    Page<PriceList> findAllByTenantId(Pageable pageable, Integer tenantId);

    @Query("SELECT p FROM PriceList p WHERE p.id = :pricelistId")
    Optional<PriceList> findById(Integer pricelistId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PriceList p WHERE p.id = :pricelistId")
    void deleteById(Integer pricelistId);

    @Query("SELECT p.id FROM PriceList p WHERE p.generalPriceList = :generalPriceList AND p.tenantId = :tenantId AND p.orgId = :orgId")
    Integer findPriceListByGeneralPriceList(@Param("generalPriceList") String generalPriceList,
                                            @Param("tenantId") Integer tenantId,
                                            @Param("orgId") Integer orgId);

    Integer findPriceListByGeneralPriceList(String generalPriceList);
    @Query("SELECT p FROM PriceList p WHERE p.generalPriceList = 'Y' AND p.tenantId = :tenantId AND p.id = :id")
    PriceList findPriceListByGeneralPriceList(@Param("tenantId") Integer tenantId,
                                            @Param("id") Integer id);


    PriceList findByErpPriceListId(Integer priceErpId);

    PriceList findByNameAndTenantId(String name, Integer tenantId);

    @Query("select max(p.fromDate) from PriceList  p where p.tenantId = :tenantId and p.generalPriceList = 'N'")
    Instant getMaxFromDate(Integer tenantId);

    @Query("SELECT p FROM PriceList p " +
            "WHERE p.generalPriceList = :generalPriceList")
    List<PriceList> findByGeneralPriceList(@Param("generalPriceList") String generalPriceList);


    @Query("SELECT p FROM PriceList p WHERE p.name = :name AND p.code = :code")
    PriceList findByNameAndAndCode(String name, String code);

    PriceList findByName(String name);
}
