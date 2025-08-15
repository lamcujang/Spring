package org.common.dbiz.dto.systemDto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConfigDto implements Serializable {
    String isActive;
    BigDecimal id;
    String name;
    String value;
    String description;
}