package com.laptopstore.ecommerce.configuration;

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
        User user = this.userService.getUserByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        int cartItemCount = this.cartService.getTotalCartItem(user.getEmail());

        String password = (user.getPassword() != null && !user.getPassword().isEmpty())
                ? user.getPassword()
                : "{noop}N/A"; // dummy password

        return new CustomUserPrincipal(
                user.getEmail(),
                password,
                user.getFullName(),
                user.getAvatar(),
                cartItemCount,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getSlug())));
    }
}
