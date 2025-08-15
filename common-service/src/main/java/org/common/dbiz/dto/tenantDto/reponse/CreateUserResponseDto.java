package com.dbiz.app.tenantservice.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserResponseDto implements Serializable {

    Long id;

    String email;
}