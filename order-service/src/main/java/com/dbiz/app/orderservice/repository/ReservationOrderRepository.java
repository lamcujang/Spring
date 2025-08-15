package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.ReservationOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationOrderRepository extends JpaRepository<ReservationOrder, Integer>, JpaSpecificationExecutor<ReservationOrder> {

    @Query(value = "select dr.d_reservation_order_id from d_reservation_order dr join d_customer dc on dr.d_customer_id = dc.d_customer_id \n" +
            "    where dc.name like '% ?1 %' and dr.d_tenant_id  = ?2  " ,nativeQuery = true)
    List<Integer>getIdCustomerByName(String name, Integer tenantId);

    @Query(value = "select r from ReservationOrder r where r.tableId =:tableId and TO_CHAR(r.reservationTime, 'YYYY-MM-DD') =:date and r.tenantId = :tenantId")
    ReservationOrder findTableByIdAndDate(Integer tableId, String date,Integer tenantId);
}