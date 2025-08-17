package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.cart.CartItemsDto;
import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.User;

public interface CartService {
    Cart createNewUserCart(User user);
    CheckoutDto getBuyNowInformation(String email, long productId, int quantity);
    CheckoutDto getUserCheckoutInformation(String email);
    CartItemsDto getUserCartItems(String email);
    void addProductToUserCart(String email, long productId, int quantity);
    void updateUserCartProductQuantity(String email, long productId, int quantityChange);
    void removeUserCartProduct(String email, long productId);
}
