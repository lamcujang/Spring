package com.dbiz.app.tenantservice.service.dao_holder;


import com.dbiz.app.tenantservice.repository.db.dao.TenantDao;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class TenantDaoHolder extends AbstractDaoHolder {

    @Override
    public void afterSingletonsInstantiated() {

        templates = new HashMap<>();
    }

    public TenantDao getTemplateByTenantKey(Long tenantKey) {

        return templates.get(tenantKey);
    }

    public void addNewTemplates(Map<Object, Object> dataSources) {

        dataSources.forEach((key, value) -> {

            TenantDao tenantDao = new TenantDao((DataSource) value);

            templates.putIfAbsent((Long) key, tenantDao);
        });
    }
}
