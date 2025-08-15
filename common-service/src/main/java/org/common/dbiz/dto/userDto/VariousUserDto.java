package org.common.dbiz.dto.userDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariousUserDto {

    Integer id;
    String code;
    String name;
    String phone;
    String address;
    String city;
    String wards;
    String description;
    String userGroup;
}
