package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.NapasTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NapasTransactionRepository extends JpaRepository<NapasTransaction, Integer> {
}