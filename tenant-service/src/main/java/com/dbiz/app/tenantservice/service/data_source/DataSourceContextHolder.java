package com.dbiz.app.tenantservice.service.data_source;

import com.dbiz.app.tenantservice.dto.reponse.UserAuthShortDto;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataSourceContextHolder {


    @NonFinal
    static ThreadLocal<Long> currentTenantId = new ThreadLocal<>();

    static Long DEFAULT_TENANT_ID = null;


    public static void setCurrentTenantId(Long tenantId) {

        currentTenantId.set(tenantId);
    }

    public static Long getCurrentTenantId() {

        return currentTenantId.get();
    }
    public static void clear() {
        log.debug("Clearing tenant context");
        currentTenantId.remove();
    }

}
