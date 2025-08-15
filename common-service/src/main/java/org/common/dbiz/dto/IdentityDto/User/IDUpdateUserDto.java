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
public class IDUpdateUserDto {

    List<String> emails;
    List<Object> schemas;
    String userName;
    List<IDPhoneListDto> phoneNumbers;
    String full_name;
//    String googleid;
//    String zaloid;
//    String facebookid;

    @JsonProperty("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")
    private SCIMUserExtension scimUserExtension;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class SCIMUserExtension {
        private String googleid;
        private String zaloid;
        private String facebookid;
        private String tiktokid;
    }

}
