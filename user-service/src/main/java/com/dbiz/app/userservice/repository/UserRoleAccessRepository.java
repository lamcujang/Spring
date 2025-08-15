package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.UserRoleAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleAccessRepository extends JpaRepository<UserRoleAccess, UserRoleAccess>, JpaSpecificationExecutor<UserRoleAccess> {
    List<UserRoleAccess> findAllByUserId(final Integer userId);

    UserRoleAccess findByUserIdAndRoleId(final Integer userId, final Integer roleId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE pos.d_user_role_access SET is_active = ?2 WHERE d_user_id = ?1", nativeQuery = true)
    void updateAllByUserId(final Integer userId, final String isActive);
//
//    @Modifying
//    @Transactional
//    void updateAllByUserId (final Integer userId, final String isActive);

    @Query("SELECT ura.roleId FROM UserRoleAccess ura WHERE ura.userId = :userId")
    List<Integer> findRoleIdByUserId(@Param("userId") Integer userId);

    @Query("SELECT ura.userId FROM UserRoleAccess ura WHERE ura.roleId = :roleId and ura.isActive = 'Y'")
    List<Integer> findByRoleId(Integer roleId);

}