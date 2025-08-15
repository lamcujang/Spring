package com.dbiz.app.orderservice.repository;

import com.dbiz.app.orderservice.domain.PosOrderline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PosOrderlineRepository extends JpaRepository<PosOrderline, Integer>, JpaSpecificationExecutor<PosOrderline> {
    @Query("select p from PosOrderline p where p.posOrderId = :posOrderId and p.isActive ='Y'")
    List<PosOrderline> findByPosOrderId(final Integer posOrderId);

    @Transactional
    @Modifying
    void deleteAllByPosOrderId(final Integer posOrderId);

    List<PosOrderline> findAllByParentId(final Integer parentId);

    @Transactional
    @Modifying
    @Query(value = "Update d_pos_orderline Set is_active = 'N' where d_pos_order_id = :posOrderId and is_active ='Y'", nativeQuery = true)
    void cancelAllLineByPosOrderId(final Integer posOrderId);
}