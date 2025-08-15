package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.view.PosterminalOrgAccessV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PosterminalOrgAccessVRepository extends JpaRepository<PosterminalOrgAccessV, BigDecimal> {

    @Query(value = "SELECT u FROM PosterminalOrgAccessV u WHERE u.userId  = ?1 AND u.orgId = ?2 and u.isActive = 'Y'" )
    List<PosterminalOrgAccessV>findAllByUserIdAndOrgId(Integer userId,Integer orgId);


}