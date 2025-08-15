package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_declaration_expense_detail", schema = "pos")
public class TaxDeclarationExpenseDetail extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_declaration_expense_detail_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_declaration_expense_detail_sq")
    @SequenceGenerator(name = "d_tax_declaration_expense_detail_sq", sequenceName = "d_tax_declaration_expense_detail_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    private Integer taxDeclarationIndividualId;

    @Column(name = "d_expense_type_id", nullable = false, precision = 10)
    private Integer expenseTypeId;

    @Column(name = "expense_amount", precision = 20, scale = 2)
    private BigDecimal expenseAmount;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;
}