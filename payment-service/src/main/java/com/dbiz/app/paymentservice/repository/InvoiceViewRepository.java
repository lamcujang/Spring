package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.InvoiceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvoiceViewRepository extends JpaRepository<InvoiceView, Integer>, JpaSpecificationExecutor<InvoiceView> {
}