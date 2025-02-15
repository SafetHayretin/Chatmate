package com.chatmate.chatmate.controller;

import com.chatmate.chatmate.dto.LoginRequest;
import com.chatmate.chatmate.dto.RegisterRequest;
import com.chatmate.chatmate.dto.Response;
import com.chatmate.chatmate.entity.CustomUserDetails;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByEmailIgnoreCase(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new Response("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());

            Authentication auth = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(auth);

            Cookie userIdCookie = getUserIdCookie(auth);

            response.addCookie(userIdCookie);

            return ResponseEntity.ok(new Response("Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Invalid email or password"));
        }
    }

    private static Cookie getUserIdCookie(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String userId = ((CustomUserDetails) userDetails).getUserId();

        Cookie userIdCookie = new Cookie("user_id", userId);
        userIdCookie.setHttpOnly(true); // Protect against XSS attacks
        userIdCookie.setSecure(false); // Send only over HTTPS (set to false for local dev if needed)
        userIdCookie.setPath("/"); // Available for all endpoints
        userIdCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration
        return userIdCookie;
    }

}
