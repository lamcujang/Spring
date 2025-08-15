package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.VoucherTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherTransactionRepository extends JpaRepository<VoucherTransaction, Integer> {
}