package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.Industry;
import com.dbiz.app.tenantservice.domain.view.IndustryV;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IndustryRepository extends JpaRepository<Industry, Integer> {
    Page<Industry> findAll(Pageable pageable);

    @Query("SELECT new com.dbiz.app.tenantservice.domain.view.IndustryV(" +
            "di.id, di.code, di.name, di.groupTypeCode, di.groupTypeName, di.businessModel, di.isActive) " +
            "FROM IndustryV di")
    List<IndustryV> getAllGroupByType();

}
