package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, Integer> {

    Optional<Ward> findByCodeAndProvinceIdAndMergerDetails(String code, Integer provinceId, String mergerDetails);

}
