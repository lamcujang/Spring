package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity

@Table(name = "d_pos_payment_dt", schema = "pos")
public class PosPaymentDT extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_payment_dt_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_payment_dt_sq")
    @SequenceGenerator(name = "d_pos_payment_dt_sq", sequenceName = "d_pos_payment_dt_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_pos_payment_id")
    private Integer posPaymentId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Size(max = 255)
    @Column(name = "code")
    private String code;


    @Column(name = "payment_method")
    private String paymentMethod;

    @Size(max = 36)
    @Column(name = "d_pos_payment_dt_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPosPaymentDtUu;

}