package com.laptopstore.ecommerce.service;

import java.util.List;

import com.laptopstore.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.CartDetails;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.CartDetailsRepository;
import com.laptopstore.ecommerce.repository.CartRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailsRepository cartDetailsRepository;

    public CartService(CartRepository cartRepository, CartDetailsRepository cartDetailsRepository) {
        this.cartRepository = cartRepository;
        this.cartDetailsRepository = cartDetailsRepository;
    }

    public int handleCountCartItemByCart(Cart cart){
        return this.cartDetailsRepository.countByCart(cart);
    }

    public double handleCalculateCartTotalPrice(Cart cart) {
        double totalPrice = 0;

        List<CartDetails> cartDetails = cartDetailsRepository.findByCart(cart);
        if (!cartDetails.isEmpty()) {
            for (CartDetails cartDetail : cartDetails) {
                if(cartDetail.getQuantity() <= cartDetail.getProduct().getQuantity()){
                    totalPrice = totalPrice + (cartDetail.getProduct().getDiscountPrice() * cartDetail.getQuantity());
                }
            }
        }

        return totalPrice;
    }

    public Cart handleAddProductToCart(Cart cart, Product product, int quantity) {
        List<CartDetails> cartDetails = cart.getCartDetails();

        boolean isProductAlreadyInCart = false;

        if (!cartDetails.isEmpty()) {
            for (CartDetails cartDetail : cartDetails) {
                if (cartDetail.getProduct().getId().equals(product.getId())) {
                    cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                    isProductAlreadyInCart = true;
                    break;
                }
            }

            this.cartDetailsRepository.saveAll(cartDetails);
        }

        if (!isProductAlreadyInCart) {
            CartDetails cartDetail = new CartDetails();
            cartDetail.setCart(cart);
            cartDetail.setProduct(product);
            cartDetail.setQuantity(quantity);

            cart.getCartDetails().add(cartDetail);

            this.cartDetailsRepository.save(cartDetail);
        }

       return this.cartRepository.save(cart);
    }

    public Cart handleCreateNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);

        return this.cartRepository.save(cart);
    }

    public void handleUpdateCartProductQuantity(List<CartDetails> cartDetails, Product product, long quantityChange){
        for (CartDetails cartDetail : cartDetails) {
            if (cartDetail.getProduct().getId().equals(product.getId())) {
                long newQuantity = cartDetail.getQuantity() + quantityChange;
                if(newQuantity > 0 && newQuantity <= 99) {
                    cartDetail.setQuantity(newQuantity);
                    this.cartDetailsRepository.save(cartDetail);
                }
                break;
            }
        }
    }

    public void handleRemoveCartProduct(List<CartDetails> cartDetails, Product product){
        for (CartDetails cartDetail : cartDetails) {
            if (cartDetail.getProduct().getId().equals(product.getId())) {
                this.cartDetailsRepository.delete(cartDetail);
                break;
            }
        }
    }

    public void handleEmptyCart(List<CartDetails> cartDetails){
        this.cartDetailsRepository.deleteAll(cartDetails);
    }
}
