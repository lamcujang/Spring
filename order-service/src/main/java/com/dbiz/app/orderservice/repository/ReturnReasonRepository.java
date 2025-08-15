package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.CancelReason;
import com.dbiz.app.orderservice.domain.ReturnReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReturnReasonRepository extends JpaRepository<ReturnReason, Integer>, JpaSpecificationExecutor<ReturnReason> {
}
