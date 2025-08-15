package com.dbiz.app.userservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_user_other", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOther extends AbstractMappedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_user_other_sq")
    @SequenceGenerator(name = "d_user_other_sq", sequenceName = "d_user_other_sq", allocationSize = 1)
    @Column(name = "d_user_other_id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="address")
    private String address;

    @Column(name="city")
    private String city;

    @Column(name="wards")
    private String wards;

    @Column(name="description")
    private String description;
}
