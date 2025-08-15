package com.dbiz.app.tenantservice.common;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper<T> {
    public List<T> map(ResultSet rs, Class<T> dtoClass) throws Exception {
        List<T> result = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            T instance = dtoClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = meta.getColumnLabel(i);
                Object columnValue = rs.getObject(i);

                for (Field field : dtoClass.getDeclaredFields()) {
                    if (toCamelCase(columnLabel).equalsIgnoreCase(field.getName())) {
                        field.setAccessible(true);
                        field.set(instance, columnValue);
                        break;
                    }
                }
            }
            result.add(instance);
        }
        return result;
    }

    private String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        for (String part : input.split("_")) {
            if (result.length() == 0) {
                result.append(part.toLowerCase());
            } else {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}
