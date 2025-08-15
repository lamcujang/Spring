package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {


    @Query(value = "select coalesce(max(p.id),1000000) from Payment p ")
    Integer getMaxId();
}
