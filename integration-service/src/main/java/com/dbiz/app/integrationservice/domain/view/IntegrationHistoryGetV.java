package com.dbiz.app.integrationservice.domain.view;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_integration_history_get_v", schema = "pos")
public class IntegrationHistoryGetV extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_integration_history_id", precision = 10)
    private Integer integrationHistoryId;


    @Column(name = "int_date")
    private Instant intDate;

    @Embedded
    UserV userV;

    @Size(max = 15)
    @Column(name = "type_value", length = 15)
    private String typeValue;

    @Size(max = 64)
    @Column(name = "int_type", length = 64)
    private String intType;

    @Size(max = 15)
    @Column(name = "flow_value", length = 15)
    private String flowValue;

    @Size(max = 64)
    @Column(name = "int_flow", length = 64)
    private String intFlow;

    @Size(max = 15)
    @Column(name = "status_value", length = 15)
    private String statusValue;

    @Size(max = 64)
    @Column(name = "int_status", length = 64)
    private String intStatus;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

}