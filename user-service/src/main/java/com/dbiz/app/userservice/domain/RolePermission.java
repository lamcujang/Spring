package com.dbiz.app.userservice.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_role_permission", schema = "pos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class RolePermission extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_role_permission_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_role_permission_sq")
    @SequenceGenerator(name = "d_role_permission_sq", sequenceName = "d_role_permission_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_role_id", nullable = false)
    private Integer roleId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;
 
    @Type(type = "jsonb")
    @Column(name= "json_data",columnDefinition = "jsonb")
    private JsonNode jsonData;



}