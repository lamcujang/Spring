package com.dbiz.app.integration.integrationservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_reference_get_v", schema = "pos")
public class ReferenceGetV {
    @Id
    @Column(name = "d_reference_id", precision = 10)
    private BigDecimal dReferenceId;

    @Size(max = 32)
    @Column(name = "name_reference", length = 32)
    private String nameReference;

    @Size(max = 64)
    @Column(name = "name", length = 64)
    private String name;

    @Size(max = 15)
    @Column(name = "value", length = 15)
    private String value;

}