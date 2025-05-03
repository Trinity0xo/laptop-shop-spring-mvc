package com.laptopstore.ecommerce.util.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final SpringTemplateEngine templateEngine;

    public GlobalExceptionHandler(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @ExceptionHandler(
            Exception.class
    )
    public String handleInternalException(Exception exception, Model model) {
        model.addAttribute("code", HttpStatus.INTERNAL_SERVER_ERROR);
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Exception occurs...");
        return "/error";
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class,
            NotFoundException.class
    })
    public Object handleNoResourceFoundException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND);
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Not found");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_HTML).body(html);
        }

        return "error";
    }

    @ExceptionHandler(value = {
            ConflictException.class
    })
    public Object handleConflictException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.CONFLICT);
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Conflict");

//        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
//            Context context = new Context();
//            context.setVariables(model.asMap());
//            String html = templateEngine.process("error", context);
//
//            return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.TEXT_HTML).body(html);
//        }

        return "/error";
    }

    @ExceptionHandler(value = {
            BadRequestException.class
    })
    public String handleBadRequestException(Exception exception, Model model) {
        model.addAttribute("code", HttpStatus.BAD_REQUEST);
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Bad request");
        return "/error";
    }
}
