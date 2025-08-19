package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.auth.AuthenticatedInformationDto;
import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.user.*;
import com.laptopstore.ecommerce.model.User;

import java.util.List;

public interface UserService {
    void updateUserRole(UpdateUserRoleDto updateUserRoleDto);
    UpdateUserRoleDto getUserInformationForRoleUpdate(long userId);
    void resetPassword(ResetPasswordDto resetPasswordDto);
    ResetPasswordDto getResetPasswordInformation(String tokenValue);
    String forgotPassword(ForgotPasswordDto forgotPasswordDto);
    void registerAccount(RegisterDto registerDto);
    User getUserByEmail(String email);
    User getUserById(long userId);
    User createNewUser(RegisterDto registerDto);
    void createNewUser(CreateUserDto createUserDto);
    PageResponse<List<User>> getAllUsers(UserFilterDto userFilterDto, String email);
    void updatePassword(ResetPasswordDto resetPasswordDto, User user);
    AuthenticatedInformationDto getAuthenticatedInformation(String email);
    UserInformationDto getUserAccountInformation(String email);
    UpdateUserInformationDto getUserAccountInformationForUpdate(String email);
    void updateUserAccountInformation(String email, UpdateUserInformationDto updateAccountInfoDto);
}
