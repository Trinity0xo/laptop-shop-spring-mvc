package com.laptopstore.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AjaxResponse<T> {
    private String message;
    private T data;
}
