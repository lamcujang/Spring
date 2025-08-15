package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.ChangeLog;
import com.dbiz.app.userservice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChangelogRepository extends JpaRepository<ChangeLog, Integer> {
}
