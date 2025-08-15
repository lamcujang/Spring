package com.dbiz.app.paymentservice.repository;

import com.dbiz.app.paymentservice.domain.EInvoiceOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface EInvoiceOrgRepository extends JpaRepository<EInvoiceOrg, Integer> {

    @Transactional
    @Modifying
    @Query(value = "update d_einvoice_setup set is_default = ?2 where d_einvoice_setup_id = ?1", nativeQuery = true)
    void updateSetDefaultById(Integer setupId, String status);

    @Transactional
    @Modifying
    @Query(value="update d_einvoice_setup set is_default = 'N' where d_einvoice_setup_id != ?1", nativeQuery = true)
    void updateSetDefaultAllNotInById(Integer setupId);
}