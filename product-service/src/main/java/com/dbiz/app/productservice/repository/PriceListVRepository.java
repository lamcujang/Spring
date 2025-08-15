package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.view.PriceListV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PriceListVRepository extends JpaRepository<PriceListV, Integer>, JpaSpecificationExecutor<PriceListV> {
}