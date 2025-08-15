package com.dbiz.app.tenantservice.config.audit;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.springframework.stereotype.*;

import javax.annotation.PostConstruct;
import javax.persistence.*;

@Component
public class HibernateStartupCheck {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void checkInterceptor() {
        Session session = entityManager.unwrap(Session.class);
        Interceptor interceptor = session.getSessionFactory().getSessionFactoryOptions().getInterceptor();

        System.out.println("✅ Interceptor hiện tại: " + interceptor.getClass().getName());
    }
}


