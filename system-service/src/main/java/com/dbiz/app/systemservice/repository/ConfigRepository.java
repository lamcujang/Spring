package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, Integer>, JpaSpecificationExecutor<Config> {

    @Query(value = "select p.value from Config p where p.name = :name and p.tenantId = :tenantId")
    String findValueByNameAndTenantId(@Param("name") String name ,@Param("tenantId") Integer tenantId);

}