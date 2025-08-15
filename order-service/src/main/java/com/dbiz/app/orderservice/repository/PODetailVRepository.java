package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.PODetailV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PODetailVRepository extends JpaRepository<PODetailV, Integer>, JpaSpecificationExecutor<PODetailV> {
}