package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.util.constant.RoleEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object authPrincipal  = auth.getPrincipal();

        if(authPrincipal instanceof UserDetails) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authPrincipal;
            HttpSession session = request.getSession();
            session.setAttribute("username", userPrincipal.getUsername());
        }

        request.getSession().removeAttribute("loginErrorMessage");

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {

        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_" + RoleEnum.USER.name(), "/");
        roleTargetUrlMap.put("ROLE_" + RoleEnum.OWNER.name(), "/dashboard");
        roleTargetUrlMap.put("ROLE_" + RoleEnum.SUPER_ADMIN.name(), "/dashboard");


        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if(roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }
}
