package com.dbiz.app.tenantservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTenantRequestDto implements Serializable {

    @NotBlank
    String name;

    @NotBlank
    String dbName;

    @NotBlank
    String userName;

    @NotBlank
    String dbPassword;

    @NotBlank
    CreateUserRequestDto user;
}
