package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.PartnerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

public interface PartnerGroupRepository extends JpaRepository<PartnerGroup, Integer>, JpaSpecificationExecutor<PartnerGroup> {

    PartnerGroup findByErpBpGroupId(Integer id);

    @Query(value="SELECT coalesce(max(p.id),1000000) from PartnerGroup p ")
    Integer getMaxId();
    PartnerGroup findByGroupCode(String groupCode);

    PartnerGroup findByGroupName(String groupName);

    Optional<PartnerGroup> findByErpBpGroupName(String name);

    Boolean existsByGroupName(String groupName);

    Boolean existsByGroupCode(String groupCode);

}