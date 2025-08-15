package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PosPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface PosPaymentRepository extends JpaRepository<PosPayment, Integer>, JpaSpecificationExecutor<PosPayment> {

    List<PosPayment> findByPosOrderId(final Integer posOrderId);

    @Modifying
    void deletePosPaymentsByPosOrderIdAndTenantId(final Integer posOrderId, final Integer tenantId);


}