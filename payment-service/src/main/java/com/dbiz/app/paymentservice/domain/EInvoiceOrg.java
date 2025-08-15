package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "d_einvoice_org", schema = "pos")
public class EInvoiceOrg extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_einvoice_org_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_einvoice_org_sq")
    @SequenceGenerator(name = "d_einvoice_org_sq", sequenceName = "d_einvoice_org_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    Integer tenantId;

    @Column(name = "d_org_id")
    Integer orgId;

    @Column(name = "d_einvoice_setup_id")
    Integer einvoiceSetupId;

    @Column(name = "taxid")
    String taxId;

    @Column(name = "password")
    String password;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "is_direct_issue")
    String isDirectIssue;

    @Column(name = "is_attach_note")
    String isAttachNote;

    @Column(name = "invoice_sign")
    String invoiceSign;

    @Column(name = "invoice_form")
    String invoiceForm;
}
