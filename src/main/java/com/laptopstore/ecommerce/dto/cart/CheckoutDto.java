package com.laptopstore.ecommerce.dto.cart;

import com.laptopstore.ecommerce.validation.cart.CheckOutConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@CheckOutConstraint
public class CheckoutDto implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String note;
    private double totalPrice;
    private List<CheckoutProduct> checkoutProducts;

    public CheckoutDto(String firstName, String lastName, String email, String phone, String address, double totalPrice, List<CheckoutProduct> checkoutProducts){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.totalPrice = totalPrice;
        this.checkoutProducts = checkoutProducts;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckoutProduct implements Serializable {
        private long id;
        private String imageName;
        private String name;
        private int quantity;
        private double price;

        public double getProductTotal(){
            return price * quantity;
        }
    }
}
