package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.order.CancelOrderInformationDto;
import com.laptopstore.ecommerce.dto.order.OrderFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.user.UserInformationDto;
import com.laptopstore.ecommerce.dto.user.UpdateUserInformationDto;
import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import com.laptopstore.ecommerce.util.error.NotImplementException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final OrderService orderService;

    public AccountController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/information")
    public String showAccountInformationPage(
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        UserInformationDto accountInfo = this.userService.getUserAccountInformation(email);
        model.addAttribute("accountInfo", accountInfo);

        return "/client/account/account_info";
    }

    @GetMapping("/information/update")
    public String showAccountInformationUpdatePage(
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        UpdateUserInformationDto updateAccountInfo = this.userService.getUserAccountInformationForUpdate(email);
        model.addAttribute("updateAccountInfo", updateAccountInfo);

        return "/client/account/update_account_info";
    }

    @PostMapping("/information/update")
    public String updateAccountInformation(
            @Valid UpdateUserInformationDto updateAccountInfoDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    )  {
        if (bindingResult.hasErrors()) {
            return "/client/account/update_account_info";
        }

        String email = AuthenticationUtils.getAuthenticatedName();

        this.userService.updateUserAccountInformation(email, updateAccountInfoDto);
        redirectAttributes.addFlashAttribute("successMessage", "Thông tin tài khoản đã được cập nhật thành công.");

        return "redirect:/account/information";
    }

    @GetMapping("/order-history")
    public String showOrderHistoryPage(
            OrderFilterDto orderFilterDto,
            Model model
    ) {
        String email = AuthenticationUtils.getAuthenticatedName();

        PageResponse<List<Order>> response = this.orderService.getUserOrderHistory(email, orderFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("orderCriteriaDto", orderFilterDto);

        return "/client/account/order_history";
    }

    @GetMapping("/order-history/details/{orderId}")
    public String showUserOrderDetailsPage(
            @PathVariable Long orderId,
            Model model
    ) {
        String email = AuthenticationUtils.getAuthenticatedName();

        Order order = this.orderService.getUserOrderItems(email, orderId);
        model.addAttribute("order", order);

        return "/client/account/order_details";
    }

    @GetMapping("/order-history/cancel/{orderId}")
    public String showCancelOrderPage(
            @PathVariable Long orderId,
            Model model
    ) {
        String email = AuthenticationUtils.getAuthenticatedName();

        CancelOrderInformationDto cancelOrderInfo = this.orderService.getInformationForUserCancelOrder(email, orderId);
        model.addAttribute("cancelOrderInfo", cancelOrderInfo);

        return "/client/account/cancel_order";
    }

    @PostMapping("/order-history/cancel")
    public String cancelOrder(
            @Valid CancelOrderInformationDto cancelOrderInformationDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    )  {
        if(bindingResult.hasErrors()){
            return "/client/account/cancel_order";
        }

        String email = AuthenticationUtils.getAuthenticatedName();

        this.orderService.userCancelOrder(email, cancelOrderInformationDto);
        redirectAttributes.addFlashAttribute("successMessage", "Hủy đơn hàng thành công");

        return "redirect:/account/order-history/details/" + cancelOrderInformationDto.getId();
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(
    )  {
        throw new NotImplementException("Chức năng này chưa hoàn thành");
    }
}
