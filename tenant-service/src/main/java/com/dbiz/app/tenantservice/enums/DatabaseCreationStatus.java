package com.dbiz.app.tenantservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum DatabaseCreationStatus {

    IN_PROGRESS("IN_PROGRESS"),
    CREATED("SUCCESS"),
    NOT_CREATED("NOT_CREATED"),
    FAILED("FAILED"),
    FAILED_TO_CREATE("FAILED_TO_CREATE");

    String value;
}
