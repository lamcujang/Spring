package org.common.dbiz.dto.orderDto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer userId;

	private String fullName;
	
	private String email;
	
	private String phone;

}










