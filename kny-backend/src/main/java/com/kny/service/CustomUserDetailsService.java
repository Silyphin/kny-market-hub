package com.kny.service;

import com.kny.model.User;
import com.kny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findActiveUserByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        
        User user = userOpt.get();
        
       
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            // This might happen for OAuth users who don't have a local password
            throw new UsernameNotFoundException("User has no local password (OAuth user?): " + email);
        }
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .accountExpired(false)
                .accountLocked(!user.getIsActive()) // Lock account if not active
                .credentialsExpired(false)
                .disabled(!user.getIsActive()) // Disable if not active
                .build();
    }
}