package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.TaxOffice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface TaxOfficeRepository extends JpaRepository<TaxOffice, Integer>, JpaSpecificationExecutor<TaxOffice> {

    Page<TaxOffice> findAll(Specification specification, Pageable pageable);
}
