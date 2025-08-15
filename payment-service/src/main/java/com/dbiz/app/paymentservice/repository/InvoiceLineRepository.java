package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Integer>, JpaSpecificationExecutor<InvoiceLine> {
    @Query("SELECT p FROM InvoiceLine p WHERE p.invoiceId = :invoiceId")
    List<InvoiceLine> findAllByInvoiceId(Integer invoiceId);
}