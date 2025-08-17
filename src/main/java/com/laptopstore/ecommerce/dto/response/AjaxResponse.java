package com.laptopstore.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AjaxResponse<T> {
    private String message;
    private T data;
}
