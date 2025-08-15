package com.dbiz.app.productservice.repository;

import com.dbiz.app.productservice.domain.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer>, JpaSpecificationExecutor<AttributeValue> {
    List<AttributeValue> findByAttributeId(final Integer attributeId);

    @Query("select a.code from AttributeValue p join Attribute a on a.id = p.attributeId where p.value = :attrValue and a.code =:attrCode and p.tenantId = :tenantId")
    String getValueByAttr(String attrValue,String attrCode,Integer tenantId);

    AttributeValue findByValueAndAttributeId(String value,Integer attributeId);
}