package com.practice.doctor_patient_app.security.controller;

import com.practice.doctor_patient_app.dto.UserLogin;
import com.practice.doctor_patient_app.security.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerPatient(@Valid @RequestBody RegisterRequest req) {
//        User savedUser = userService.registerPatient(req);
//        return ResponseEntity.ok(savedUser.getId());
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin user) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        String token = jwtUtil.generateToken(user.getEmail(),
                auth.getAuthorities().iterator().next().getAuthority());

        return ResponseEntity.ok(token);
    }
}
