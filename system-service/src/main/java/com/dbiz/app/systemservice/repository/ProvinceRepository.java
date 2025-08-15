package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    Optional<Province> findByCode(String code);

}