package com.dbiz.app.orderservice.repository;



import com.dbiz.app.orderservice.domain.view.TablePosV;
import com.dbiz.app.orderservice.domain.view.TablePosVV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TablePosVV2Repository extends JpaRepository<TablePosVV2, Integer>, JpaSpecificationExecutor<TablePosVV2> {

}