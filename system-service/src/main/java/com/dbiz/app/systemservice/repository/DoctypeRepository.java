package com.dbiz.app.systemservice.repository;


import com.dbiz.app.systemservice.domain.Doctype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface DoctypeRepository extends JpaRepository<Doctype, BigDecimal>, JpaSpecificationExecutor<Doctype> {

    Doctype findByCode(String code);
}