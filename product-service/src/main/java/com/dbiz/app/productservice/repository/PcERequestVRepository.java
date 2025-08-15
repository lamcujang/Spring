package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.view.PcERequestV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PcERequestVRepository extends JpaRepository<PcERequestV, Integer>, JpaSpecificationExecutor<PcERequestV> {
}