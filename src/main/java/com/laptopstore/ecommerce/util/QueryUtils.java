package com.laptopstore.ecommerce.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class QueryUtils {
    public static String modifiedQuery(String query, Map<String, String> queryParams){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(query);

        for(String paramName : queryParams.keySet()){
            String paramValue = queryParams.get(paramName);
            if(paramValue == null || paramValue.isEmpty()){
                builder.replaceQueryParam(paramName);
            }else{
                builder.replaceQueryParam(paramName, paramValue);
            }
        }

        return builder.build().encode().toUriString();
    }
}
