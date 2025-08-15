package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link }
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto implements Serializable {
    Integer id;
    String imageUrl;
    String imageCode; // truyen imageCode rong hoac code de update hoac insert
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    String image64;

    String tenantCode;
    String IndustryCode;
    private String isActive;


}