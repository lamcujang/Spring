package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.PosOrderListView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PosOrderListViewRepository extends JpaRepository<PosOrderListView, Integer>, JpaSpecificationExecutor<PosOrderListView> {
}