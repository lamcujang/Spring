package com.dbiz.app.proxyclient.jwt.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private int dTenantId;
    private String username;
    private String password;
    private int userId;
    private Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(int dTenantId, String username, String password, int userId, Collection<? extends GrantedAuthority> authorities) {
        this.dTenantId = dTenantId;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.authorities = authorities;
    }

    public int getdTenantId() {
        return dTenantId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
