package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.POHeaderV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface POHeaderVRepository extends JpaRepository<POHeaderV, Integer>, JpaSpecificationExecutor<POHeaderV> {
}