package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PosOrderRepository extends JpaRepository<PosOrder, Integer>, JpaSpecificationExecutor<PosOrder> {

    @Query(value = "SELECT * FROM d_pos_order U  JOIN d_pos_orderline  L  on U.d_pos_order_id = L.d_pos_order_id " +
            "where U.d_pos_order_id = :id",
            nativeQuery = true)
    Page<PosOrder> findAllByCriteria(@Param("id") Integer id, Pageable pageable);


    List<Integer> findByOrderStatusAndTableId(String orderStatus, Integer tableId);
    @Query(value = "select p.d_pos_order_id from pos.d_pos_order p where p.order_status = :orderStatus and p.d_table_id = :tableId and p.is_active='Y'  limit 1",nativeQuery = true)
    Integer findByOrderStatusAndTableIdLimit(String orderStatus, Integer tableId);

    @Query("select coalesce(max(p.id),999999) from PosOrder p")
    Integer getMaxId();


    PosOrder findByTableIdAndOrderStatusAndCustomerId(Integer tableId, String orderStatus,Integer customerId);

    Boolean existsByFloorIdAndOrderStatus(Integer floorId, String orderStatus);
    Boolean existsByFloorId(Integer floorId);

    Boolean existsByTableIdAndOrderStatus(Integer tableId, String orderStatus);

    Optional<PosOrder> findByNpOrderId(Integer id);
}