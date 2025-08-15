package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, JpaSpecificationExecutor<Notification> {

    @Modifying
    @Query("update Notification n set n.status = ?1 where n.status = ?2")
    void updateAllByStatus(String status, String status2);

    @Modifying
    @Query("update Notification n set n.status = ?1 where n.id = ?2")
    void updateStatusById(String status, Integer id);
}