package com.dbiz.app.inventoryservice.repository;

import com.dbiz.app.inventoryservice.domain.StorageOnhand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageOnhandRepository extends JpaRepository<StorageOnhand, Integer> {


  Optional<StorageOnhand> findByProductIdAndWarehouseIdAndLocatorId(Integer productId, Integer warehouseId, Integer locatorId);
  Optional<StorageOnhand> findByLotIdAndWarehouseIdAndLocatorId(Integer lotId, Integer warehouseId, Integer locatorId);
}