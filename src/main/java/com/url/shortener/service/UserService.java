package com.url.shortener.service;

import com.url.shortener.dto.LoginRequest;
import com.url.shortener.dto.UrlMappingDto;
import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;
import com.url.shortener.repository.UrlMappingRepository;
import com.url.shortener.repository.UserRepository;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UrlMappingRepository urlMappingRepository;

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(token);
    }

    public User findUser(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                ()->new UsernameNotFoundException("user not found with given username "+name)
        );
    }


}
