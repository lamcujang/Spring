package org.common.dbiz.request.externalRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveImageMDMRequest {
    private String branchCode;
    private String tenantCode;
    private String imageCode;
    private String image64;

    @Override
    public String toString() {
        return "SaveImageMDMRequest{" +
                "branchCode='" + branchCode + '\'' +
                ", tenantCode='" + tenantCode + '\'' +
                ", imageCode='" + imageCode + '\'' +
                ", image64='" + image64 + '\'' +
                '}';
    }
}
