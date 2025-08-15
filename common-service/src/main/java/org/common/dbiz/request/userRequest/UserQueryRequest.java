package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseQueryRequest {
  String keyword;
  Integer roleId;
  Integer currentUserId;
  String isActive;
  Integer orgId;
  Integer roleSearch;
  Integer userId;

}