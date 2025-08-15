package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_payment_type", schema = "pos")
public class PaymentType extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_payment_type_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_payment_type_sq")
    @SequenceGenerator(name = "d_payment_type_sq", sequenceName = "d_payment_type_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false, precision = 10)
    private Integer orgId;

    @Size(max = 3)
    @Column(name = "code", nullable = false, length = 3)
    private String code;

    @Size(max = 128)
    @NotNull
    @Column(name = "payment_name", nullable = false, length = 128)
    private String paymentName;

    @Size(max = 3)
    @NotNull
    @Column(name = "payment_type", nullable = false, length = 3)
    private String paymentType;

    @Size(max = 512)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_enable", nullable = false, length = 1)
    private String isEnable;


    @Column(name = "sequence_no", precision = 10)
    private BigDecimal sequenceNo;

    @Size(max = 15)
    @Column(name = "image_code", length = 15)
    private String imageCode;

}