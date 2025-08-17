package com.laptopstore.ecommerce.dto.pagination;

import com.laptopstore.ecommerce.util.TypeParseUtils;
import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableDto {
    private String page;
    private String limit;
    private String sortBy;
    private String sortDirection;
    private String search;

    public Long getLongSearch(){
       return TypeParseUtils.parseStringLongOrDefault(search, -1L);
    }

    public Integer getIntegerPage(){
        return TypeParseUtils.parseStringIntegerOrDefault(page, 1);
    }

    public Integer getIntegerLimit(){
       return TypeParseUtils.parseStringIntegerOrDefault(limit, 10);
    }

    public SortDirectionEnum getEnumSortDirection(){
        return TypeParseUtils.parseStringEnumOrDefault(SortDirectionEnum.class, sortDirection, SortDirectionEnum.DESC);
    }
}