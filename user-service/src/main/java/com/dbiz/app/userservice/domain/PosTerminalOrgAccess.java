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
@Table(name = "d_pos_org_access", schema = "pos")
@IdClass(PrimaryPosTerminalOrgAccess.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PosTerminalOrgAccess extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_user_id", nullable = false)
    private Integer userId;


    @Id
    @Column(name = "d_pos_terminal_id", nullable = false)
    private Integer posTerminalId;





    @Column(name="d_tenant_id")
    private Integer tenantId;

    @Column(name="d_org_id")
    private Integer orgId;

}