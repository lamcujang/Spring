package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.dbiz.app.domain.Vendor}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorDto implements Serializable {
    Integer id;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    @Size(max = 15)
    String phone1;
    @Size(max = 15)
    String phone2;
    @Size(max = 255)
    String address1;
    @Size(max = 255)
    String address2;
    @Size(max = 15)
    String taxCode;
    @Size(max = 64)
    String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthday;
    ImageDto image;
    BigDecimal debitAmount;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
     String isActive = "Y";
    Integer partnerGroupId;
    PartnerGroupDto partnerGroup;
    String description;
    String area;
    String wards;
    BigDecimal totalTransactionAmount;
    private Integer erpVendorId;
    String isPosVip;
    private String partnerName;
    private BigDecimal disCount;
    private BigDecimal creditLimit;
    Integer[] ids;
    String isDebt;
    private String company;

}