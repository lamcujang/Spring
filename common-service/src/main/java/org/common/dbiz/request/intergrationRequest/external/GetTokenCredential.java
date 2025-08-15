package org.common.dbiz.request.intergrationRequest.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenCredential {
    private String userName;
    private String password;
    private Parameters parameters;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameters{
        private Integer clientId;
        private Integer roleId;
        private String language;
    }
}
