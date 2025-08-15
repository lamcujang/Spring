package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PayMethodRepository extends JpaRepository<PayMethod, Integer>, JpaSpecificationExecutor<PayMethod> {

    PayMethod findByBankId(Integer bankId);
}