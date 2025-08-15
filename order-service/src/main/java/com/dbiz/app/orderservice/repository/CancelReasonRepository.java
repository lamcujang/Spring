package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.CancelReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CancelReasonRepository extends JpaRepository<CancelReason, Integer>, JpaSpecificationExecutor<CancelReason> {

    Page<CancelReason> findAll(Specification specification, Pageable pageable);

    Page<CancelReason> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM CancelReason p WHERE p.id = :dCancelReasonId")
    Optional<CancelReason> findById(Integer dCancelReasonId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CancelReason p WHERE p.id = :dCancelReasonId")
    void deleteById(Integer dCancelReasonId);
}