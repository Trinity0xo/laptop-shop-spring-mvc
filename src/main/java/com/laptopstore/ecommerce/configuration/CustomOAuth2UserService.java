package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.service.impl.UserServiceImpl;
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

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
        ;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        User user = this.userService.getUserByEmail(email);
        if(user == null){
            String firstName = oauth2User.getAttribute("given_name");
            String lastName = oauth2User.getAttribute("family_name");
            RegisterDto registerDto = new RegisterDto();
            registerDto.setFirstName(firstName);
            registerDto.setLastName(lastName);
            registerDto.setEmail(email);
            user = userService.createNewUser(registerDto);
        }

        return new CustomOAuth2User(
                user.getFullName(),
                user.getAvatar(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getSlug())), // Authorities
                oauth2User.getAttributes(),
                "email"
        );
    }
}
