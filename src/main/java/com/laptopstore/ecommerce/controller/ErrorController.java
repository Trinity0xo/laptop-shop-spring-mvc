package com.laptopstore.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/forbidden")
    public String showForbiddenPage() {
        return "error";
    }
}
