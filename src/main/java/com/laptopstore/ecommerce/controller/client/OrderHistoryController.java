package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.order.OrderCriteriaDto;
import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import com.laptopstore.ecommerce.util.constant.PaymentMethodEnum;
import com.laptopstore.ecommerce.util.error.BadRequestException;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/order-history")
public class OrderHistoryController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderHistoryController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("")
    public String showOrderHistoryPage(
            @Valid OrderCriteriaDto orderCriteriaDto,
            Model model

    ) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Page<Order> orders = this.orderService.handleGetAllOrders(orderCriteriaDto, user);
        model.addAttribute("orderList", orders.getContent());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", orders.getPageable().getPageNumber() + 1);
        model.addAttribute("query", orderCriteriaDto);
        model.addAttribute("paymentMethods", PaymentMethodEnum.values());
        model.addAttribute("orderStatus", OrderStatusEnum.values());
        model.addAttribute("resultCount", orders.getTotalElements());

        return "/client/order_history";
    }

    @GetMapping("/details/{id}")
    public String showOrderDetailsPage(
            @PathVariable Long id,
            Model model
    ) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Order order = this.orderService.handleGetOrderByIdAndUser(id, user);
        if(order == null){
            throw new NotFoundException("Order not found");
        }

        model.addAttribute("order", order);

        return "/client/order_details";
    }

    @GetMapping("/cancel/{id}")
    public String showCancelOrderPage(
            @PathVariable Long id,
            Model model
    ) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Order order = this.orderService.handleGetOrderByIdAndUser(id, user);
        if(order == null){
            throw new NotFoundException("Order not found");
        }

        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto();
        updateOrderStatusDto.setId(order.getId());
        updateOrderStatusDto.setOrderStatus(order.getOrderStatus());
        updateOrderStatusDto.setCancelledReason(order.getCancelledReason());

        model.addAttribute("updateOrderStatusDto", updateOrderStatusDto);

        return "/client/cancel_order";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(
            @PathVariable Long id,
            UpdateOrderStatusDto updateOrderStatusDto,
            BindingResult bindingResult
    ) throws Exception{
//        if(bindingResult.hasErrors()){
//            return "/client/cancel_order";
//        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Order order = this.orderService.handleGetOrderByIdAndUser(id, user);
        if(order == null){
            throw new NotFoundException("Order not found");
        }

        if(order.getOrderStatus() != OrderStatusEnum.PENDING){
            throw new BadRequestException("Can only cancel order when pending");
        }

        updateOrderStatusDto.setOrderStatus(OrderStatusEnum.CANCELLED);

        this.orderService.handleUpdateOrderStatus(order, updateOrderStatusDto);

        return "redirect:/order-history/details/" + id;
    }
}
