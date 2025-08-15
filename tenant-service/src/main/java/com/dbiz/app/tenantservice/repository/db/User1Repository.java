package com.dbiz.app.tenantservice.repository.db;

import com.dbiz.app.tenantservice.domain.db.User1;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface User1Repository extends JpaRepository<User1, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User1> findByEmail(String email);
}
