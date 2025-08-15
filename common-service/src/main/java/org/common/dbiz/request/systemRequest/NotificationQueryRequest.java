package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class NotificationQueryRequest extends BaseQueryRequest {
    String status;
    String type;

}