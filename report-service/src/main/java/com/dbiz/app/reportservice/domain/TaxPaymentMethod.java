package com.dbiz.app.reportservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_payment_method", schema = "pos")
public class TaxPaymentMethod extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_payment_method_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_payment_method_sq")
    @SequenceGenerator(name = "d_tax_payment_method_sq", sequenceName = "d_tax_payment_method_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", length = 1, nullable = false)
    private String isActive;
}