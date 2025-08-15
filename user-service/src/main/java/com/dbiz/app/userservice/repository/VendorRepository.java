package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Customer;
import com.dbiz.app.userservice.domain.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VendorRepository  extends JpaRepository<Vendor, Integer>, JpaSpecificationExecutor<Vendor> {//RevisionRepository<Vendor, Integer, Integer>,JpaRepository<Vendor, Integer>, JpaSpecificationExecutor<Vendor> {
    Page<Vendor> findAll(Specification specification, Pageable pageable);

    @Query(value = "select dv.d_vendor_id from pos.d_vendor dv join pos.d_payment dp on dp.d_vendor_id = dv.d_vendor_id\n" +
            "where payment_amount between  ?  and ? and dv.d_tenant_id = ? ",nativeQuery = true)
    List<Integer> findByTransactionAmountBetween(BigDecimal transactionAmountFrom, BigDecimal transactionAmountTo, Integer tenantId);

    @Query(value = "select sum(payment_amount) from pos.d_payment where d_vendor_id = ? ",nativeQuery = true)
    BigDecimal getTotalTransactionAmountByVendorId(Integer vendorId);

    @Query(value = "select v from Vendor v where v.id = :id ")
    Optional<Vendor> findById(@Param("id") Integer integer);

    Vendor findByErpVendorId(Integer id);

    @Query(value = "select coalesce(max(d_vendor_id),1000000) from pos.d_vendor",nativeQuery = true)
    Integer getMaxId();

    List<Vendor>findByPhone1(String phone1);


    List<Vendor> findAllByCodeAndTenantId(String code, Integer tenantId);

}
