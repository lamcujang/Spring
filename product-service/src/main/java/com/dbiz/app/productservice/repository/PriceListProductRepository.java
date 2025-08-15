package com.dbiz.app.productservice.repository;

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

public interface PriceListProductRepository extends JpaRepository<PriceListProduct, Integer>, JpaSpecificationExecutor<PriceListProduct> {

    Page<PriceListProduct> findAll(Specification specification, Pageable pageable);

    Page<PriceListProduct> findAllByTenantId(Pageable pageable, Integer tenantId);

    @Query("SELECT p FROM PriceListProduct p WHERE p.id = :pricelistId")
    Optional<PriceListProduct> findById(Integer pricelistId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PriceListProduct p WHERE p.id = :pricelistId")
    void deleteById(Integer pricelistId);

    @Query("SELECT p FROM PriceListProduct p WHERE p.priceListId = :priceList AND p.productId = :productId")
    Optional<PriceListProduct> findByPriceListAndProductId(Integer priceList, Integer productId);


    @Query("SELECT p FROM PriceListProduct p WHERE p.priceListId = :priceListId ")
    List<PriceListProduct> findAllByPriceListId(Integer priceListId);


    @Query("SELECT p FROM PriceListProduct p join PriceList pl on p.priceListId = pl.id WHERE p.productId = :productId AND pl.generalPriceList = 'Y' AND pl.tenantId = :tenantId")
    Optional<PriceListProduct> findByProductIdAndTenantId(@Param("productId") Integer productId, @Param("tenantId") Integer tenantId);

    @Query("SELECT p FROM PriceListProduct p where p.priceListId <> :priceListId AND p.productId = :productId")
    List<PriceListProduct> findAllNotInGeneralPriceList(Integer priceListId, Integer productId);

    @Query("select p  from PriceListProduct p join PriceList pl on p.priceListId = pl.id " +
            " where p.productId = :productId AND pl.generalPriceList = 'Y' AND pl.tenantId = :tenantId")
    PriceListProduct findAllPriceListProductByGeneralPriceListAndProductId(Integer productId, Integer tenantId);

    @Query("select p  from  PriceListProduct p join PriceList pl on p.priceListId = pl.id " +
            " where p.productId = :productId AND pl.generalPriceList = 'Y' AND pl.tenantId = :tenantId")
    Optional<PriceListProduct> findPriceListProductByDefault(Integer productId, Integer tenantId);


    @Query("SELECT p FROM PriceListProduct p JOIN PriceList pl ON p.priceListId = pl.id " +
            "WHERE p.productId = :productId AND pl.generalPriceList = :generalPriceList")
    List<PriceListProduct> findAllByProductIdAndGeneralPriceList(@Param("productId") Integer productId, @Param("generalPriceList") String generalPriceList);
}