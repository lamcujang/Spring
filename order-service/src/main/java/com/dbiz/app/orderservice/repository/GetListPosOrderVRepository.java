package com.dbiz.app.orderservice.repository;


import com.dbiz.app.orderservice.domain.view.GetListPosOrderV;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface GetListPosOrderVRepository extends JpaRepository<GetListPosOrderV, BigDecimal>, JpaSpecificationExecutor<GetListPosOrderV> {

    List<GetListPosOrderV> findAllByShiftControlId(Integer shiftControlID);
}