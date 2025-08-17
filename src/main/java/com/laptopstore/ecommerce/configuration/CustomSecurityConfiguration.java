package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.util.constant.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
public class CustomSecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SpringSessionRememberMeServices customAlwaysRememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setRememberMeParameterName("rememberMe");
        rememberMeServices.setValiditySeconds(86400);
        return rememberMeServices;
    }

    @Bean
    public SpringSessionRememberMeServices customRememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(false);
        rememberMeServices.setRememberMeParameterName("rememberMe");
        rememberMeServices.setValiditySeconds(86400);
        return rememberMeServices;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomFailureHandler customFailureHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomSuccessHandler customSuccessHandler, CustomOAuth2SuccessHandler customOAuth2SuccessHandler)
            throws Exception {
        http.authorizeHttpRequests(authorization -> authorization
                        .requestMatchers( "/", "/auth/**","/shop/**", "/about", "/contact", "/hot-deals/**", "/images/**", "/js/**", "/css/**", "/uploads/**", "/error" ).permitAll()

                        // Owners & Admins can view users
                        .requestMatchers("/dashboard/user", "/dashboard/user/details/**")
                        .hasAnyRole(RoleEnum.SUPER_ADMIN.name(), RoleEnum.OWNER.name())


                        // Admin-only actions
                        .requestMatchers("/dashboard/user/create").hasRole(RoleEnum.SUPER_ADMIN.name())
                        .requestMatchers("/dashboard/user/update-role").hasRole(RoleEnum.SUPER_ADMIN.name())
                        .requestMatchers("/dashboard/user/update-role/**").hasRole(RoleEnum.SUPER_ADMIN.name())

                        // Owners & Admins can view dashboard
                        .requestMatchers("/dashboard/**").hasAnyRole(RoleEnum.SUPER_ADMIN.name(), RoleEnum.OWNER.name())

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler)
                        .failureHandler(customFailureHandler))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .failureUrl("/auth/login?error")
                        .successHandler(customOAuth2SuccessHandler))
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/auth/login?sessionExpired"))
                .rememberMe(rememberMe -> rememberMe.rememberMeServices(customRememberMeServices()));
        return http.build();
    }
}
