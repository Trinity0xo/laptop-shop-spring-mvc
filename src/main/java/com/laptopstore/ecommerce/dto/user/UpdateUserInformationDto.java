package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.validation.user.UpdateAccountInfoConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@UpdateAccountInfoConstraint
public class UpdateUserInformationDto extends BaseUserInfoDto {
    private String currentAvatarName;
    private MultipartFile newAvatar;

    public UpdateUserInformationDto(String currentAvatarName, String firstName, String lastName, String address, String phone){
        this.currentAvatarName = currentAvatarName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}
