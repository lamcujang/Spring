package com.dbiz.app.orderservice.repository;


import com.dbiz.app.orderservice.domain.ReconcileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReconcileDetailRepository extends JpaRepository<ReconcileDetail, Integer> , JpaSpecificationExecutor<ReconcileDetail> {
}