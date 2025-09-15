package com.laptopstore.ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final SpringTemplateEngine templateEngine;

    public GlobalExceptionHandler(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private String getRedirectUrl(HttpServletRequest request) {
        if (request.getRequestURI().contains("/dashboard")) {
            return "/dashboard";
        } else {
            return "/";
        }
    }

    @ExceptionHandler(AuthenticatedUserNotFoundException.class)
    public Object handleAuthenticatedUserNotFound(AuthenticatedUserNotFoundException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.error("Authenticated user not found. Session invalidated. Request URI: {}", request.getRequestURI(), exception);

        if(isAjax(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(HttpServletRequest request, Model model) {
        log.error("No resource found for request URI: {}", request.getRequestURI());

        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", "Rất tiếc trang bạn đang tìm kiếm không tồn tại");
        model.addAttribute("error", "Not Found");

        String redirectUrl = getRedirectUrl(request);
        model.addAttribute("redirectUrl", redirectUrl);

        return "/error";
    }

    @ExceptionHandler(NotImplementException.class)
    public Object handleNotImplementedException(NotImplementException exception, HttpServletRequest request, Model model) {
        log.error("Not Implemented: {}, request url: {}", exception.getMessage(), request.getRequestURI(), exception);

        model.addAttribute("code", HttpStatus.NOT_IMPLEMENTED.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Not Implemented");

        String redirectUrl = getRedirectUrl(request);
        model.addAttribute("redirectUrl", redirectUrl);

        return "/error";
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public Object handleCategoryNotFound(CategoryNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("Category not found with id: {}, request URI: {}", exception.getCategoryId(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Object handleProductNotFound(ProductNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("Product not found with id: {}, slug: {}, request URI: {}", exception.getProductId(), exception.getProductSlug(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public Object handleOrderNotFound(OrderNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("Order not found with id: {}, request URI: {}", exception.getOrderId(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public Object handleRoleNotFound(RoleNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("Role not found with id: {}, slug: {}, request URI: {}", exception.getRoleId(), exception.getRoleSlug(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Object handleUserNotFound(UserNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("User not found with id: {}, request URI: {}", exception.getUserId(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public Object handleReviewNotFound(ReviewNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("Review not found with id: {}, request URI: {}", exception.getReviewId(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public Object handleBrandNotFound(BrandNotFoundException exception, HttpServletRequest request, Model model) {
        log.error("Brand not found with id: {}, request URI: {}", exception.getBrandId(), request.getRequestURI(), exception);
        return buildNotFoundResponse(exception, request, model);
    }

    private Object buildNotFoundResponse(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("error", "Not Found");

        String redirectUrl = getRedirectUrl(request);
        model.addAttribute("redirectUrl", redirectUrl);

        if (isAjax(request)) {
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        return "/error";
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequestException(BadRequestException exception, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        log.error("Bad request with message: {}, request URI: {}", exception.getMessage(), request.getRequestURI(), exception);

        model.addAttribute("code", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Bad request");

        String redirectUrl = getRedirectUrl(request);
        model.addAttribute("redirectUrl", redirectUrl);

        if(isAjax(request)){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        return "/error";
    }

    @ExceptionHandler(ConflictException.class)
    public Object handleConflictException(ConflictException exception, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        log.error("Conflict with message: {}, request URI: {}", exception.getMessage(), request.getRequestURI(), exception);

        model.addAttribute("code", HttpStatus.CONFLICT.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Conflict");

        String redirectUrl = getRedirectUrl(request);
        model.addAttribute("redirectUrl", redirectUrl);

        if(isAjax(request)){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        return "/error";
    }

    @ExceptionHandler(Exception.class)
    public Object handleInternalException(Exception exception, HttpServletRequest request, Model model) {
        log.error("Internal error occurred. Exception: {}, Message: {}, Request URI: {}", exception.getClass().getSimpleName(), exception.getMessage(), request.getRequestURI(), exception);

        model.addAttribute("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message", "Có lỗi xảy ra, vui lòng thử lại sau");
        model.addAttribute("error", "Internal Error");

        String redirectUrl = getRedirectUrl(request);
        model.addAttribute("redirectUrl", redirectUrl);

        if(isAjax(request)){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        return "/error";
    }
}
