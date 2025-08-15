package org.common.dbiz.dto.MDMDto.BPartner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ListMDMCreateBPartnerDto {
    List<MDMCreateBPartnerDto> data;
}
