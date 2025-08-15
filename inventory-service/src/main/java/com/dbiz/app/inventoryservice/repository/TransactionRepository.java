package com.dbiz.app.inventoryservice.repository;

import com.dbiz.app.inventoryservice.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}