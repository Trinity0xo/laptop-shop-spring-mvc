package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.model.ResetPasswordToken;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.ResetPasswordTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;


    public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    public String handleCreateResetPasswordToken(User user){
        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setToken(token);
        resetPasswordToken.setUser(user);

        this.resetPasswordTokenRepository.save(resetPasswordToken);

        return token;
    }

    public ResetPasswordToken handleGetResetPasswordToken(String token){
        return this.resetPasswordTokenRepository.findByToken(token).orElse(null);
    }
}
