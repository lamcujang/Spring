package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain.Notification}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDto implements Serializable {
    String isActive;
    Integer id;
    @Size(max = 64)
    String title;
    @Size(max = 255)
    String content;
    @Size(max = 5)
    String notificationType;
    @NotNull
    @Size(max = 5)
    String status;
    String created;
    String isUpdateAll;
    Integer recordId;
    String routeFunction;
}