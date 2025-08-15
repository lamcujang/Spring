package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationLineRepository extends JpaRepository<ReservationLine, Integer>, JpaSpecificationExecutor<ReservationLine> {
}