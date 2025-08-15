 package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Product;
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
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer>, JpaSpecificationExecutor<Warehouse> {

    Page<Warehouse> findAll(Specification specification, Pageable pageable);

    Page<Warehouse> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM Warehouse p WHERE p.id = :warehouseId")
    Optional<Warehouse> findById(Integer warehouseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Warehouse p WHERE p.id = :warehouseId")
    void deleteById(Integer warehouseId);

    @Query("SELECT coalesce(MAX(p.id),1000000) FROM Warehouse p")
    Integer getMaxWarehouseId();

    Optional<Warehouse> findByErpWarehouseId(Integer erpWarehouseId);

    Optional<Warehouse> findByErpWarehouseName(String warehouseName);

    @Query("SELECT p FROM Warehouse p WHERE p.name = :name")
    Optional<Warehouse> findByName(String name);
}
