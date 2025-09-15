package com.laptopstore.ecommerce.util;

import java.time.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DateTimeUtils {
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static Instant getCurrentInstant(){
        return ZonedDateTime.now().toInstant();
    }

    public static Instant getInstantMonthsAgo(int numberOfMonths){
        return ZonedDateTime.now().minusMonths(numberOfMonths).toInstant();
    }

    public static Instant getInstantDaysAgo(int numberOfDays){
        return ZonedDateTime.now().minusDays(numberOfDays).toInstant();
    }

    public static Instant getEndOfDay(Instant instant) {
        return instant.atZone(ZONE_ID)
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay(ZONE_ID)
                .toInstant()
                .minusNanos(1);
    }

    public static Map<String, Instant> getValidInstantRange(Instant startDate, Instant endDate){
        if(startDate == null || endDate == null) {
            return Collections.emptyMap();
        }

        LocalDate startLocal = startDate.atZone(ZONE_ID).toLocalDate();
        LocalDate endLocal = endDate.atZone(ZONE_ID).toLocalDate();

        if(startLocal.isAfter(endLocal)){
            return Collections.emptyMap();
        }

        Map<String, Instant> validDateRange = new HashMap<>();

        validDateRange.put("startDate", startDate);
        validDateRange.put("endDate", getEndOfDay(endDate));

        return validDateRange;
    }
}