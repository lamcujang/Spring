package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.StorageOnhand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StorageOnhandRepository extends JpaRepository<StorageOnhand, Integer>, JpaSpecificationExecutor<StorageOnhand> {
}