package com.marketplace.hyderabad.controller;

import com.marketplace.hyderabad.model.User;
import com.marketplace.hyderabad.repository.UserRepository;
import com.marketplace.hyderabad.security.JwtUtil;
import com.marketplace.hyderabad.security.OtpService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    record PasswordLoginRequest(String phoneNumber, String password) {}
    record OtpRequest(String phoneNumber) {}
    record OtpVerifyRequest(String phoneNumber, String otp) {}
    record RegisterShopRequest(String phoneNumber, String name, String email, String businessName, String password) {}
    record RegisterBuyerRequest(String phoneNumber, String name, String email, String password) {}

    @Data
    static class AuthResponse { private String accessToken; private Long userId; }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.otpService = otpService;
    }

    @PostMapping("/login/password")
    public ResponseEntity<?> loginWithPassword(@RequestBody PasswordLoginRequest req) {
        if (req.phoneNumber() == null || req.password() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "phoneNumber and password required"));
        }
        return userRepository.findByPhoneNumber(req.phoneNumber())
                .map(u -> {
                    if (u.getPassword() == null) return ResponseEntity.status(401).body(Map.of("error", "No local password set"));
                    if (!passwordEncoder.matches(req.password(), u.getPassword())) {
                        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
                    }
                    String token = jwtUtil.generateToken(String.valueOf(u.getId()));
                    AuthResponse resp = new AuthResponse();
                    resp.setAccessToken(token);
                    resp.setUserId(u.getId());
                    return ResponseEntity.ok(resp);
                })
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @PostMapping("/otp/request")
    public ResponseEntity<?> requestOtp(@RequestBody OtpRequest req) {
        if (req == null || req.phoneNumber() == null) return ResponseEntity.badRequest().body(Map.of("error", "phoneNumber required"));
        String otp = otpService.generateOtp(req.phoneNumber());
        // In prod you'd send SMS. For dev, return the OTP in response so frontend can use it.
        return ResponseEntity.ok(Map.of("message", "OTP generated (dev)", "otp", otp));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest req) {
        if (req == null || req.phoneNumber() == null || req.otp() == null) return ResponseEntity.badRequest().body(Map.of("error", "phoneNumber and otp required"));
        boolean ok = otpService.verifyOtp(req.phoneNumber(), req.otp());
        if (!ok) return ResponseEntity.status(401).body(Map.of("error", "Invalid OTP"));
        User user = userRepository.findByPhoneNumber(req.phoneNumber()).orElseGet(() -> {
            User u = User.builder()
                    .phoneNumber(req.phoneNumber())
                    .role("USER")
                    .build();
            return userRepository.save(u);
        });
        String token = jwtUtil.generateToken(String.valueOf(user.getId()));
        AuthResponse resp = new AuthResponse();
        resp.setAccessToken(token);
        resp.setUserId(user.getId());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register-shop")
    public ResponseEntity<?> registerShop(@RequestBody RegisterShopRequest req) {
        if (req == null || req.phoneNumber() == null) return ResponseEntity.badRequest().body(Map.of("error", "phoneNumber required"));
        if (userRepository.findByPhoneNumber(req.phoneNumber()).isPresent()) return ResponseEntity.status(409).body(Map.of("error", "User exists"));
        User u = User.builder()
                .phoneNumber(req.phoneNumber())
                .name(req.name())
                .email(req.email())
                .businessName(req.businessName())
                .password(req.password() == null ? null : passwordEncoder.encode(req.password()))
                .role("SELLER")
                .build();
        User saved = userRepository.save(u);
        return ResponseEntity.status(201).body(Map.of("id", saved.getId()));
    }

    @PostMapping("/register-buyer")
    public ResponseEntity<?> registerBuyer(@RequestBody RegisterBuyerRequest req) {
        if (req == null || req.phoneNumber() == null || req.password() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "phoneNumber and password required"));
        }
        if (userRepository.findByPhoneNumber(req.phoneNumber()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "User exists"));
        }

        User u = User.builder()
                .phoneNumber(req.phoneNumber())
                .name(req.name())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .role("USER")
                .build();
        User saved = userRepository.save(u);

        String token = jwtUtil.generateToken(String.valueOf(saved.getId()));
        AuthResponse resp = new AuthResponse();
        resp.setAccessToken(token);
        resp.setUserId(saved.getId());
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestBody PasswordLoginRequest req) {
        if (req == null || req.phoneNumber() == null || req.password() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "phoneNumber and password required"));
        }
        return userRepository.findByPhoneNumber(req.phoneNumber())
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(req.password()));
                    userRepository.save(u);
                    return ResponseEntity.ok(Map.of("message", "password set"));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(name = "Authorization", required = false) String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                String sub = jwtUtil.parseToken(token).getBody().getSubject();
                Long userId = Long.parseLong(sub);
                return userRepository.findById(userId)
                        .map(u -> ResponseEntity.ok(Map.of("id", u.getId(), "phoneNumber", u.getPhoneNumber(), "name", u.getName(), "email", u.getEmail(), "role", u.getRole())))
                        .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
            } catch (Exception e) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
            }
        }
        return ResponseEntity.status(401).body(Map.of("error", "Authorization required"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // no-op for now
        return ResponseEntity.ok(Map.of("message", "logged out (no-op)"));
    }
}
