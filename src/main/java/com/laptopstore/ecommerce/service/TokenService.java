package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.model.Token;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.util.constant.TokenTypeEnum;

public interface TokenService {
    String createToken(User user, TokenTypeEnum tokenType);
    Token getToken(String tokenValue, TokenTypeEnum tokenType);
    void deleteTokenById(long id);
}
