package com.laptopstore.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {
    private int currentPage;
    private int totalPages;
    private long totalItem;
    private T data;
}
