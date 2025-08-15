package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {

    @Query("SELECT p FROM Invoice p WHERE p.id = :invoiceId")
    Optional<Invoice> findById(Integer invoiceId);


    @Query(value = "select coalesce(max(p.id),1000000) from Invoice p ")
    Integer getMaxId();
}