package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.WDSDetailHistory;
import com.dbiz.app.orderservice.domain.view.GetKolSameProductV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WDSDetailHistoryRepository extends JpaRepository<WDSDetailHistory, Integer> , JpaSpecificationExecutor<WDSDetailHistory> {
}