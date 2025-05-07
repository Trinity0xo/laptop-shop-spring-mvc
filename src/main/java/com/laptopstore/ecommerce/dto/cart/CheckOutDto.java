package com.laptopstore.ecommerce.dto.cart;

import com.laptopstore.ecommerce.model.CartDetails;
import com.laptopstore.ecommerce.util.anotaion.validation.cart.CheckOutConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@CheckOutConstraint
public class CheckOutDto {
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverNote;
    private Double totalPrice;
    private List<CartDetails> validCheckOutProducts;
}
