package com.dbiz.app.tenantservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_industry", schema = "pos")
public class Industry extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_industry_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "group_type")
    private String groupType;

    @Column(name = "business_model")
    private String businessModel;

    @Size(max = 36)
    @NotNull
    @Column(name = "d_industry_uu", nullable = false, length = 36)
    private String dIndustryUu;


}