package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_environment_fee", schema = "pos")
public class EnvironmentFee extends AbstractMappedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_environment_fee_sq")
    @SequenceGenerator(name = "d_environment_fee_sq", sequenceName = "d_environment_fee_sq", allocationSize = 1)
    @Column(name = "d_environment_fee_id", nullable = false, precision = 10, updatable = true)
    private Integer id;

    @Column(name = "item_code", nullable = false, length = 32)
    private String itemCode;

    @Column(name = "item_name", nullable = false, length = 255)
    private String itemName;

    @Column(name = "unit_name", length = 50)
    private String unitName;

    @Column(name = "tax_rate", nullable = false, precision = 10)
    private BigDecimal taxRate;

    @Column(name = "tax_price", precision = 20)
    private BigDecimal taxPrice;

    @Column(name = "provincial_income_taxculation_price", precision = 20)
    private BigDecimal provincialIncomeTaxculationPrice;

    @Column(name = "tax_able_price", precision = 20)
    private BigDecimal taxAblePrice;

    @Column(name = "min_fee", precision = 20)
    private BigDecimal minFee;

    @Column(name = "max_fee", precision = 20)
    private BigDecimal maxFee;

    @Column(name = "apply_from")
    private Instant applyFrom;

    @Column(name = "apply_to")
    private Instant applyTo;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @Column(name = "tax_type", nullable = false)
    private Integer taxType;

    @Column(name = "resource_type", length = 5)
    private String resourceType;
}