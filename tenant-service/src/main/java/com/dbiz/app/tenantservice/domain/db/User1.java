package com.dbiz.app.tenantservice.domain.db;

import com.dbiz.app.tenantservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = PRIVATE)
public class User1 {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq",
            sequenceName = "users_id_seq", allocationSize = 1)
    Long id;

    @Builder.Default
    @Enumerated(STRING)
    @Column(name = "role")
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    Collection<Role> roles = new ArrayList<>();

    String email;

    String password;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    Tenant1 tenant;

    public User1() {}
}
