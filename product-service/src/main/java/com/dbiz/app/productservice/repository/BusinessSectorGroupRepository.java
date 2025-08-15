package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.BusinessSectorGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BusinessSectorGroupRepository extends JpaRepository<BusinessSectorGroup, Integer>, JpaSpecificationExecutor<BusinessSectorGroup> {

    Optional<BusinessSectorGroup> findByCode(String name);
}