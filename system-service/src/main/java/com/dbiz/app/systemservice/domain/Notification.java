package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_notification", schema = "pos")
public class Notification extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_notification_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_notification_sq")
    @SequenceGenerator(name = "d_notification_sq", sequenceName = "d_notification_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Size(max = 64)
    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Size(max = 5)
    @Column(name = "notification_type", nullable = false, length = 5)
    private String notificationType;

    @Size(max = 5)
    @NotNull
    @Column(name = "status", nullable = false, length = 5)
    private String status;

    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "route_function")
    private String routeFunction;

}