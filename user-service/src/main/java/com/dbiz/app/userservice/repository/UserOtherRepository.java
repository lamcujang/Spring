package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.UserOther;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserOtherRepository extends JpaRepository<UserOther, Integer> {

    List<UserOther> findAllByPhone(String phone);

    @Query(value = "select coalesce(max(p.id),1000000) from UserOther  p ")
    Integer getMaxId();
}
