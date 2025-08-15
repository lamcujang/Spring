package org.common.dbiz.dto.userDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariousUserParamDto {

    Integer id;
    String code;
    String name;
    String phone;
    String userGroup;
    private int page=0;
    private int pageSize=15;
    private String order="desc";
    private String sortBy="name";

}
