package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.dto.order.CancelOrderInformationDto;
import com.laptopstore.ecommerce.dto.order.OrderFilterDto;
import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrderItems(long orderId);
    Order getUserOrderItems(String email, long orderId);
    PageResponse<List<Order>> getAllOrders(OrderFilterDto orderFilterDto);
    PageResponse<List<Order>> getUserOrderHistory(String email, OrderFilterDto orderFilterDto);
    void createNewOrder(CheckoutDto checkoutDto);
    CancelOrderInformationDto getInformationForUserCancelOrder(String email, long orderId);
    void userCancelOrder(String email, CancelOrderInformationDto cancelOrderInformationDto);
    UpdateOrderStatusDto getOrderStatusUpdateInformation( long orderId);
    void updateOrderStatus(String email, UpdateOrderStatusDto updateOrderStatusDto);
}
