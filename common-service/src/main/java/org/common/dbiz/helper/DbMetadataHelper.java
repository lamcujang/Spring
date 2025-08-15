package org.common.dbiz.helper;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.lang.reflect.Field;

@Slf4j
public final class DbMetadataHelper {

    // Lấy tên bảng từ entity sử dụng annotation @Table
    public static String getTableName(Object entity) {
        try {
            Table tableAnnotation = entity.getClass().getAnnotation(Table.class);
            if (tableAnnotation != null && tableAnnotation.name() != null && !tableAnnotation.name().isEmpty()) {
                return tableAnnotation.name();
            }
            log.warn("Không tìm thấy annotation @Table cho entity: {}, sử dụng tên lớp làm dự phòng", entity.getClass().getSimpleName());
            return entity.getClass().getSimpleName();
        } catch (Exception e) {
            log.error("Lỗi khi lấy tên bảng cho entity: {}, sử dụng tên lớp làm dự phòng. Lỗi: {}",
                    entity.getClass().getSimpleName(), e.getMessage());
            return entity.getClass().getSimpleName();
        }
    }

    // Lấy tên cột từ tên thuộc tính sử dụng annotation @Column hoặc @JoinColumn
    public static String getColumnName(Object entity, String propertyName) {
        try {
            // Tìm field tương ứng với propertyName
            Field field = findField(entity.getClass(), propertyName);
            if (field != null) {
                // Kiểm tra annotation @Column
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null && columnAnnotation.name() != null && !columnAnnotation.name().isEmpty()) {
                    return columnAnnotation.name();
                }
                // Kiểm tra annotation @JoinColumn (cho các mối quan hệ như @ManyToOne)
                JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
                if (joinColumnAnnotation != null && joinColumnAnnotation.name() != null && !joinColumnAnnotation.name().isEmpty()) {
                    return joinColumnAnnotation.name();
                }
            }
            log.warn("Không tìm thấy annotation @Column hoặc @JoinColumn cho thuộc tính {} của entity {}, sử dụng tên thuộc tính làm dự phòng",
                    propertyName, entity.getClass().getSimpleName());
            return propertyName;
        } catch (Exception e) {
            log.error("Lỗi khi lấy tên cột cho thuộc tính {} của entity {}. Lỗi: {}",
                    propertyName, entity.getClass().getSimpleName(), e.getMessage());
            return propertyName;
        }
    }

    // Tìm field trong class hoặc các superclass
    private static Field findField(Class<?> clazz, String propertyName) {
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(propertyName);
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Kiểm tra trong superclass
            }
        }
        return null; // Không tìm thấy field
    }

}
