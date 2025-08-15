package com.dbiz.app.inventoryservice.repository;

import com.dbiz.app.inventoryservice.domain.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LotRepository extends JpaRepository<Lot, Integer> {

    @Query(value = "select coalesce(max(l.id),1000000) from Lot l ")
    Integer getMaxId();
}
