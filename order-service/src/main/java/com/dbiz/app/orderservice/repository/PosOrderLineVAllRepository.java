package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosOrder;
import com.dbiz.app.orderservice.domain.view.PosOrderLineVAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PosOrderLineVAllRepository extends JpaRepository<PosOrderLineVAll, Integer>, JpaSpecificationExecutor<PosOrderLineVAll> {

    List<PosOrderLineVAll>findAllByPosOrderAndIsActive(PosOrder id,String isActive);

    List<PosOrderLineVAll> findByPosOrder_Id(Integer posOrderId);

    List<PosOrderLineVAll>findAllByPosOrder(PosOrder posOrder);
    

    List<PosOrderLineVAll> findByPosOrder_IdAndIsActive(Integer posOrderId,String isActive);

    List<PosOrderLineVAll> findByPosOrder_IdAndIsActiveAndParentId(Integer posOrderId,String isActive,Integer parentId);
}