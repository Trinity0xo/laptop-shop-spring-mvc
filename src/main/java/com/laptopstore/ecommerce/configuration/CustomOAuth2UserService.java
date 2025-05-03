package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    private final CartService cartService;

    public CustomOAuth2UserService(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        User user = this.userService.handleGetUserByEmail(email);
        if(user == null){
            String firstName = oauth2User.getAttribute("given_name");
            String lastName = oauth2User.getAttribute("family_name");
            RegisterDto registerDto = new RegisterDto();
            registerDto.setFirstName(firstName);
            registerDto.setLastName(lastName);
            registerDto.setEmail(email);
            user = userService.handleCreateNewUser(registerDto);
        }

        int cartItemCount = 0;

        Cart userCart = user.getCart();
        if (userCart != null) {
            cartItemCount = this.cartService.handleCountCartItemByCart(user.getCart());
        }

        return new CustomOAuth2User(
                user.getFullName(),
                user.getAvatar(),
                cartItemCount,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())), // Authorities
                oauth2User.getAttributes(),
                "email"
        );
    }
}
