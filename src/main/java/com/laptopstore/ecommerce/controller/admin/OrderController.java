package com.laptopstore.ecommerce.controller.admin;

import java.util.List;

import com.laptopstore.ecommerce.dto.order.OrderFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.model.Order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public String showOrderPage(
            OrderFilterDto orderFilterDto,
            Model model
    )  {
        PageResponse<List<Order>> response = this.orderService.getAllOrders(orderFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("orderFilterDto", orderFilterDto);

        return "admin/order/index";
    }

    @GetMapping("/details/{orderId}")
    public String showOrderItemsPage(
            @PathVariable long orderId,
            Model model
    )  {
        Order order = this.orderService.getOrderItems(orderId);
        model.addAttribute("order", order);

        return "admin/order/details";
    }

    @GetMapping("/update/{orderId}")
    public String showUpdateOrderPage(
            @PathVariable long orderId,
            Model model
    )  {
        UpdateOrderStatusDto updateOrderStatusDto = this.orderService.getOrderStatusUpdateInformation(orderId);
        model.addAttribute("updateOrderStatusDto", updateOrderStatusDto);

        return "admin/order/update";
    }

    @PostMapping("/update")
    public String updateOrder(
            @Valid UpdateOrderStatusDto updateOrderStatusDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    )  {
        if(bindingResult.hasErrors()){
            return "admin/order/update";
        }

        String email = AuthenticationUtils.getAuthenticatedName();

        this.orderService.updateOrderStatus(email, updateOrderStatusDto);

        redirectAttributes.addFlashAttribute("successMessage", "Order successfully updated");

        return "redirect:/dashboard/order/details/" + updateOrderStatusDto.getId();
    }
}
