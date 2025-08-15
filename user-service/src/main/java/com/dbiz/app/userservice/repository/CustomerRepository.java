package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Customer;
import com.dbiz.app.userservice.domain.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    Page<Customer> findAll( Specification specification,Pageable pageable);

    @Query(value = "select dc.d_customer_id from pos.d_customer dc join pos.d_payment dp on dp.d_customer_id = dc.d_customer_id\n" +
            "where payment_amount between  ?  and ? and dc.d_tenant_id = ?",nativeQuery = true)
    List<Integer> findByTransactionAmountBetween(BigDecimal transactionAmountFrom, BigDecimal transactionAmountTo, Integer tenantId);

    @Query(value="select dc.d_customer_id from pos.d_customer dc join pos.d_invoice di on dc.d_customer_id = di.d_customer_id where di.search_code = ? and dc.d_tenant_id=?"
    ,nativeQuery = true)
    List<Integer>findByDocNo(String docNo,Integer tenantId);

    @Query(value = "select sum(payment_amount) from pos.d_payment where d_customer_id = ? ",nativeQuery = true)
    BigDecimal getTotalTransactionAmountByCustomerId(Integer customerId);

    @Query(value = "select * from pos.d_customer where erp_customer_id = ?",nativeQuery = true)
    Optional<Customer> findByErpCustomerId(Integer id);
    @Query(value = "select coalesce(max(p.id),1000000) from Customer  p ")
    Integer getMaxId();

    @Query(value = "select cus from Customer cus where cus.phone1 = :phone1" )
    Optional<Customer> findFirstByPhone1(@RequestParam("phone1") String phone1);

    Boolean existsByCode(String code);

    Customer findByCode(String code);

    Optional<Customer> findByErpCustomerName(String erpCustomerName);

    List<Customer> findByPhone1(String phone1);

    Boolean existsByPhone1(String phone1);

    Boolean existsByPhone1AndIdNot(String phone1, Integer id);

}
