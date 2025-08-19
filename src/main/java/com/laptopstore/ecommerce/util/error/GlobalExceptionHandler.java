package com.laptopstore.ecommerce.util.error;

import com.laptopstore.ecommerce.dto.response.AjaxResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SpringTemplateEngine templateEngine;

    public GlobalExceptionHandler(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    @ExceptionHandler(AuthenticatedUserNotFoundException.class)
    public Object handleAuthenticatedUserNotFound(AuthenticatedUserNotFoundException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if(isAjax(request)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AjaxResponse<>(exception.getMessage(), null));
        }

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        return "redirect:/auth/login";
    }

    @ExceptionHandler(
            value = {
                    ProductNotFoundException.class,
                    OrderNotFoundException.class,
                    RoleNotFoundException.class,
                    UserNotFoundException.class,
                    ReviewNotFoundException.class,
            }
    )
    public Object handleEntityNotFound(EntityNotFoundException exception, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        if(exception.hasRedirect()){
            if(isAjax(request)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new AjaxResponse<>(exception.getMessage(), exception.redirectUrl));
            }else{
                redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
                return "redirect:" + exception.redirectUrl;
            }
        }else{
            model.addAttribute("code", HttpStatus.NOT_FOUND.value());
            model.addAttribute("message", "Rất tiết trang bạn đang tìm kiếm không tồn tại");
            model.addAttribute("error", "Not Found");
            return "/error";
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", "Rất tiết trang bạn đang tìm kiếm không tồn tại");
        model.addAttribute("error", "Not Found");
        return "/error";
    }

    @ExceptionHandler(NotImplementException.class)
    public Object handleNotImplementedException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.NOT_IMPLEMENTED.value());
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("error", "Not Implemented");

        if(isAjax(request)){
            Context context = new Context();
            context.setVariables(model.asMap());
            String html = templateEngine.process("error", context);

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        return "/error";
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequestException(BadRequestException exception, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if(exception.hasRedirect()){
            if(isAjax(request)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new AjaxResponse<>(exception.getMessage(), exception.redirectUrl));
            }else{
                redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
                return "redirect:" + exception.redirectUrl;
            }
        }else {
            model.addAttribute("code", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", exception.getMessage());
            model.addAttribute("error", "Not Found");
            return "/error";
        }
    }

    @ExceptionHandler(ConflictException.class)
    public Object handleConflictException(ConflictException exception, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if(exception.hasRedirect()){
            if(isAjax(request)){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new AjaxResponse<>(exception.getMessage(), exception.redirectUrl));
            }else{
                redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
                return "redirect:" + exception.redirectUrl;
            }
        }else {
            model.addAttribute("code", HttpStatus.CONFLICT.value());
            model.addAttribute("message", exception.getMessage());
            model.addAttribute("error", "Not Found");
            return "/error";
        }
    }

    @ExceptionHandler(Exception.class)
    public Object handleInternalException(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message", "Có lỗi xảy ra, vui lòng thử lại sau");
        model.addAttribute("error", "Internal Server Error");

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
