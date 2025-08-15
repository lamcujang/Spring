package com.dbiz.app.tenantservice.repository.db.dao;

import com.dbiz.app.tenantservice.dto.reponse.UserAuthShortDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDao extends AbstractDao {

    @Autowired
    public UserDao(@Qualifier("mainDataSource") DataSource dataSource) {
        super(dataSource);
    }

    public UserAuthShortDto getAuthShortDtoByUserId(Long userId) {

        UserAuthShortDto dto = new UserAuthShortDto();

        String queryForTenantKey =
            "select u.tenant_id" +
            "from users u" +
            "where u.id = :userId" ;

        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

        Long tenantId = namedParameterJdbcTemplate.queryForObject(queryForTenantKey, params, (rs, rowNum)
                -> rs.getLong("tenant_id"));

        dto.setTenantId(tenantId);
        dto.setRoles(getAuthoritiesByUserId(userId));

        return dto;
    }

    public List<String> getAuthoritiesByUserId(Long userId) {

        String queryForAuthorities =
                        "select ur.role\n" +
                        "from user_roles ur\n" +
                                "where ur.user_id = :userId\n";


        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

        return namedParameterJdbcTemplate.query(queryForAuthorities, params,
                (rs, rowNum) -> rs.getString("role"));
    }
}
