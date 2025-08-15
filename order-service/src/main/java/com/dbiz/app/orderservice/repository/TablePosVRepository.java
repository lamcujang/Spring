package com.dbiz.app.orderservice.repository;



import com.dbiz.app.orderservice.domain.view.TablePosV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TablePosVRepository extends JpaRepository<TablePosV, Integer>, JpaSpecificationExecutor<TablePosV> {

}