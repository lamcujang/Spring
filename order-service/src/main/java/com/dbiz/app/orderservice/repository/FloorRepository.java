package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.Floor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface FloorRepository extends JpaRepository<Floor, Integer>, JpaSpecificationExecutor<Floor> {

    Page<Floor> findAll(Specification specification, Pageable pageable);

    Page<Floor> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM Floor p WHERE p.id = :floorId")
    Optional<Floor> findById(Integer floorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Floor p WHERE p.id = :floorId")
    void deleteById(Integer floorId);

    @Query("SELECT p.name FROM Floor p WHERE p.id = :floorId")//jpql
    String getNameFloorById(Integer floorId);

    @Query("SELECT MAX(p.id) FROM Floor p")
    Integer getMaxId();

    Floor findByNameAndOrgId(String name,Integer orgId);

    Floor findByFloorNoAndOrgId(String floorNo,Integer orgId);

    Floor findByErpFloorId(Integer floorId);


}