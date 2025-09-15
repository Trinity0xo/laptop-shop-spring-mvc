package com.laptopstore.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.laptopstore.ecommerce.dto.cart.CartItemsDto;
import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.repository.ProductRepository;
import com.laptopstore.ecommerce.repository.UserRepository;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.exception.BadRequestException;
import com.laptopstore.ecommerce.exception.ProductNotFoundException;
import com.laptopstore.ecommerce.exception.AuthenticatedUserNotFoundException;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.CartItem;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.CartItemsRepository;
import com.laptopstore.ecommerce.repository.CartRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    public CartServiceImpl(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Override
    public Cart createNewUserCart(User user) {
        Cart cart = new Cart(user);
        return this.cartRepository.save(cart);
    }

    @Override
    public CheckoutDto getBuyNowInformation(String email, long productId, int quantity){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException(productId);
        }

        if (quantity <= 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0");
        }

        double totalPrice = product.getDiscountPrice() * quantity;

        CheckoutDto.CheckoutProduct checkoutProduct = new CheckoutDto.CheckoutProduct(
                product.getId(),
                product.getProductImages().get(0).getImageName(),
                product.getName(),
                quantity,
                product.getDiscountPrice()
        );

        List<CheckoutDto.CheckoutProduct> checkoutProducts = new ArrayList<>();
        checkoutProducts.add(checkoutProduct);

        return new CheckoutDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                totalPrice,
                checkoutProducts
        );
    }

    @Override
    public CheckoutDto getUserCheckoutInformation(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();

        }

        Cart userCart = user.getCart();
        if(userCart == null || userCart.getCartItems().isEmpty()){
            throw new BadRequestException("Giỏ hàng trống");
        }

        List<CartItem> validCartItems = new ArrayList<>();
        for (CartItem cartItem : userCart.getCartItems()) {
            if(cartItem.getQuantity() <= cartItem.getProduct().getQuantity()){
                validCartItems.add(cartItem);
            }
        }

        if(validCartItems.isEmpty()){
            throw new BadRequestException("Không thể thanh toán vì một số sản phẩm đã hết hàng");
        }

        double totalPrice = 0;

        List<CheckoutDto.CheckoutProduct> checkoutProducts = new ArrayList<>();

        for (CartItem cartItem : validCartItems){
            double itemTotal = cartItem.getProduct().getDiscountPrice() * cartItem.getQuantity();
            totalPrice += itemTotal;

            CheckoutDto.CheckoutProduct checkoutProduct = new CheckoutDto.CheckoutProduct(
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getProductImages().isEmpty() ? null : cartItem.getProduct().getProductImages().get(0).getImageName(),
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getDiscountPrice()
            );
            checkoutProducts.add(checkoutProduct);
        }

        return new CheckoutDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                totalPrice,
                checkoutProducts
        );
    }

    @Override
    @Transactional
    public void addProductToUserCart(String email, long productId, int quantity) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }

        Cart userCart = user.getCart();
        if (userCart == null) {
            userCart = this.createNewUserCart(user);
            user.setCart(userCart);
            this.userRepository.save(user);
        }

        if(quantity < 0 || quantity > 99){
            quantity = 1;
        }

        List<CartItem> cartItems = userCart.getCartItems();

        boolean isProductAlreadyInCart = false;

        if (!cartItems.isEmpty()) {
            for (CartItem details : cartItems) {
                if (details.getProduct().getId().equals(product.getId())) {
                    details.setQuantity(details.getQuantity() + quantity);
                    isProductAlreadyInCart = true;
                    break;
                }
            }
        }

        if (!isProductAlreadyInCart) {
            CartItem cartItem = new CartItem(userCart, product, quantity);
            userCart.getCartItems().add(cartItem);
        }

        this.cartRepository.save(userCart);
    }

    @Override
    public void updateUserCartProductQuantity(String email, long productId, int quantityChange){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }

        Cart userCart = user.getCart();
        if(userCart == null || userCart.getCartItems().isEmpty()){
            throw new BadRequestException("Giỏ hàng trống");
        }

        for (CartItem item : user.getCart().getCartItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                int newQuantity = item.getQuantity() + quantityChange;
                if(newQuantity > 0 && newQuantity <= 99) {
                    item.setQuantity(newQuantity);
                }
                break;
            }
        }

        this.cartRepository.save(userCart);
    }

    @Override
    public void removeUserCartProduct(String email, long productId){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        Cart userCart = user.getCart();
        if (userCart == null || userCart.getCartItems().isEmpty()) {
            return;
        }

        CartItem needRemove = null;

        for (CartItem item : userCart.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                needRemove = item;
                break;
            }
        }

        if(needRemove != null){
            userCart.getCartItems().remove(needRemove);
        }

        this.cartRepository.save(userCart);
    }

    @Override
    public CartItemsDto getUserCartItems(String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        Cart userCart = user.getCart();
        if(userCart == null || userCart.getCartItems().isEmpty()){
            return new CartItemsDto(Collections.emptyList(), 0.0, false);
        }

        double totalPrice = this.cartItemsRepository.calculateCartTotalPrice(userCart);

        boolean validProductsExist = true;

        for (CartItem item : userCart.getCartItems()) {
            if(item.getQuantity() > item.getProduct().getQuantity()){
                validProductsExist = false;
                break;
            }
        }

        return new CartItemsDto(
                userCart.getCartItems(),
                totalPrice,
                validProductsExist
        );
    }
}
