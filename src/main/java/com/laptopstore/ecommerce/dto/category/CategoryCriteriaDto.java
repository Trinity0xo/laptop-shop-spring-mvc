package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCriteriaDto extends PageableCriteriaDto {
    private String name;

    public String toQuery(){
        StringBuilder stringBuilder = new StringBuilder();

        if (name != null && !name.isEmpty()) {
            stringBuilder.append("&name=").append(name);
        }

        if(getIntegerPage() != null && getIntegerPage() > 0){
            stringBuilder.append("&page=").append(getPage());
        }

        if(getIntegerLimit() != null && getIntegerLimit()  > 0){
            stringBuilder.append("&limit=").append(getLimit());
        }

        if (getSortBy() != null && !getSortBy().isEmpty()) {
            stringBuilder.append("&sortBy=").append(getSortBy());
        }

        if (getEnumSortDirection() != null) {
            stringBuilder.append("&sortDirection=").append(getSortDirection());
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.setCharAt(0, '?');
        }

        return stringBuilder.toString();
    }
}