package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_pc_terminalaccess", schema = "pos")
public class PcTerminalAccess extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pc_terminalaccess_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pc_terminalaccess_sq")
    @SequenceGenerator(name = "d_pc_terminalaccess_sq", sequenceName = "d_pc_terminalaccess_sq", allocationSize = 1)
    private Integer id;


    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "erp_pc_terminalaccess_id", precision = 10)
    private Integer erpPcTerminalAccessId;


    @Size(max = 36)
    @Column(name = "d_pc_terminalaccess_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String pcTerminalAccessUu;


    @Column(name = "d_product_category_id")
    private Integer productCategoryId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}