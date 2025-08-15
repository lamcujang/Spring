package org.common.dbiz.dto.tenantDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateOrgDto {

    ImageDto image;
    String name;
    String address;
    String tenantCode;
    String industryCode;
}
