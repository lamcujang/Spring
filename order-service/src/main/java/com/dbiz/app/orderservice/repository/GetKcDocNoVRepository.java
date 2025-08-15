package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.view.GetKcDocNoV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GetKcDocNoVRepository extends JpaRepository<GetKcDocNoV, Integer>, JpaSpecificationExecutor<GetKcDocNoV> {
}