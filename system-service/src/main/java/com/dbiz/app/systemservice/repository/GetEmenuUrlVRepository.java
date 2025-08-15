package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.GetEmenuUrlV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GetEmenuUrlVRepository extends JpaRepository<GetEmenuUrlV, Integer>, JpaSpecificationExecutor<GetEmenuUrlV> {
}