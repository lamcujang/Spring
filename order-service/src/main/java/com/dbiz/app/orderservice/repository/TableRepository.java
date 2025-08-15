package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Integer>, JpaSpecificationExecutor<Table> {
    Page<Table> findAll(Specification specification, Pageable pageable);

    Page<Table> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM Table p WHERE p.id = :tableId")
    Optional<Table> findById(Integer tableId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Table p WHERE p.id = :tableId")
    void deleteById(Integer tableId);

    @Query("SELECT COUNT(p) FROM Table p WHERE p.floorId = :floorId")
    Integer countByFloorId(Integer floorId);

    @Query("SELECT p.name FROM Table p WHERE p.id = :tableId")
    String getNameTableById(Integer tableId);


    @Modifying
    @Transactional
    @Query("update Table p set p.tableStatus = :status where p.id = :tableId")
    void updateStatusById(Integer tableId, String status);

    @Query("SELECT coalesce(MAX(p.id),1000000) FROM Table p")
    Integer getMaxId();

    Table findByNameAndOrgIdAndFloorId(String name,Integer orgId,Integer floorId);

    Table findByTableNoAndOrgId(String tableNo,Integer orgId);

    Table findByErpTableId(Integer erpTableId);


    Boolean existsByFloorId(Integer floorId);

    @Query("SELECT p.name FROM Table p WHERE p.id = :id")//jpql
    String findNameById(Integer id);


}
