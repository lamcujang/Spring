package com.dbiz.app.reportservice.repository;

import com.dbiz.app.reportservice.domain.HouseholdIndustry;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import com.dbiz.app.reportservice.domain.TaxPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaxPaymentMethodRepository extends JpaRepository<TaxPaymentMethod, Integer>, JpaSpecificationExecutor<TaxPaymentMethod> {

    Optional<TaxPaymentMethod> findByCode(String code);
}
