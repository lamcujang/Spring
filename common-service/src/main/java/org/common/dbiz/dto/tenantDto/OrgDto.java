package org.common.dbiz.dto.tenantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrgDto implements Serializable {
    Integer id;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    @Size(max = 512)
    String address;
    @Size(max = 64)
    String email;
    @Size(max = 100)
    String area;
    @Size(max = 100)
    String wards;
    Integer erpOrgId;
    private String description;
    private String isSummary ;
    private String phone;
    private String taxCode;
    private String isActive = "Y";
    String url;
    private Integer priceListId;
    private String isPosMng;
    String exDate;
    Integer userId;
    String role; // check role of user in org
    ImageDto image;
    private String country;
    private String erpOrgName;
}