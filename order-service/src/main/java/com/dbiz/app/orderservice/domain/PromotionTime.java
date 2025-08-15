package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;
import java.time.OffsetTime;

@Getter
@Setter
@Entity
@Table(name = "d_promotion_time", schema = "pos")
public class PromotionTime extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_promotion_time_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_promotion_time_sq")
    @SequenceGenerator(name = "d_promotion_time_sq", sequenceName = "d_promotion_time_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_promotion_id", nullable = false)
    private Integer promotionId;

    @Column(name = "from_hour", nullable = false)
    private LocalTime fromHour;

    @Column(name = "to_hour", nullable = false)
    private LocalTime toHour;
}