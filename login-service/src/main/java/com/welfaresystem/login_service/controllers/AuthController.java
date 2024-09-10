package com.welfaresystem.login_service.controllers;

import com.welfaresystem.login_service.jwt.JwtUtil;
import com.welfaresystem.login_service.models.User;
import com.welfaresystem.login_service.payload.LoginRequest;
import com.welfaresystem.login_service.payload.JwtResponse;
import com.welfaresystem.login_service.payload.MessageResponse;
import com.welfaresystem.login_service.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Authenticate user and generate JWT token
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user using provided credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set the authenticated user in the Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token using the authenticated user's details
        String jwt = jwtUtil.generateToken(authentication.getName());

        // Retrieve user details from authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Return JWT and user details in response
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if the username is already taken
        if (userDetailsService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        // Check if the email is already taken
        if (userDetailsService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        //if (userDetailsService.)
        // Encode the user's password and save the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDetailsService.saveUser(user);

        // Return a success message
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}