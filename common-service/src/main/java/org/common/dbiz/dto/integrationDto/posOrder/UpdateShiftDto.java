package org.common.dbiz.dto.integrationDto.posOrder;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UpdateShiftDto {

    private Integer ad_Client_ID;
    private Integer ad_Org_ID;
    private Integer ad_User_ID;
    private Integer terminalID;
    private Integer D_Shift_Control_ID;
    private Integer sequenceNo;
    private String startDate;
    private String endDate;
    private String shiftType;


}
