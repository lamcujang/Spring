package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findByCodeAndTenantId(String code, Integer tenantId);

    Optional<Coupon>findByErpCouponIdAndTenantId(Integer id,Integer tenantId);


    @Modifying
    @Query("update Coupon c set c.isAvailable = ?3 where c.code = ?1 and c.tenantId = ?2")
    void updateStatusByCodeAndTenantId(@Param("code") String code,
                                       @Param("tenantId") Integer tenantId,
                                       @Param("isAvailable") String isAvailable);

}