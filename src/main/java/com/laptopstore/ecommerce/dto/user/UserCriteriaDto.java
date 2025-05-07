package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCriteriaDto extends PageableCriteriaDto {
    private String email;

    public String toQuery(){
        StringBuilder stringBuilder = new StringBuilder();

        if (email != null && !email.isEmpty()) {
            stringBuilder.append("&email=").append(email);
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
