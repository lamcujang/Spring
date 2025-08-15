package org.common.dbiz.dto.tenantDto.reponse;



import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantResponseDto implements Serializable {

    Long id;

    Long userId;

    String name;

    String dbName;

//    DatabaseCreationStatus creationStatus;
}