package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.PayMethodOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PayMethodOrgRepository extends JpaRepository<PayMethodOrg, Integer>, JpaSpecificationExecutor<PayMethodOrg> {
    Optional<PayMethodOrg> findByPayMethodId(Integer payMethodId);

    @Query(value = "select * from pos.d_paymethod_org po " +
            "where po.d_tenant_id = :tenantId " +
            "and po.d_org_id = :orgId " +
            "and po.d_pay_method_id = (" +
            "select pm.d_pay_method_id " +
            "from pos.d_pay_method pm " +
            "where pm.name = :payMethodName" +
            ")",
            nativeQuery = true)
    Optional<PayMethodOrg> findByOrgIdAndPayMethodName(
            @Param("tenantId") Integer tenantId,
            @Param("orgId") Integer orgId,
            @Param("payMethodName") String payMethodName);

    List<PayMethodOrg> findAllByOrgId(Integer orgId);
}