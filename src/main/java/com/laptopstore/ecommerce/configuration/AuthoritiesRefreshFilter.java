package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.CartService;
import com.laptopstore.ecommerce.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthoritiesRefreshFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final CartService cartService;

    public AuthoritiesRefreshFilter(UserDetailsService userDetailsService, UserService userService, CartService cartService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            Object principal = auth.getPrincipal();

            if(principal instanceof UserDetails userDetails){
                try{
                    UserDetails updatedUser = userDetailsService.loadUserByUsername(userDetails.getUsername());

                    UsernamePasswordAuthenticationToken newAuth =
                            new UsernamePasswordAuthenticationToken(
                                    updatedUser,
                                    updatedUser.getPassword(),
                                    updatedUser.getAuthorities()
                            );
                    newAuth.setDetails(auth.getDetails());
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                }catch (UsernameNotFoundException e){
                    SecurityContextHolder.clearContext();
                }
            } else if (principal instanceof OAuth2User oAuth2User) {
                String email = oAuth2User.getAttribute("email");

                User user = userService.getUserByEmail(email);

                if(user != null){
                    int cartItemCount = cartService.getTotalCartItem(email);
                    UsernamePasswordAuthenticationToken newAuth = getUsernamePasswordAuthenticationToken(oAuth2User, user, cartItemCount);
                    newAuth.setDetails(auth.getDetails());
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                }else{
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(OAuth2User oAuth2User, User user, int cartItemCount) {
        CustomOAuth2User updatedPrincipal = new CustomOAuth2User(
                user.getFullName(),
                user.getAvatar(),
                cartItemCount,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getSlug())),
                oAuth2User.getAttributes(),
                "email"
        );

        return new UsernamePasswordAuthenticationToken(
                updatedPrincipal,
                null,
                updatedPrincipal.getAuthorities()
        );
    }
}
