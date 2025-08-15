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
@Table(name = "d_coupon", schema = "pos")
public class Coupon extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_coupon_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_coupon_sq")
    @SequenceGenerator(name = "d_coupon_sq", sequenceName = "d_coupon_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Size(max = 255)
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "balance_amount", nullable = false)
    private BigDecimal balanceAmount;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Size(max = 1)
    @Column(name = "is_available", length = 1)
    private String isAvailable;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Column(name = "d_customer_id", precision = 10)
    private Integer customerId;

    @Column(name = "erp_coupon_id", precision = 10)
    private Integer erpCouponId;

    @Size(max = 36)
    @Column(name = "d_coupon_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dCouponUu;

}