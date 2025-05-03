package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final CartService cartService;

    public CustomUserDetailsService(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.handleGetUserByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        int cartItemCount = 0;

        Cart userCart = user.getCart();
        if (userCart != null) {
            cartItemCount = this.cartService.handleCountCartItemByCart(user.getCart());
        }

        return new CustomUserPrincipal(
                user.getEmail(),
                user.getPassword(),
                user.getFullName(),
                user.getAvatar(),
                cartItemCount,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
    }
}
