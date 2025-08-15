package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.view.UserKcV}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserKcVDto implements Serializable {
    Integer id;
    @Size(max = 128)
    String fullName;
    @Size(max = 64)
    String userName;
}