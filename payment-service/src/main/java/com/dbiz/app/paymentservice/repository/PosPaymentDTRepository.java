package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PosPaymentDT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PosPaymentDTRepository extends JpaRepository<PosPaymentDT, Integer> {


    @Modifying
    @Query(value = "delete from pos.d_pos_payment_dt where d_pos_payment_id " +
            " in (select d_pos_payment_id from pos.d_pos_payment where d_pos_order_id = ?1 and d_tenant_id = ?2) ",
            nativeQuery = true)
    void deletePosPaymentDTByPosOrderId( Integer posOrderId,  Integer tenantId);
}