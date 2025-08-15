package com.dbiz.app.integrationservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_import_file_history", schema = "pos")
public class ImportFileHistory extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_import_file_history_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_import_file_history_sq")
    @SequenceGenerator(name = "d_import_file_history_sq", sequenceName = "d_import_file_history_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "code")
    private String code;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_tail")
    private String fileTail;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "import_status")
    private String importStatus;

    @Column(name = "import_date")
    private Instant importDate;

}
