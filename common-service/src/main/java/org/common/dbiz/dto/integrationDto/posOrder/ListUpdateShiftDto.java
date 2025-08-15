package org.common.dbiz.dto.integrationDto.posOrder;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ListUpdateShiftDto {

    private List<UpdateShiftDto> data;

}
