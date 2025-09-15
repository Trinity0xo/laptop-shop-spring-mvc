package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public String showCheckOutPage(
            Model model,
            HttpSession session
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        CheckoutDto checkoutDto = this.cartService.getUserCheckoutInformation(email);
        model.addAttribute("checkoutDto", checkoutDto);
        session.setAttribute("checkoutDto", checkoutDto);

        return "/client/checkout";
    }

    @PostMapping("")
    public String checkOut(
            @Valid CheckoutDto checkoutDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session
    )  {
        CheckoutDto savedCheckoutDto = (CheckoutDto) session.getAttribute("checkoutDto");
        if(savedCheckoutDto == null){
            String email = AuthenticationUtils.getAuthenticatedName();
            savedCheckoutDto = this.cartService.getUserCheckoutInformation(email);
        }

        checkoutDto.setCheckoutProducts(savedCheckoutDto.getCheckoutProducts());
        checkoutDto.setTotalPrice(savedCheckoutDto.getTotalPrice());

        if(bindingResult.hasErrors()){
            model.addAttribute("checkoutDto", checkoutDto);
            return "/client/checkout";
        }

        Order order = this.orderService.createNewOrder(checkoutDto);
        session.removeAttribute("checkoutDto");
        redirectAttributes.addFlashAttribute("orderId", order.getId());

        return "redirect:/checkout/confirmation";
    }

    @GetMapping("/confirmation")
    public String showCheckOutConfirmationPage(
            Model model,
            @ModelAttribute("orderId") long orderId
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();
        Order order = this.orderService.getUserOrderItems(email, orderId);
        model.addAttribute("order", order);

        return "/client/checkout_confirmation";
    }
}
