package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PurchaseInvoiceLine;
import com.dbiz.app.paymentservice.domain.PurchaseInvoiceLineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseInvoicelineRepository extends JpaRepository<PurchaseInvoiceLine, Integer>, JpaSpecificationExecutor<PurchaseInvoiceLine> {
    List<PurchaseInvoiceLine> findAllByPurchaseInvoiceIdAndIsActive(Integer id, String isActive);

    @Modifying
    @Query("update PurchaseInvoiceLine set isActive = :isActive where purchaseInvoiceId = :purchaseInvoiceId")
    void updateIsActiveByPurchaseInvoiceId(Integer purchaseInvoiceId, String isActive);

    PurchaseInvoiceLine findByPurchaseOrderIdAndPurchaseInvoiceId(Integer purchaseOrderId, Integer purchaseInvoiceId);
}
