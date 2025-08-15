package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.RequestOrderGetAllV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestOrderGetAllVRepository extends JpaRepository<RequestOrderGetAllV, Integer>, JpaSpecificationExecutor<RequestOrderGetAllV> {

    List<RequestOrderGetAllV> getAllByCustomerPhone1AndOrderStatus(String phone, String orderStatus);
}