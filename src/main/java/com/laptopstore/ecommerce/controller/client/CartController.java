package com.laptopstore.ecommerce.controller.client;

import java.util.ArrayList;
import java.util.List;

import com.laptopstore.ecommerce.model.*;
import com.laptopstore.ecommerce.util.error.BadRequestException;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.laptopstore.ecommerce.dto.cart.CheckOutDto;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    public CartController(ProductService productService, UserService userService, CartService cartService,
            OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;

    }

    @PostMapping("/buy-now/{id}")
    public String buyNow(
            @PathVariable Long id,
            @RequestParam int quantity,
            Model model,
            HttpSession session
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Product product = this.productService.handleGetProductById(id);
        if(product == null) {
            throw new NotFoundException("Product not found");
        }

        if(product.getQuantity() < quantity) {
            throw new BadRequestException("No valid products available for checkout due to insufficient stock");
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = this.cartService.handleCreateNewCart(user);
        }

        if(quantity < 0 || quantity > 99){
            quantity = 1;
        }

        cart = this.cartService.handleAddProductToCart(cart, product, quantity);

        int cartItemCount = this.cartService.handleCountCartItemByCart(cart);

        session.setAttribute("cartItemCount", cartItemCount);

        double totalPrice = this.cartService.handleCalculateCartTotalPrice(cart);

        model.addAttribute("checkOutProducts", cart.getCartDetails());
        model.addAttribute("totalPrice", totalPrice);

        CheckOutDto checkOutDto = new CheckOutDto();
        checkOutDto.setReceiverFirstName(user.getFirstName());
        checkOutDto.setReceiverLastName(user.getLastName());
        checkOutDto.setReceiverEmail(user.getEmail());
        checkOutDto.setReceiverAddress(user.getAddress());
        checkOutDto.setReceiverPhone(user.getPhone());

        model.addAttribute("checkOutDto", checkOutDto);

        return "client/checkout";
    }

    @PostMapping("/add/{id}")
    public String addToCart(
            @PathVariable Long id,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            HttpServletRequest request
    )
            throws Exception {

        Product product = productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Cart cart = user.getCart();
        if (cart == null) {
            cart = this.cartService.handleCreateNewCart(user);
        }

        if(quantity < 0 || quantity > 99){
            quantity = 1;
        }

        this.cartService.handleAddProductToCart(cart, product, quantity);

        int cartItemCount = this.cartService.handleCountCartItemByCart(cart);

        session.setAttribute("cartItemCount", cartItemCount);

        String successMessage = "Add to cart successfully";

        String currentUrl = request.getHeader("Referer");

        return "redirect:" + currentUrl + "?successMessage=" + successMessage;
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(
            @PathVariable Long id,
            @RequestParam("quantityChange") int quantityChange
    )
            throws Exception {
        Product product = productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Cart cart = user.getCart();
        if (cart == null) {
            throw new NotFoundException("Product not found");
        }

        List<CartDetails> cartDetails = cart.getCartDetails();
        if (cartDetails.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        this.cartService.handleUpdateCartProductQuantity(cartDetails, product, quantityChange);

        String successMessage = "Update cart successfully";

        return "redirect:/cart?successMessage=" + successMessage;
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session)
            throws Exception {

        Product product = productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Cart cart = user.getCart();
        if (cart == null) {
            throw new NotFoundException("Cart is empty");
        }

        List<CartDetails> cartDetails = cart.getCartDetails();
        if (cartDetails.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        this.cartService.handleRemoveCartProduct(cartDetails, product);

        int cartItemCount = this.cartService.handleCountCartItemByCart(cart);

        session.setAttribute("cartItemCount", cartItemCount);

        String successMessage = "Removed product successfully";

        return "redirect:/cart?successMessage=" + successMessage;
    }

    @GetMapping("")
    public String showCartPage(Model model)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Cart cart = user.getCart();
        if (cart == null) {
            cart = this.cartService.handleCreateNewCart(user);
        }

        double totalPrice = this.cartService.handleCalculateCartTotalPrice(cart);
        int cartItemCount = this.cartService.handleCountCartItemByCart(cart);


        boolean validProductsExist = false;
        for (CartDetails cartDetail : cart.getCartDetails()) {
            if(cartDetail.getQuantity() <= cartDetail.getProduct().getQuantity()){
                validProductsExist = true;
                break;
            }
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("validProductsExist", validProductsExist);

        return "/client/cart";
    }

    @GetMapping("/checkout")
    public String showCheckOutPage(
            Model model, HttpSession session
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Cart cart = user.getCart();
        if (cart == null) {
            throw new NotFoundException("Cart is empty");
        }

        List<CartDetails> cartDetails = cart.getCartDetails();
        if (cartDetails.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        List<CartDetails> validCheckOutProducts = new ArrayList<>();
        List<Long> validProductIds = new ArrayList<>();
        for (CartDetails cartDetail : cartDetails) {
            if(cartDetail.getQuantity() <= cartDetail.getProduct().getQuantity()){
                validCheckOutProducts.add(cartDetail);
                validProductIds.add(cartDetail.getProduct().getId());
            }
        }

        if(validCheckOutProducts.isEmpty()){
            throw new BadRequestException("No valid products available for checkout due to insufficient stock");
        }

        double totalPrice = this.cartService.handleCalculateCartTotalPrice(cart);

        CheckOutDto checkoutDto = new CheckOutDto();
        checkoutDto.setReceiverFirstName(user.getFirstName());
        checkoutDto.setReceiverLastName(user.getLastName());
        checkoutDto.setReceiverEmail(user.getEmail());
        checkoutDto.setReceiverAddress(user.getAddress());
        checkoutDto.setReceiverPhone(user.getPhone());
        checkoutDto.setTotalPrice(totalPrice);
        checkoutDto.setValidCheckOutProducts(validCheckOutProducts);

        model.addAttribute("checkOutDto", checkoutDto);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("checkOutProducts", validCheckOutProducts);

        session.setAttribute("checkOutProductIds", validProductIds);

        return "/client/checkout";
    }

    @PostMapping("/checkout")
    public String checkOut(
            @Valid CheckOutDto checkOutDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        Cart cart = user.getCart();
        if (cart == null) {
            throw new NotFoundException("Cart is empty");
        }

        List<CartDetails> cartDetails = cart.getCartDetails();
        if (cartDetails.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        List<Long> productIds = new ArrayList<>();

        Object checkOutProductIds = session.getAttribute("checkOutProductIds");
        if(checkOutProductIds instanceof List<?>){
            for (Object checkOutProductId : ((List<?>) checkOutProductIds)) {
                if(checkOutProductId instanceof Long) {
                    productIds.add((Long) checkOutProductId);
                }
            }
        }

        List<CartDetails> validCheckOutProducts = new ArrayList<>();
        for(CartDetails cartDetail : cartDetails) {
            if(productIds.contains(cartDetail.getProduct().getId()) && cartDetail.getQuantity() <= cartDetail.getProduct().getQuantity()){
               validCheckOutProducts.add(cartDetail);
            }
        }

        if (bindingResult.hasErrors()) {
            double totalPrice = this.cartService.handleCalculateCartTotalPrice(cart);
            checkOutDto.setTotalPrice(totalPrice);
            checkOutDto.setValidCheckOutProducts(validCheckOutProducts);

            model.addAttribute("checkOutDto", checkOutDto);
            return "/client/checkout";
        }

        if(validCheckOutProducts.isEmpty()){
            throw new BadRequestException("Checkout failed: Products are out of stock or invalid");
        }

        this.orderService.handleCreateNewOrder(user, checkOutDto, cartDetails);

        for (CartDetails cartDetail : validCheckOutProducts) {
            cartDetail.getProduct().setQuantity(cartDetail.getProduct().getQuantity() - cartDetail.getQuantity());
            this.productService.handleUpdateProduct(cartDetail.getProduct());
        }

        this.cartService.handleEmptyCart(validCheckOutProducts);

        int cartItemCount = this.cartService.handleCountCartItemByCart(cart);

        session.setAttribute("cartItemCount", cartItemCount);
        session.removeAttribute("checkOutProducts");

        return "redirect:/";
    }
}
