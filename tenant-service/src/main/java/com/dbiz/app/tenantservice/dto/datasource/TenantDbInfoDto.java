package com.dbiz.app.tenantservice.dto.datasource;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantDbInfoDto implements Serializable {

    Long id;

    String dbName;

    String userName;

    String dbPassword;
}
