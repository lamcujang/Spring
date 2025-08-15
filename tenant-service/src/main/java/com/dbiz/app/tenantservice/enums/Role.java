package com.dbiz.app.tenantservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Role {

    SYS_ADMIN("SYS_ADMIN"),
    ADMIN("ADMIN"),
    USER("USER");

    String value;
}
