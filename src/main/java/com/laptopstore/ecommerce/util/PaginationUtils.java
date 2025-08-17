package com.laptopstore.ecommerce.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;

import java.lang.reflect.Field;
import java.util.List;

public class PaginationUtils {
    public static String getValidSortBy(List<String> validSortFields, String sortBy, String defaultField) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return defaultField;
        }

        for (String field : validSortFields) {
            if (sortBy.trim().equalsIgnoreCase(field)) {
                return field;
            }
        }

        return defaultField;
    }

    public static String getValidSortBy(Class<?> sortClass, String sortBy, String defaultField) {
        Field[] fields = sortClass.getDeclaredFields();

        if (sortBy == null || sortBy.trim().isEmpty()) {
            return defaultField;
        }

        for (Field field : fields) {
            if (sortBy.trim().equalsIgnoreCase(field.getName())) {
                return field.getName();
            }
        }

        return defaultField;
    }

    public static Pageable createPageable(int currentPage, int pageSize, String sortBy, SortDirectionEnum sortDirection) {
        Sort sort = Sort.by(sortDirection.equals(SortDirectionEnum.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return PageRequest.of(currentPage - 1, pageSize, sort);
    }

    public static Pageable createPageable(int currentPage, int pageSize) {
        return PageRequest.of(currentPage - 1, pageSize);
    }

    public static Pageable createPageable(int pageSize, String sortBy, SortDirectionEnum sortDirection){
        Sort sort = Sort.by(sortDirection.equals(SortDirectionEnum.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return PageRequest.of(0, pageSize, sort);
    }

    public static Pageable createPageable(int pageSize){
        return PageRequest.of(0, pageSize);
    }
}
