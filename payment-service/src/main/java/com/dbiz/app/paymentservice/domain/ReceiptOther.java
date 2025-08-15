package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_receipt_other", schema = "pos")
public class ReceiptOther extends AbstractMappedEntity implements Serializable {


    @Id
    @Column(name = "d_receipt_other_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_receipt_other_sq")
    @SequenceGenerator(name = "d_receipt_other_sq", sequenceName = "d_receipt_other_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Size(max = 32)
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount_or_percent", nullable = false)
    private BigDecimal amountOrPercent;

    @Size(max = 1)
    @Column(name = "is_percent", nullable = false, length = 1)
    private String isPercent;

    @Size(max = 1)
    @Column(name = "is_auto_allocate", nullable = false, length = 1)
    private String isAutoAllocate;

    @Size(max = 1)
    @Column(name = "is_auto_return", nullable = false, length = 1)
    private String isAutoReturn;

    @Size(max = 36)
    @Column(name = "d_receipt_other_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String receiptOtherUu;

}