package com.dbiz.app.tenantservice.repository;

import com.dbiz.app.tenantservice.domain.AuditLogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogUserRepository extends JpaRepository<AuditLogUser,Integer> {
}
