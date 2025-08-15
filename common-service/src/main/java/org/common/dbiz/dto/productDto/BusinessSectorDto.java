package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessSectorDto implements Serializable {

    Integer id;

    Integer businessSectorGroupId;

    Integer tenantId;

    Integer orgId;

    @Size(max = 1)
    String isActive;

    @Size(max = 32)
    String code;

    @Size(max = 1024)
    String name;

    BigDecimal vatRate;

    BigDecimal pitRate;

    BusinessSectorGroupDto businessSectorGroupDto;
}
