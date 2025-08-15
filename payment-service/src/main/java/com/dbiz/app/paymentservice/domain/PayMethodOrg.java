package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@Table(name = "d_paymethod_org", schema = "pos")
@AllArgsConstructor
@NoArgsConstructor
public class PayMethodOrg extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_paymethod_org_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_paymethod_org_sq")
    @SequenceGenerator(name = "d_paymethod_org_sq", sequenceName = "d_paymethod_org_sq", allocationSize = 1)
    private Integer id;




    @Size(max = 32)
    @Column(name = "access_code", length = 32)
    private String accessCode;

    @Size(max = 32)
    @Column(name = "terminal_id", length = 32)
    private String terminalId;

    @Size(max = 32)
    @Column(name = "merchant_code", length = 32)
    private String merchantCode;

    @Size(max = 512)
    @Column(name = "hash_key", length = 512)
    private String hashKey;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_paymethod_org_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPaymethodOrgUu;


    @Column(name = "d_pay_method_id")
    private Integer payMethodId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}