package com.laptopstore.ecommerce.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;

import java.util.List;

@Service
public class PageableService {
    public Pageable handleCreatePageable(int currentPage, int pageSize, String sortBy, SortDirectionEnum sortDirection, List<String> validSortBy) {
        if (!validSortBy.contains(sortBy)) {
            sortBy = validSortBy.get(0);
        }

        Sort sort = Sort.by(sortDirection.equals(SortDirectionEnum.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return PageRequest.of(currentPage - 1, pageSize, sort);
    }

    public Pageable handleCreatePageable(int currentPage, int pageSize, String sortBy, SortDirectionEnum sortDirection) {
        Sort sort = Sort.by(sortDirection.equals(SortDirectionEnum.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return PageRequest.of(currentPage - 1, pageSize, sort);
    }

    public Pageable handleCreatePageable(int currentPage, int pageSize) {
        return PageRequest.of(currentPage - 1, pageSize);
    }
}
