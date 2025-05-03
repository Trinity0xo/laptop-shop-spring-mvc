package com.laptopstore.ecommerce.dto;

import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableCriteriaDto {
    private String page = "1";
    private String limit = "10";
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

    public Integer getIntegerPage(){
        try {
            return Integer.parseInt(page);
        } catch (Exception e) {
            return 1;
        }
    }

    public Integer getIntegerLimit(){
        try {
            return Integer.parseInt(limit);
        } catch (Exception e) {
            return 10;
        }
    }

    public SortDirectionEnum getEnumSortDirection(){
        try {
            return SortDirectionEnum.valueOf(sortDirection.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SortDirectionEnum.DESC;
        }
    }
}