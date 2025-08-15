package com.dbiz.app.tenantservice.domain.view;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
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
@Table(name = "d_industry_v", schema = "pos")
public class IndustryV extends AbstractMappedEntity implements Serializable {
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

    @Column(name = "group_type_code")
    private String groupTypeCode;

    @Column(name = "group_type_name")
    private String groupTypeName;

    @Column(name = "business_model")
    private String businessModel;

    @Column(name = "is_active")
    private String isActive;

}