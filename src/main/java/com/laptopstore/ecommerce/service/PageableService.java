package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;

@Service
public class PageableService {
    public Pageable handleCreatePageable(int currentPage, int pageSize, String sortBy, SortDirectionEnum sortDirection) {
        Sort sort = Sort.by(sortDirection.equals(SortDirectionEnum.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return PageRequest.of(currentPage - 1, pageSize, sort);
    }
}
