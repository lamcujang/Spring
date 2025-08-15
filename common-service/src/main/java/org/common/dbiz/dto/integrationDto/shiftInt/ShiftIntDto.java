package org.common.dbiz.dto.integrationDto.shiftInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShiftIntDto implements Serializable {

	private Integer tenantId;
	private Integer orgId;
	private Integer shiftId;
	private Integer userId;
}










