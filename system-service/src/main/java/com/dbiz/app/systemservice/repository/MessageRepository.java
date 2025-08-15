package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageRepository extends JpaRepository<Message, Integer>, JpaSpecificationExecutor<Message> {
    Message findByValue(String value);
}