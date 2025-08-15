package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.userDto.jsonView.JsonViewUserDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.UserOrgAccess}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserOrgWarehouseAccessIntDto implements Serializable {
   Integer warehouseId;
   Integer userId;
   Integer orgId;
   String isActive;


}