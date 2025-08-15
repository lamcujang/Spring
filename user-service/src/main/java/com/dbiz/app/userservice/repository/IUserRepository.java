package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.IUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface IUserRepository extends JpaRepository<IUser, BigDecimal>, JpaSpecificationExecutor<IUser> {
}