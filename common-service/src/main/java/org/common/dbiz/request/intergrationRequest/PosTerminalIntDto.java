package org.common.dbiz.request.intergrationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PosTerminalIntDto {
    List<PosTerminalDto> listPosterminalDto;
    String type;
}
