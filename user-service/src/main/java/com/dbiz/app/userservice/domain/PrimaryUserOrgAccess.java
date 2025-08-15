package com.dbiz.app.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrimaryUserOrgAccess implements Serializable {
    private Integer userId;
    private Integer orgId;
}
