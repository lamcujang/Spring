package com.dbiz.app.tenantservice.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthShortDto implements Serializable {

    Long tenantId;

    List<String> roles;
}