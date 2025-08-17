package com.laptopstore.ecommerce.dto.review;


import com.laptopstore.ecommerce.dto.pagination.PageableDto;
import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.TypeParseUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.laptopstore.ecommerce.util.DateTimeUtils.getValidInstantRange;
import static com.laptopstore.ecommerce.util.TypeParseUtils.parseStringInstantOrNull;
import static com.laptopstore.ecommerce.util.TypeParseUtils.parseStringIntegerListOrEmpty;

@Getter
@Setter
public class ReviewFilterDto extends PageableDto {
    private List<String> starRatings;
    private String startDate;
    private String endDate;

    public Instant getInstantStartDate(){
        return parseStringInstantOrNull(startDate);
    }

    public Instant getInstantEndDate(){
        return parseStringInstantOrNull(endDate);
    }

    public List<Integer> getIntegerStarRatings(){
        return parseStringIntegerListOrEmpty(starRatings);
    }

    public int getFilterCount() {
        int count = 0;

        if(starRatings != null && !starRatings.isEmpty()) count++;
        if(startDate!= null && endDate !=null && !startDate.isEmpty() && !endDate.isEmpty()) count++;

        return count;
    }

    public String toQuery() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        if (getIntegerPage() != null && getIntegerPage() > 0) {
            builder.queryParam("page", getIntegerPage());
        }

        if (getIntegerLimit() != null && getIntegerLimit() > 0) {
            builder.queryParam("limit", getLimit());
        }

        String sortField = PaginationUtils.getValidSortBy(Review.class, getSortBy(), Review.DEFAULT_SORT_FIELD);

        if (sortField != null) {
            builder.queryParam("sortBy", sortField);
        }

        if (getEnumSortDirection() != null) {
            builder.queryParam("sortDirection", getEnumSortDirection());
        }

        if(getIntegerStarRatings() != null && !getIntegerStarRatings().isEmpty()){
            for (Integer starRating : getIntegerStarRatings()) {
                builder.queryParam("starRatings", starRating);
            }
        }

        Map<String, Instant> instantRange = getValidInstantRange(getInstantStartDate(), getInstantEndDate());

        if(!instantRange.isEmpty()){
            builder.queryParam("startDate", TypeParseUtils.parseInstantLocalDateOrNull(instantRange.get("startDate")));
            builder.queryParam("endDate", TypeParseUtils.parseInstantLocalDateOrNull(instantRange.get("endDate")));
        }

        return builder.build().encode().toUriString();
    }
}
