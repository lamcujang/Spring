package org.common.dbiz.request.intergrationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncIntegrationCredential  implements Serializable {
    Integer posOrderId;
    Integer orgId;
    String intFlow; // chieu tich hop
    String dataType;// loai du lieu tich hop
    String intType; // insert/update
    Integer fromType; // 1  2
    String date;
    String intDate;
    Integer userId;
    Integer integrationHistoryId;
    Integer tenantId;
    String status;
    String error;
    String syncKafka = "N";
    String outputFormatter = "dd-MM-yyyy";
    Integer lineProduct; // dung cho syncProduct
    Integer skipRows; // dung cho syncCoupon
    public SyncIntegrationCredential(SyncIntegrationCredential value) {

    }
}