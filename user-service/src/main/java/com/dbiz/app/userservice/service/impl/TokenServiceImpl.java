package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.userservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.Token;
import com.dbiz.app.userservice.repository.TokenRepository;
import com.dbiz.app.userservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.TokenDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;
    private final DataSourceContextHolder dataSourceContextHolder;

    @Override
    public GlobalReponse save(TokenDto dto) {

        Token token = tokenRepository.findByRefreshToken(dto.getRefreshToken()); // should be null
        if (token != null) { // collision (very unlikely)
            throw new PosException("Token already exists");
            // could retry generate?
        } else {
            List<Token> existingTokens = tokenRepository.findByUserId(dto.getUserId()); // nếu đã tồn tại token khác thuộc cùng userId (login lại máy khác/ngày khác)
            tokenRepository.deleteAll(existingTokens);
            token = modelMapper.map(dto, Token.class);
            token.setTenantId(0);
            token.setIsRevoked("N");

            if (dto.getIssued() != null && dto.getExpireAt() != null) {

//                // Pass ISO_INSTANT String
//                token.setIssued( Instant.parse(dto.getIssued()) );
//                token.setExpireAt( Instant.parse(dto.getExpireAt()) );

                // Pass ISO_LOCAL_DATE_TIME String
                token.setIssued(LocalDateTime.parse(dto.getIssued())
                        .atZone(ZoneOffset.UTC)
                        .toInstant()
                );
                token.setExpireAt(LocalDateTime.parse(dto.getExpireAt())
                        .atZone(ZoneOffset.UTC)
                        .toInstant()
                );
            }

            token = tokenRepository.save(token);
        }

        return GlobalReponse.builder()
                .message("Save token successfully")
                .data(modelMapper.map(token, TokenDto.class))
                .build();

    }

    @Override
    public TokenDto checkValidRefreshToken(TokenDto dto) {

        Token token = tokenRepository.findByRefreshToken(dto.getRefreshToken());
        TokenDto tokenDto = TokenDto.builder().isValid("Y").build();

        if ( token == null || "Y".equals(token.getIsRevoked()) ) {
            tokenDto.setIsValid("N");
        }

        return tokenDto;

    }

    @Override
    public GlobalReponse revokeRefreshToken(TokenDto dto) {

        // delete
        Token token = tokenRepository.findByRefreshToken(dto.getRefreshToken());
        tokenRepository.delete(token);

//        // set revoked
//        Token token = tokenRepository.findByRefreshToken(dto.getRefreshToken());
//        token.setIsRevoked("N");
//        tokenRepository.save(token);

        return GlobalReponse.builder()
                .message("Revoke token successfully")
                .data(modelMapper.map(token, TokenDto.class))
                .build();

    }

}
