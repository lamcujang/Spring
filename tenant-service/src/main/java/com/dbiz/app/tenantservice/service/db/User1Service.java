package com.dbiz.app.tenantservice.service.db;

import com.dbiz.app.tenantservice.domain.db.Tenant1;
import com.dbiz.app.tenantservice.domain.db.User1;
import com.dbiz.app.tenantservice.dto.reponse.CreateUserResponseDto;
import com.dbiz.app.tenantservice.dto.reponse.UserAuthShortDto;
import com.dbiz.app.tenantservice.dto.request.CreateUserRequestDto;
import com.dbiz.app.tenantservice.enums.Role;
import com.dbiz.app.tenantservice.mapper.UserMapper;
import com.dbiz.app.tenantservice.repository.db.User1Repository;
import com.dbiz.app.tenantservice.repository.db.dao.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.common.dbiz.exception.wrapper.ForbiddenException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@Service
@DependsOn("dataSourceRouting")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class User1Service {

    @NonFinal
    @Value("${jwt.secret}")
    String jwtSecret;

    UserDao userDao;
    Tenant1Service tenantService;
    User1Repository userRepository;

    public User1Service(UserDao userDao,
                        @Lazy Tenant1Service tenantService,
                        User1Repository userRepository) {
        this.userDao = userDao;
        this.tenantService = tenantService;
        this.userRepository = userRepository;
    }

    public User1 getByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(format("User with email - %s does not exist. ", email));
                    return new ObjectNotFoundException(format("User with email - %s does not exist. ", email));
                });
    }

    public CreateUserResponseDto create(CreateUserRequestDto requestDto) {

        Tenant1 tenant = tenantService.getById(requestDto.getTenantId());

        return create(requestDto, tenant, requestDto.getRoles());
    }

    public CreateUserResponseDto create(CreateUserRequestDto requestDto, Tenant1 tenant, List<Role> roles) {

        User1 user = UserMapper.INSTANCE.fromRequestDto(requestDto);

        user.setRoles(roles);
        user.setTenant(tenant);
        user.setPassword(bcryptPassword(requestDto.getPassword()));

        user = userRepository.saveAndFlush(user);

        return UserMapper.INSTANCE.toResponseDto(user);
    }

    public User1 create2(CreateUserRequestDto requestDto, Tenant1 tenant, List<Role> roles) {

        User1 user = UserMapper.INSTANCE.fromRequestDto(requestDto);

        user.setRoles(roles);
        user.setTenant(tenant);
        user.setPassword(bcryptPassword(requestDto.getPassword()));

        user = userRepository.saveAndFlush(user);

        return user;
    }

    private String bcryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Transactional
    public UserAuthShortDto getActualUser(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {

            try {
                String claims = token.replace("Bearer ", StringUtils.EMPTY);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret.getBytes())
                        .build()
                        .parseClaimsJws(claims);

                Long userId = Long.parseLong(claimsJws.getBody().getSubject());
                UserAuthShortDto user = userDao.getAuthShortDtoByUserId(userId);

                if (user.getRoles() == null || user.getRoles().isEmpty()) {

                    throw new ForbiddenException("Access denied");
                }

                return user;

            } catch (Exception e) {

                log.error("Exception occurred while 'getActualUser' execution: " + e.getMessage());

                throw new ForbiddenException("Access denied");
            }
        }
        throw new ForbiddenException("Access denied");
    }
}
