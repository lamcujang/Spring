package org.common.dbiz.dto.paymentDto.napas;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NapasResponseDto {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    String code;
    String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String description;
}
