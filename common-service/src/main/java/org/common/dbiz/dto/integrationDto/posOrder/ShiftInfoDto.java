package org.common.dbiz.dto.integrationDto.posOrder;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShiftInfoDto {

    private Integer D_Shift_Control_ID;
    private Integer sequenceNo;
    private String startDate;
    private String endDate;
    private String shiftType;
}
