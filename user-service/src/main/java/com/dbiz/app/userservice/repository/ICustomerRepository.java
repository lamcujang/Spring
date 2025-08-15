package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.ICustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ICustomerRepository extends JpaRepository<ICustomer, Integer>, JpaSpecificationExecutor<ICustomer> {
}