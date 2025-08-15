package com.dbiz.app.tenantservice.dto.request;


import com.dbiz.app.tenantservice.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequestDto implements Serializable {

    Long tenantId;

    String email;

    String password;

    List<Role> roles;
}
