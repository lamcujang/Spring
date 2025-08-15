package org.common.dbiz.dto.tenantDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.productDto.ImageDto;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.tenantservice.domain.TenantBanner}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantBannerDto implements Serializable {
    String isActive;
    Integer id;
    Integer tenantId;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    ImageDto image;
    Integer imageId;
}