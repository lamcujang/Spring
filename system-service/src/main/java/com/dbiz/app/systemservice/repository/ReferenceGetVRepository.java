package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.view.ReferenceGetV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface ReferenceGetVRepository extends JpaRepository<ReferenceGetV, BigDecimal>, JpaSpecificationExecutor<ReferenceGetV> {

    ReferenceGetV findByNameReferenceAndValue(String nameReference, String value);

    List<ReferenceGetV> findByNameReference(String nameReference);
}