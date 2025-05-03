package com.laptopstore.ecommerce.util.anotaion.validation.cart;

import com.laptopstore.ecommerce.dto.cart.CheckOutDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckOutValidator implements ConstraintValidator<CheckOutConstraint, CheckOutDto> {
    @Override
    public boolean isValid(CheckOutDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if(value.getReceiverFirstName() == null || value.getReceiverFirstName().length() < 2){
            context.buildConstraintViolationWithTemplate("First name must be at least 2 characters")
                    .addPropertyNode("receiverFirstName")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getReceiverFirstName() == null || value.getReceiverLastName().length() < 2){
            context.buildConstraintViolationWithTemplate("Last name must be at least 2 characters")
                    .addPropertyNode("receiverLastName")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getReceiverEmail() == null || value.getReceiverEmail().isEmpty()){
            context.buildConstraintViolationWithTemplate("Email is required")
                    .addPropertyNode("receiverEmail")
                    .addConstraintViolation();
            isValid = false;
        }else if(!value.getReceiverEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            context.buildConstraintViolationWithTemplate("Invalid email")
                    .addPropertyNode("receiverEmail")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getReceiverPhone() == null || !value.getReceiverPhone().matches("0\\d{9,14}")){
            context.buildConstraintViolationWithTemplate("Invalid phone number")
                    .addPropertyNode("receiverPhone")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getReceiverAddress() == null || value.getReceiverAddress().isEmpty()){
            context.buildConstraintViolationWithTemplate("Address is required")
                    .addPropertyNode("receiverAddress")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
