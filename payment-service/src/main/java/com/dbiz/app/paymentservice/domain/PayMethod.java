package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_pay_method", schema = "pos")
public class PayMethod  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pay_method_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pay_method_sq")
    @SequenceGenerator(name = "d_pay_method_sq", sequenceName = "d_pay_method_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_bank_id", nullable = false)
    private Integer bankId;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "d_image_id", precision = 10)
    private BigDecimal imageId;

    @Size(max = 1)
    @Column(name = "is_default", nullable = false, length = 1)
    private String isDefault;

    @Size(max = 36)
    @Column(name = "d_pay_method_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String payMethodUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}