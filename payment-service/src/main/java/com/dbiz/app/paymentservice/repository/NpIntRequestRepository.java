package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.NpIntRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NpIntRequestRepository extends JpaRepository<NpIntRequest, Integer> {
}
