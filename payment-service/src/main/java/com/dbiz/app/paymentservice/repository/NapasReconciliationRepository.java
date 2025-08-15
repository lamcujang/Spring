package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.NapasReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NapasReconciliationRepository extends JpaRepository<NapasReconciliation, Integer> {
}