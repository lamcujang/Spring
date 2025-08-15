package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.NoteGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface NoteGroupRepository extends JpaRepository<NoteGroup, Integer>, JpaSpecificationExecutor<NoteGroup> {

    Page<NoteGroup> findAll(Specification specification, Pageable pageable);

    Page<NoteGroup> findAllByTenantId(Pageable pageable,Integer tenantId);

    @Query("SELECT p FROM NoteGroup p WHERE p.id = :dNoteGroupId")
    Optional<NoteGroup> findById(Integer dNoteGroupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM NoteGroup p WHERE p.id = :dNoteGroupId")
    void deleteById(Integer dNoteGroupId);

    @Query("SELECT p FROM NoteGroup p JOIN Note n on n.noteGroupId = p.id WHERE n.productCategoryIds LIKE CONCAT('%', :groupId, '%') AND p.tenantId = :tenantId")
    Page<NoteGroup> findAllByGroupId(@Param("groupId") String groupId, @Param("tenantId") Integer tenantId, Pageable pageable);
}