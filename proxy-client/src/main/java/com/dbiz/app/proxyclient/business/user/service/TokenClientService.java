package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import feign.HeaderMap;
import org.common.dbiz.dto.userDto.TokenDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.Map;

@FeignClient(name = "USER-SERVICE", contextId = "tokenClientService", path = "/user-service/api/v1/token"
        , configuration = FeignClientConfig.class
)
public interface TokenClientService {

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(
//            @HeaderMap Map<String, String> headers,
            @RequestHeader("tenantId") Integer tenantId,
            @RequestBody @Valid final TokenDto dto
    );

    @PostMapping("/check")
    ResponseEntity<TokenDto> checkValidRefreshToken(
            @RequestHeader("tenantId") Integer tenantId,
            @RequestBody @Valid final TokenDto dto
    );

    @PostMapping("/revoke")
    ResponseEntity<GlobalReponse> revokeRefreshToken(
            @RequestHeader("tenantId") Integer tenantId,
            @RequestBody @Valid final TokenDto dto
    );

}
