package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>, JpaSpecificationExecutor<Currency> {
        Currency findByCurrencyCode(String currencyCode);
}