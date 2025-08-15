package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.ReservationVAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface ReservationVAllRepository extends JpaRepository<ReservationVAll, BigDecimal>, JpaSpecificationExecutor<ReservationVAll> {
}