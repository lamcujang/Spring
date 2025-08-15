package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_np_int_request", schema = "pos")
public class NpIntRequest extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_np_int_request_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_np_int_request_sq")
    @SequenceGenerator(name = "d_np_int_request_sq", sequenceName = "d_np_int_request_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id")
    private Integer orgId;

    @NotNull
    @Column(name = "d_pos_terminal_id", nullable = false)
    private Integer posTerminalId;

    @NotNull
    @Column(name = "d_bankaccount_id", nullable = false)
    private Integer bankAccountId;

    @Size(max = 30)
    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Size(max = 5)
    @Column(name = "request_status", nullable = false, length = 5)
    private String requestStatus;

//    @Size(max = 36)
//    @Column(name = "d_np_int_request_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
//    private String NpIntRequestUu;

}
