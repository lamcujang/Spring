package com.dbiz.app.proxyclient.config.client;

import com.dbiz.app.proxyclient.jwt.util.JwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Configuration
public class FeignClientConfig {
    private final JwtUtil jwtUtil;

    public FeignClientConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes != null) {
//                    HttpServletRequest request = attributes.getRequest();
                    String authorizationToken = attributes.getRequest().getHeader("Authorization");
                    if (authorizationToken != null) {
                        authorizationToken.startsWith("Bearer ");
                       String token =  authorizationToken.substring(7);
                       template.header("userId", jwtUtil.extractDuserID(token).toString());
                        template.header("createBy", jwtUtil.extractUsername(token));
                        template.header("updateBy", jwtUtil.extractUsername(token));
                        template.header("orgId", jwtUtil.extractdOrgId(token).toString());
                        template.header("tenantId", jwtUtil.extractdTenantId(token).toString());
                        template.header("Accept-Language", jwtUtil.extractLanguage(token));
                    }
                }

            }
        };
    }

}