package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosLotRepository extends JpaRepository<PosLot, Integer> {
}
