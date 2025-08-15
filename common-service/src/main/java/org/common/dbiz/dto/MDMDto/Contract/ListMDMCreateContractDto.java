package org.common.dbiz.dto.MDMDto.Contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ListMDMCreateContractDto {
    List<MDMCreateContractDto> data;
}
