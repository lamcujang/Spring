package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosLot;
import com.dbiz.app.orderservice.domain.RoLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoLotRepository extends JpaRepository<RoLot, Integer> {
}
