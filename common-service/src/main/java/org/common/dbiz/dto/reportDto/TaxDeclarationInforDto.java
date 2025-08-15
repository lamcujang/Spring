package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationInforDto implements Serializable {

    private TaxDeclarationIndividualDto taxDeclarationIndividualDto;

    // Existing list fields
    private List<TaxDeclarationVatPitLineDto> taxDeclarationVatPitLineDto;
    private List<TaxDeclarationExciseDto> taxDeclarationExciseDto;
    private List<TaxDeclarationResourceEnvironmentDto> TaxDeclarationResourceEnvironmentDto;

    // Existing list fields
    private List<TaxDeclarationExpenseDetailDto> taxDeclarationExpenseDetailDto;
    private List<TaxDeclarationInventoryDetailDto> taxDeclarationInventoryDetailDto;
}