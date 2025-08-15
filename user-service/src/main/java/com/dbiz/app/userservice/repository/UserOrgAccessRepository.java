package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.PrimaryUserOrgAccess;
import com.dbiz.app.userservice.domain.UserOrgAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserOrgAccessRepository extends JpaRepository<UserOrgAccess, PrimaryUserOrgAccess>, JpaSpecificationExecutor<UserOrgAccess> {

    List<UserOrgAccess> findAllByUserIdAndIsActive(final Integer userId,String isActive);

    UserOrgAccess findByUserIdAndOrgId(final Integer userId, final Integer orgId);

    @Query("update UserOrgAccess u set u.isActive = ?2 where u.userId = ?1 and u.tenantId = ?3")
    @Modifying
    @Transactional
    void updateAllByUserIdAndTenantId(final Integer userId, final String isActive,Integer tenantId);

    @Query("SELECT u.userId FROM UserOrgAccess u WHERE u.orgId = :orgId AND u.isActive = 'Y'")
    List<Integer> findByOrgId(Integer orgId);
}