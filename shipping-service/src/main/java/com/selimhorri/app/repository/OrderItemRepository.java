package com.dbiz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbiz.app.domain.OrderItem;
import com.dbiz.app.domain.id.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
	
	
	
}
