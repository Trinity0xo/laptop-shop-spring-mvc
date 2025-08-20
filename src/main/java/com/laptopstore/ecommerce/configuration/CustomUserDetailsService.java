package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.model.User;
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

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.getUserByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        String password = (user.getPassword() != null && !user.getPassword().isEmpty())
                ? user.getPassword()
                : "{noop}N/A"; // dummy password nếu đăng nhập bằng google

        return new CustomUserPrincipal(
                user.getEmail(),
                password,
                user.getFullName(),
                user.getAvatar(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getSlug())));
    }
}
