package com.laptopstore.ecommerce.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserInformationDto extends BaseUserInfoDto {
    private String currentAvatarName;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    public UserInformationDto(String currentAvatarName, String firstName, String lastName, String email, String address, String phone, Instant createdAt, Instant updatedAt
    ){
        this.currentAvatarName = currentAvatarName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
