package com.dbiz.app.orderservice.helper;

import lombok.RequiredArgsConstructor;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class Helper {
    private final EntityManager entityManager;
    private List<Map<String, Object>> excuteQuery(String sql, Map<String, Object> parameters) {
        Query query = entityManager.createNativeQuery(sql);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> results = query.getResultList();
        return results;
    }
}
