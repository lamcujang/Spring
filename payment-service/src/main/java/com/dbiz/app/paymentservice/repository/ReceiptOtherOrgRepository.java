package com.dbiz.app.paymentservice.repository;


import com.dbiz.app.paymentservice.domain.ReceiptOtherOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ReceiptOtherOrgRepository extends JpaRepository<ReceiptOtherOrg, Integer>, JpaSpecificationExecutor<ReceiptOtherOrg> {



    @Modifying
    void deleteReceiptOtherOrgByReceiptOtherId(Integer receiptOtherId);


    @Modifying
    @Query("update ReceiptOtherOrg set isActive = :isActive where receiptOtherId = :receiptOtherId")
    void updateIsActiveByReceiptOtherId(Integer receiptOtherId, String isActive);

    ReceiptOtherOrg findByReceiptOtherIdAndOrgId(Integer receiptOtherId, Integer orgId);
}