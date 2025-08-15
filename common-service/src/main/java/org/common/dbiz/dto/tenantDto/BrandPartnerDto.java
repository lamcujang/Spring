package org.common.dbiz.dto.tenantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BrandPartnerDto implements Serializable {
    Integer id;
    private String name;
    private String code;
    private String logoUrl;
    private String imageCode;
    private String description;
    private String isActive;
}