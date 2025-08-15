package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer>, JpaSpecificationExecutor<PaymentType> {
}