package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.util.anotaion.validation.user.UpdateAccountConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@UpdateAccountConstraint
public class UpdateAccountDto {
//    private MultipartFile avatar;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
