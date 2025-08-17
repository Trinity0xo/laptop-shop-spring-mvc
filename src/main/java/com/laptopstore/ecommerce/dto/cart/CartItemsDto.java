package com.laptopstore.ecommerce.dto.cart;

import com.laptopstore.ecommerce.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDto {
    List<CartItem> cartItems;
    double totalPrice;
    boolean validProductsExist;
}
