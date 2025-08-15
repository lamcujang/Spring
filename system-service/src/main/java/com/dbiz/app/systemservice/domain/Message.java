package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = "d_message", schema = "pos")
public class Message extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_message_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_message_sq")
    @SequenceGenerator(name = "d_message_sq", sequenceName = "d_message_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Size(max = 512)
    @NotNull
    @Column(name = "value", nullable = false, length = 512)
    private String value;

    @Column(name = "msg_text")
    @Type(type = "org.hibernate.type.TextType")
    private String msgText;
 
}