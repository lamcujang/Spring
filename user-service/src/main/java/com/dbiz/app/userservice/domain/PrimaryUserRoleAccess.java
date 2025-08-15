package com.dbiz.app.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrimaryUserRoleAccess implements Serializable {
    private Integer userId;
    private Integer roleId;
}
