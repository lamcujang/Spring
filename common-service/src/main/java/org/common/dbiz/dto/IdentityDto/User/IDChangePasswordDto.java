package org.common.dbiz.dto.IdentityDto.User;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IDChangePasswordDto {

    List<String> schemas;

    @JsonProperty("Operations")
    List<IDOperations> Operations;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class IDOperations {
        private String op;
        private IDPassword value;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class IDPassword {
        private String password;
    }
}
