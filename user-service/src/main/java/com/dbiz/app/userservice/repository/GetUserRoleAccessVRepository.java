package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.view.GetUserRoleAccessV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GetUserRoleAccessVRepository extends JpaRepository<GetUserRoleAccessV, Integer> {

    @Query(value = "select * from pos.d_get_user_role_access_v where d_user_id = :userId",nativeQuery = true)
    List<GetUserRoleAccessV> findAllByUserId(Integer userId);
}