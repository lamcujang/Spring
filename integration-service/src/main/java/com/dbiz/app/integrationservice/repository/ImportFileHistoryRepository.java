package com.dbiz.app.integrationservice.repository;

import com.dbiz.app.integrationservice.domain.ImportFileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImportFileHistoryRepository extends JpaRepository<ImportFileHistory, Integer> {

    @Query("select coalesce(max(p.id),999999) from ImportFileHistory p")
    Integer getMaxId();


    Optional<ImportFileHistory> findByObjectTypeAndImportStatus(String objectType,String importStatus);
}