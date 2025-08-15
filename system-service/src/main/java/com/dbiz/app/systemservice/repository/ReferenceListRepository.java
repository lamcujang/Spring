package com.dbiz.app.systemservice.repository;

import com.dbiz.app.systemservice.domain.ReferenceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReferenceListRepository extends JpaRepository<ReferenceList, Integer>, JpaSpecificationExecutor<ReferenceList> {
    ReferenceList findByValue(String value);

    @Query("SELECT r FROM ReferenceList r join TableUseRef  tb on r.referenceId = tb.referenceId  WHERE r.value = :value AND tb.domainName = :domain AND tb.domainColumn = :column")
    ReferenceList findByValueAndDomain(@Param("value") String value,@Param("domain") String domain, @Param("column") String column);
}