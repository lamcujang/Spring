package com.dbiz.app.productservice.helper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
@RequiredArgsConstructor
public class Helper {
    public final EntityManager entityManager;


    public Integer getSequence(String nameSequence)
    {
        Query query = entityManager.createNativeQuery("SELECT  nextval('pos."+nameSequence+"')");
        return ((Number) query.getSingleResult()).intValue();
    }
}
