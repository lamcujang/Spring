package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Integer>, JpaSpecificationExecutor<Note> {

    Page<Note> findAll(Specification specification, Pageable pageable);

    Page<Note> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM Note p WHERE p.id = :dNoteId")
    Optional<Note> findById(Integer dNoteId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Note p WHERE p.id = :dNoteId")
    void deleteById(Integer dNoteId);

    List<Note> findAllByNoteGroupId(Integer noteGroupId);
}