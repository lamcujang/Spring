package com.dbiz.app.tenantservice.domain.db;

import com.dbiz.app.tenantservice.enums.DatabaseCreationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tenants")
@FieldDefaults(level = PRIVATE)
public class Tenant1 {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenants_id_seq")
    @SequenceGenerator(name = "tenants_id_seq",
            sequenceName = "tenants_id_seq", allocationSize = 1)
    Long id;

    String name;

    @Column(name = "db_name")
    String dbName;

    @Column(name = "user_name")
    String userName;

    @Column(name = "db_password")
    String dbPassword;

    @Enumerated(STRING)
    @Column(name = "creation_status")
    DatabaseCreationStatus creationStatus;
}

