package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserIntKafkaDto implements Serializable {
	Integer tenantId;
	String status;
	String error;
	String lastPage;
	String platForm;
	List<UserIntDto> userIntDto;



}










