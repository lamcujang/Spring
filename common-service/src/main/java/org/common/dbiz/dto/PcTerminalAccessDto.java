package org.common.dbiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class PcTerminalAccessDto implements Serializable {
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    Integer posTerminalId;
    Integer erpPcTerminalAccessId;
    Integer productCategoryId;

    String name;
    String code;
    String address;
    String phone;
    String isAssign;
    String area;
}