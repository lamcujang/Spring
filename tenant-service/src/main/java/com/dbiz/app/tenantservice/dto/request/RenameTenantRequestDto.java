package com.dbiz.app.tenantservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenameTenantRequestDto implements Serializable {

    Long id;

    String name;
}
