package com.dbiz.app.userservice.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends Repository<T, ID> {
    Optional<T> findById(ID id);
}