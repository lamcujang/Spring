package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.view.GetUserOrgAccessV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GetUserOrgAccessVRepository extends JpaRepository<GetUserOrgAccessV, Integer>, JpaSpecificationExecutor<GetUserOrgAccessV> {

    @Query("select u from GetUserOrgAccessV u where u.userId = ?1 and u.isActive = 'Y'")
    List<GetUserOrgAccessV> findAllByUserId(Integer userId);
}