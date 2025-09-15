package com.laptopstore.ecommerce.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomOAuth2User extends DefaultOAuth2User {
    private String fullName;
    private String avatar;
    private int cartItemCount;

    public CustomOAuth2User(String fullName, String avatar, int cartItemCount, Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);

        this.fullName = fullName;
        this.avatar = avatar;
        this.cartItemCount = cartItemCount;
    }
}
