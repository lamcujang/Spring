package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosOrderLineDetail;
import com.dbiz.app.orderservice.domain.PosOrderline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PosOrderLineDetailRepository extends JpaRepository<PosOrderLineDetail, Integer>, JpaSpecificationExecutor<PosOrderLineDetail> {

}