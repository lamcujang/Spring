package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.RoleDto;
import org.common.dbiz.dto.userDto.TokenDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/api/v1/token"})
@Slf4j
@RequiredArgsConstructor
public class TokenResource {

    private final TokenService tokenService;

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final TokenDto dto) {
        log.info("*** TokenDto, resource; save token ***");
        return ResponseEntity.ok(this.tokenService.save(dto));
    }

    @PostMapping("/check")
    public ResponseEntity<TokenDto> checkValidRefreshToken(@RequestBody @Valid final TokenDto dto) {
        log.info("*** TokenDto, resource; check refresh token valid ***");
        return ResponseEntity.ok(this.tokenService.checkValidRefreshToken(dto));
    }

    @PostMapping("/revoke")
    public ResponseEntity<GlobalReponse> revokeRefreshToken(@RequestBody @Valid final TokenDto dto) {
        log.info("*** TokenDto, resource; revoke refresh token ***");
        return ResponseEntity.ok(this.tokenService.revokeRefreshToken(dto));
    }

}
