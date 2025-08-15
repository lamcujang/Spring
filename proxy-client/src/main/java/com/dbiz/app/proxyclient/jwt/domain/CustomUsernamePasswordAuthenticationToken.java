package com.dbiz.app.proxyclient.jwt.domain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Integer dTenantId;

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Integer dTenantId) {
        super(principal, credentials);
        this.dTenantId = dTenantId;
    }

    public Integer getdTenantId() {
        return dTenantId;
    }
}