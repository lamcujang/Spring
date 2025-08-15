package com.dbiz.app.userservice.repository;

import com.dbiz.app.userservice.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByRefreshToken(String refreshToken);

    List<Token> findByUserId(Integer userId);
}
