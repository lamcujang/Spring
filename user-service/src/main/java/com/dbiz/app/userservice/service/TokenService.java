package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.TokenDto;
import org.common.dbiz.payload.GlobalReponse;

public interface TokenService {

    GlobalReponse save(TokenDto dto);

    TokenDto checkValidRefreshToken(TokenDto dto);

    GlobalReponse revokeRefreshToken(TokenDto dto);

}
