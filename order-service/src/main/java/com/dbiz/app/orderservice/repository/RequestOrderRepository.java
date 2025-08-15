package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.RequestOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestOrderRepository extends JpaRepository<RequestOrder, Integer>, JpaSpecificationExecutor<RequestOrder> {
    @Query(value = "SELECT coalesce(MAX(id),999999) FROM RequestOrder")
    Integer getMaxId();

    Boolean existsByFloorId(Integer floorId);

    List<RequestOrder> getAllByPosOrderId(Integer posOrderId);

    List<RequestOrder> getAllByCustomerPhoneAndOrderStatus(String customerPhone, String orderStatus);
}