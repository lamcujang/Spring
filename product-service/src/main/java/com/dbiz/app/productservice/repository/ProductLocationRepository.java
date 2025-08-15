package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.ProductLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductLocationRepository extends JpaRepository<ProductLocation, Integer>, JpaSpecificationExecutor<ProductLocation> {
    @Transactional
    @Modifying
    @Query("delete from ProductLocation pl where pl.productId = :productId")
    void deleteAllByProductId(Integer productId);

    @Transactional
    @Modifying
    @Query("update ProductLocation pl set pl.isActive = 'N' where pl.productId = :productId")
    void updateIsActiveAllByProductId(Integer productId);

    @Transactional
    @Modifying
    @Query("update ProductLocation pl set pl.locatorId = :locatorId where pl.warehouseId = :warehouseId")
    void updateAllByWarehouseId(Integer warehouseId, Integer locatorId);


    Optional<ProductLocation> findByProductIdAndOrgId(Integer productId, Integer orgId);


    Optional<ProductLocation> findByProductIdAndWarehouseIdAndOrgId(Integer productId, Integer locationId,Integer orgId);

    Optional<ProductLocation>findByProductIdAndWarehouseId(Integer productId, Integer locationId);

    Optional<ProductLocation> findByProductIdAndWarehouseIdAndPosTerminalId(Integer productId, Integer warehouseId, Integer posTerminalId);

    Optional<ProductLocation> findByProductIdAndWarehouseIdAndPosTerminalIdAndOrgId(Integer productId, Integer warehouseId, Integer posTerminalId,Integer orgId);

    Optional<ProductLocation> findByProductIdAndWarehouseIdAndLocatorIdAndOrgId(Integer productId, Integer warehouseId, Integer posTerminalId,Integer orgId);
    Optional<ProductLocation> findByProductIdAndWarehouseIdAndIsActiveAndIsDefault(Integer productId, Integer locationId,String isActive, String isDefault);

    @Query("SELECT COUNT(dpl) FROM ProductLocation dpl WHERE dpl.productId = :productId AND dpl.isDefault = 'Y'")
    Long countDefaultLocationByProductId(@Param("productId") Integer productId);

}