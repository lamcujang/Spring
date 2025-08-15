package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> , JpaSpecificationExecutor<BankAccount> {
    BankAccount findByIsDefault(String isDefault);

}