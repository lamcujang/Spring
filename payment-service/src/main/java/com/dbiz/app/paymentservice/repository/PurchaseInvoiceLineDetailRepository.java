package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PurchaseInvoice;
import com.dbiz.app.paymentservice.domain.PurchaseInvoiceLine;
import com.dbiz.app.paymentservice.domain.PurchaseInvoiceLineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseInvoiceLineDetailRepository extends JpaRepository<PurchaseInvoiceLineDetail, Integer>,
        JpaSpecificationExecutor<PurchaseInvoiceLineDetail> {
    List<PurchaseInvoiceLineDetail> findAllByPurchaseInvoiceIdAndIsActive(Integer id, String isActive);

    @Modifying
    @Query("update PurchaseInvoiceLineDetail set isActive = :isActive where purchaseInvoiceId = :purchaseInvoiceId")
    void updateIsActiveByPurchaseInvoiceId(Integer purchaseInvoiceId, String isActive);
}
