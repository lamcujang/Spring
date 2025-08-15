package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendNotification implements Serializable {
    String title;
    String body;
    String deviceToken;
    String type;
    List<String> deviceTokens;
    String messageValue;//
    String router;
    String speak="";
    String routerFunction;
    Integer recordId;
    String code = null;
    String status = null;
}