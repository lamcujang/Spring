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
@Table(name = "d_bank", schema = "pos")
public class Bank extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_bank_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_bank_sq")
    @SequenceGenerator(name = "d_bank_sq", sequenceName = "d_bank_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 15)
    @Column(name = "bin_code", length = 15)
    private String binCode;

    @Size(max = 15)
    @Column(name = "swift_code", length = 15)
    private String swiftCode;

    @Column(name = "d_image_id", precision = 10)
    private BigDecimal imageId;


    @Size(max = 36)
    @Column(name = "d_bank_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String bankUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Size(max = 255)
    @Column(name = "short_name")
    private String shortName;

}