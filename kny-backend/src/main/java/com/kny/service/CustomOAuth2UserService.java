package com.kny.service;

import com.kny.model.User;
import com.kny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google" or "facebook"
        String providerId = oauth2User.getAttribute("sub"); // Google uses "sub", Facebook uses "id"
        
        if (providerId == null) {
            providerId = oauth2User.getAttribute("id"); // Facebook
        }

        // Find or create user
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;
        
        if (existingUser.isPresent()) {
            user = existingUser.get();
            // Update OAuth info
            user.setOauthProvider(provider);
            user.setOauthId(providerId);
            user.setProvider(provider);
            user.setProviderId(providerId);
            userRepository.save(user);
        } else {
            // Create new user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setOauthProvider(provider);
            user.setOauthId(providerId);
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setIsActive(true);
            userRepository.save(user);
        }

        return oauth2User;
    }
}