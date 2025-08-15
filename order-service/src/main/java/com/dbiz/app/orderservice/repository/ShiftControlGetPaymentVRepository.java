package com.dbiz.app.orderservice.repository;


import com.dbiz.app.orderservice.domain.view.ShiftControlGetPaymentV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ShiftControlGetPaymentVRepository extends JpaRepository<ShiftControlGetPaymentV, Integer>, JpaSpecificationExecutor<ShiftControlGetPaymentV> {
    List<ShiftControlGetPaymentV> findAllByShiftControlId(Integer shiftControlId);
}