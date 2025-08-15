package com.dbiz.app.integrationservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@Table(name = "d_integration_history", schema = "pos")
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationHistory extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_integration_history_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_integration_history_sq")
    @SequenceGenerator(name = "d_integration_history_sq", sequenceName = "d_integration_history_sq", allocationSize = 1)
    private Integer id;


    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @NotNull
    @Column(name = "int_date", nullable = false)
    private Instant intDate;

    @Size(max = 3)
    @NotNull
    @Column(name = "int_type", nullable = false, length = 3)
    private String intType;

    @Size(max = 3)
    @NotNull
    @Column(name = "int_flow", nullable = false, length = 3)
    private String intFlow;

    @Size(max = 3)
    @NotNull
    @Column(name = "int_status", nullable = false, length = 3)
    private String intStatus;

    @Size(max = 36)
    @Column(name = "d_integration_history_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String integrationHistoryUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "payload")
    private String payload;

    @Column(name = "response")
    private String response;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description = "";

    @Column(name="integration_type")
    private String integrationType;
}