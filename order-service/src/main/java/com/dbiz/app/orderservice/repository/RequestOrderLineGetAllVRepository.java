package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.RequestOrderLineGetAllV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestOrderLineGetAllVRepository extends JpaRepository<RequestOrderLineGetAllV, Integer>, JpaSpecificationExecutor<RequestOrderLineGetAllV> {
}