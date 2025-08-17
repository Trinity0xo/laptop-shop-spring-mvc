package com.laptopstore.ecommerce.dto.role;

import com.laptopstore.ecommerce.dto.pagination.PageableDto;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.util.PaginationUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class RoleFilterDto extends PageableDto {
    public String toQuery(){
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        if (getSearch() != null && !getSearch().isEmpty()) {
            builder.queryParam("search", getSearch());
        }

        if (getIntegerPage() != null && getIntegerPage() > 0) {
            builder.queryParam("page", getIntegerPage());
        }

        if (getIntegerLimit() != null && getIntegerLimit() > 0) {
            builder.queryParam("limit", getIntegerLimit());
        }

        String sortField = PaginationUtils.getValidSortBy(Role.class, getSortBy(), Role.DEFAULT_SORT_FIELD);

        if (sortField != null) {
            builder.queryParam("sortBy", sortField);
        }

        if (getEnumSortDirection() != null) {
            builder.queryParam("sortDirection", getEnumSortDirection());
        }

        return builder.build().encode().toUriString();
    }
}
