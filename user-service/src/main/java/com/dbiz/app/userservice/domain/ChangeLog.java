package com.dbiz.app.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "d_changelog", schema = "pos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_changelog_sq")
    @SequenceGenerator(name = "d_changelog_sq", sequenceName = "pos.d_changelog_sq", allocationSize = 1)
    @Column(name = "d_change_log", nullable = false, precision = 10)
    private Long id;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "old_value", length = 512)
    private String oldValue;

    @Column(name = "new_value", length = 512)
    private String newValue;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Column(name = "updated_by", nullable = false)
    private Integer updatedBy;

    @Column(name = "d_changelog_uu", nullable = false, length = 36)
    private String changeLogUU;

    @Column(name = "entity_id", nullable = false)
    private Integer entityId;

}
