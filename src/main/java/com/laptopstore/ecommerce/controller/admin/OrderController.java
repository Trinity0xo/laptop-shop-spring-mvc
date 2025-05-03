package com.laptopstore.ecommerce.controller.admin;

import java.util.List;

import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import com.laptopstore.ecommerce.util.constant.PaymentMethodEnum;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.dto.order.OrderCriteriaDto;
import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.model.OrderDetails;
import com.laptopstore.ecommerce.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/dashboard/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public String showOrderPage(OrderCriteriaDto orderCriteriaDto, Model model) {
        Page<Order> orders = this.orderService.handleGetAllOrders(orderCriteriaDto);
        model.addAttribute("orderList", orders.getContent());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", orders.getPageable().getPageNumber() + 1);
        model.addAttribute("query", orderCriteriaDto);
        model.addAttribute("paymentMethods", PaymentMethodEnum.values());
        model.addAttribute("orderStatus", OrderStatusEnum.values());
        model.addAttribute("resultCount", orders.getTotalElements());

        return "admin/order/index";
    }

    @GetMapping("/details/{id}")
    public String showOrderDetailsPage(
            @PathVariable Long id,
            Model model)
            throws Exception {
        Order order = this.orderService.handleGetOrderById(id);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        List<OrderDetails> orderDetails = order.getOrderDetails();

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);

        return "admin/order/details";
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderPage(
            @PathVariable Long id,
            Model model
    )throws Exception{
        Order order = this.orderService.handleGetOrderById(id);
        if(order == null){
            throw new NotFoundException("Order not found");
        }

        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto();
        updateOrderStatusDto.setId(order.getId());
        updateOrderStatusDto.setOrderStatus(order.getOrderStatus());
        updateOrderStatusDto.setCancelledReason(order.getCancelledReason());

        model.addAttribute("updateOrderStatusDto", updateOrderStatusDto);
        model.addAttribute("orderStatus", OrderStatusEnum.values());

        return "admin/order/edit";
    }

    @PostMapping("/edit/{id}")
    public String editOrder(
            @PathVariable Long id,
            UpdateOrderStatusDto updateOrderStatusDto)
            throws Exception {

        Order order = this.orderService.handleGetOrderById(id);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        this.orderService.handleUpdateOrderStatus(order, updateOrderStatusDto);

        return "redirect:/dashboard/order/details/" + id;
    }
}
