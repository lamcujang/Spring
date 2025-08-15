package com.dbiz.app.paymentservice.repository;


import com.dbiz.app.paymentservice.domain.ReceiptOther;
import com.dbiz.app.paymentservice.domain.ReceiptOtherOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ReceiptOtherRepository extends JpaRepository<ReceiptOther, Integer>, JpaSpecificationExecutor<ReceiptOther> {
    @Query(value = "select coalesce(max(id),999999) from ReceiptOther")
    Integer getMaxId();
}