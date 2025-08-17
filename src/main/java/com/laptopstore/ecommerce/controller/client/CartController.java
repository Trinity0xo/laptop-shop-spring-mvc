package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.cart.CartItemsDto;
import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;

    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public String showCartPage(
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        CartItemsDto cartItemsDto = this.cartService.getUserCartItems(email);
        model.addAttribute("cartItemsDto", cartItemsDto);

        return "/client/cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(
            @PathVariable long productId,
            @RequestParam("quantity") int quantity,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        this.cartService.addProductToUserCart(email, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm vào giỏ hàng thành công");
        String currentUrl = request.getHeader("Referer");

        return "redirect:" + currentUrl;
    }

    @PostMapping("/update/{productId}")
    public String updateQuantity(
            @PathVariable long productId,
            @RequestParam("quantityChange") int quantityChange
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        this.cartService.updateUserCartProductQuantity(email, productId, quantityChange);

        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(
            @PathVariable long productId
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        this.cartService.removeUserCartProduct(email, productId);

        return "redirect:/cart";
    }

    @PostMapping("/buy-now/{productId}")
    public String buyNow(
            @PathVariable long productId,
            @RequestParam int quantity,
            Model model,
            HttpSession session
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        CheckoutDto checkoutDto = this.cartService.getBuyNowInformation(email, productId, quantity);
        model.addAttribute("checkoutDto", checkoutDto);
        session.setAttribute("checkoutDto", checkoutDto);

        return "client/checkout";
    }

    @GetMapping("/checkout")
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

    @PostMapping("/checkout")
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

        this.orderService.createNewOrder(checkoutDto);

        redirectAttributes.addFlashAttribute("successMessage", "Tạo đơn hàng thành công");

        return "redirect:/";
    }
}
