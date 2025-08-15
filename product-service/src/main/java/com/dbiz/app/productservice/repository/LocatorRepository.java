package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Locator;
import com.dbiz.app.productservice.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocatorRepository extends JpaRepository<Locator, Integer>, JpaSpecificationExecutor<Locator> {

    Page<Locator> findAll(Specification specification, Pageable pageable);

    Page<Locator> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM Locator p WHERE p.id = :locatorId")
    Optional<Locator> findById(Integer locatorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Locator p WHERE p.id = :warehouseId")
    void deleteById(Integer warehouseId);

    @Query("SELECT coalesce(MAX(p.id),1000000) FROM Locator p")
    Integer getMaxLocatorId();


    List<Locator> findAllByWarehouseId(Integer warehouseId);

    Optional<Locator> findByWarehouseIdAndIsDefault(Integer warehouseId, String isDefault);
}
