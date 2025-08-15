package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.PenaltyDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyDeductionRepository extends JpaRepository<PenaltyDeduction,Integer> {
}
