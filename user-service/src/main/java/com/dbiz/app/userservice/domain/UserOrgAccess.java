package com.dbiz.app.userservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@Entity
@Table(name = "d_userorg_access", schema = "pos")
//@IdClass(PrimaryUserOrgAccess.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOrgAccess extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_userorg_access_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_userorg_access_sq")
    @SequenceGenerator(name = "d_userorg_access_sq", sequenceName = "d_userorg_access_sq", allocationSize = 1)
    private Integer id;


    @Column(name = "d_user_id", nullable = false)
    private Integer userId;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;



    @Column(name="d_tenant_id")
    private Integer tenantId;

}