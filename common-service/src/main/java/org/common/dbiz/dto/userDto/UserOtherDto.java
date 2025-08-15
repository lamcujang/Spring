package org.common.dbiz.dto.userDto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOtherDto {

    Integer id;
    String code;
    String name;
    String phone;
    String address;
    String city;
    String wards;
    String isActive;
    String description;
}
