package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.view.GetKcProductV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GetKcProductVRepository extends JpaRepository<GetKcProductV, Integer>, JpaSpecificationExecutor<GetKcProductV> {
}