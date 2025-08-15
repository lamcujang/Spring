package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PosReceiptOther;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PosReceiptOtherRepository extends JpaRepository<PosReceiptOther, Integer> {

    @Modifying
    @Query("delete from PosReceiptOther where posOrderId = ?1 and tenantId = ?2")
    void deletePosReceiptOtherByPosOrderIdAndTenantId(Integer posOrderId, Integer tenantId);

    @Modifying
    @Query(value = "update PosReceiptOther  a set a.isActive = ?1, a.isCal = ?1 where a.posOrderId = ?2")
    void updateStatusPosReceiptOthers(String isActive,Integer posOrderId);

    PosReceiptOther findByPosOrderIdAndReceiptOtherIdAndTaxId
            (Integer posOrderId,Integer receiptOtherId, Integer taxId);


}