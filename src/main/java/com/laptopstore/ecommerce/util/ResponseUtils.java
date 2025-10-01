package com.laptopstore.ecommerce.util;

import com.laptopstore.ecommerce.dto.response.AjaxResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {
    public static ResponseEntity<AjaxResponse<Object>> getAjaxErrorResponseResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            AjaxResponse<Object> ajaxResponse = new AjaxResponse<>(null, errors);

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ajaxResponse);
        }
        return null;
    }
}
