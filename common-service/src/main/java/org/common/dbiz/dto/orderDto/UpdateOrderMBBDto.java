package org.common.dbiz.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateOrderMBBDto implements Serializable {

    String traceTransfer;
    String storeLabel;
    String terminalLabel;
    String debitAmount;
    String realAmount;
    String payDate;
    String respCode;
    String respDesc;
    String checkSum;
    String rate;
    String billNumber;
    String consumerLabelTerm;
    String referenceLabelCode;
    //	private String referenceLabel;
    String userName;
    String ftCode;
    String endPointUrl;
    //	private String additionalData;
    List<String> referenceLabel = new ArrayList<>();
    List<MBBAddData> additionalData = new ArrayList<MBBAddData>();
    String authToken;
    String isSkipCheckSum = "N";

}
