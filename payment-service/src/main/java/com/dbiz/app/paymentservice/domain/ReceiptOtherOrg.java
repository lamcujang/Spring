package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_receipt_other_org", schema = "pos")
public class ReceiptOtherOrg extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_receipt_other_org_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_receipt_other_org_sq")
    @SequenceGenerator(name = "d_receipt_other_org_sq", sequenceName = "d_receipt_other_org_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_receipt_other_id", nullable = false)
    private Integer receiptOtherId;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;


    @Size(max = 36)
    @Column(name = "d_receipt_other_org_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String receiptOtherOrgUu;

}