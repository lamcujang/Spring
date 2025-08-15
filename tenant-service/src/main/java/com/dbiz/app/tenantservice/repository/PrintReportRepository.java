package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.PrintReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PrintReportRepository extends JpaRepository<PrintReport, Integer>, JpaSpecificationExecutor<PrintReport> {

    Page<PrintReport> findAll(Specification specification, Pageable pageable);

    Page<PrintReport> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM PrintReport p WHERE p.id = :id")
    Optional<PrintReport> findById(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PrintReport p WHERE p.id = :id")
    void deleteById(Integer id);

    @Query("SELECT COUNT(p) FROM PrintReport p WHERE p.tenantId = :tenantId")
    Integer countByTenantId(Integer tenantId);

    @Query("SELECT p FROM PrintReport p WHERE p.reportType = :type")
    Optional<PrintReport> findByType(String type);
}