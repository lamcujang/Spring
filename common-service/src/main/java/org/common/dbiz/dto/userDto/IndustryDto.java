package org.common.dbiz.dto.userDto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IndustryDto implements Serializable {
    Integer id;
    String code;
    String name;
}