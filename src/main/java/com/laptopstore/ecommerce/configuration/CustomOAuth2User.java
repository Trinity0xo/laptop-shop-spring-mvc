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

    public CustomOAuth2User(String fullName, String avatar, Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);

        this.fullName = fullName;
        this.avatar = avatar;
    }
}
