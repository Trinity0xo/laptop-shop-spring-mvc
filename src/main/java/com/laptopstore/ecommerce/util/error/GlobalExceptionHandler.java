package com.laptopstore.ecommerce.util.error;

import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final SpringTemplateEngine templateEngine;

    public GlobalExceptionHandler(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @ExceptionHandler( value = {
            Exception.class
        }
    )
    public Object handleInternalException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message", "Có lỗi xảy ra, vui lòng thử lại sau");
        model.addAttribute("error", "Internal Server Error");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_HTML).body(html);
        }

        return "/error";
    }

    @ExceptionHandler(ExecutionControl.NotImplementedException.class)
    public Object handleNotImplementedException(ExecutionControl.NotImplementedException exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_IMPLEMENTED.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Not Implemented");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        return "/error";
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class,
            NotFoundException.class
    })
    public Object handleNoResourceFoundException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Not Found");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_HTML).body(html);
        }

        return "/error";
    }

    @ExceptionHandler(value = {
            NotImplementException.class
    })
    public Object handleNotImplementedException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_IMPLEMENTED.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Not Implemented");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_HTML).body(html);
        }

        return "/error";
    }

    @ExceptionHandler(value = {
            ConflictException.class
    })
    public Object handleConflictException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.CONFLICT.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Conflict");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.TEXT_HTML).body(html);
        }

        return "/error";
    }

    @ExceptionHandler(value = {
            BadRequestException.class,
            FileUploadException.class
    })
    public Object handleBadRequestException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Bad Request");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.TEXT_HTML).body(html);
        }

        return "/error";
    }
}
