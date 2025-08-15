package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BonusRepository extends JpaRepository<Bonus, Integer>, JpaSpecificationExecutor<Bonus> {


}