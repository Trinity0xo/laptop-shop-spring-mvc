package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.model.Token;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.TokenRepository;
import com.laptopstore.ecommerce.service.TokenService;
import com.laptopstore.ecommerce.util.constant.TokenTypeEnum;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String createToken(User user, TokenTypeEnum tokenType){
        String tokenValue = UUID.randomUUID().toString();

        Token resetPasswordToken = new Token(user, tokenValue, tokenType);

        this.tokenRepository.save(resetPasswordToken);

        return tokenValue;
    }

    @Override
    public Token getToken(String token, TokenTypeEnum tokenType){
        return this.tokenRepository.findByValueAndType(token, tokenType).orElse(null);
    }

    @Override
    public void deleteTokenById(long id) {
        this.tokenRepository.deleteById(id);
    }
}
