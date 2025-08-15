package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, Integer>, JpaSpecificationExecutor<PurchaseInvoice> {
    @Query(value = "select coalesce(max(p.id),1000000) from PurchaseInvoice p ")
    Integer getMaxId();

    Boolean existsByDocumentNo(String documentNo);
}
