package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.TokenClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.TokenDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/api/v1/token"})
@Slf4j
@RequiredArgsConstructor
public class TokenController {

    private final TokenClientService tokenClientService;

//    @PostMapping("/save")
//    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final TokenDto dto) {
//        log.info("*** TokenDto, controller; save token ***");
//        return ResponseEntity.ok(this.tokenClientService.save(dto).getBody());
//    }
//
//    @GetMapping("/check")
//    public ResponseEntity<TokenDto> checkValidRefreshToken(@RequestBody @Valid final TokenDto dto) {
//        log.info("*** TokenDto, controller; check refresh token valid ***");
//        return ResponseEntity.ok(this.tokenClientService.checkValidRefreshToken(dto).getBody());
//    }
//
//    @PostMapping("/revoke")
//    public ResponseEntity<GlobalReponse> revokeRefreshToken(@RequestBody @Valid final TokenDto dto) {
//        log.info("*** TokenDto, controller; revoke refresh token ***");
//        return ResponseEntity.ok(this.tokenClientService.revokeRefreshToken(dto).getBody());
//    }

}
