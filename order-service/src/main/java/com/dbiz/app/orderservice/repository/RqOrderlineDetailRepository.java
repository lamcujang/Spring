package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.RqOrderlineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RqOrderlineDetailRepository extends JpaRepository<RqOrderlineDetail, Integer>, JpaSpecificationExecutor<RqOrderlineDetail> {
    List<RqOrderlineDetail>getAllByRequestOrderLineId(Integer requestOrderLineId);
}