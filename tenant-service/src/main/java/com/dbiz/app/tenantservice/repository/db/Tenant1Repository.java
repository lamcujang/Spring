package com.dbiz.app.tenantservice.repository.db;

import com.dbiz.app.tenantservice.domain.db.Tenant1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface Tenant1Repository extends JpaRepository<Tenant1, Long> {

    @Modifying
    @Query(value =

    "update tenants " +
    "set name = ?2 " +
    "where id = ?1 "

    , nativeQuery = true)
    void rename(Long id, String name);
}
