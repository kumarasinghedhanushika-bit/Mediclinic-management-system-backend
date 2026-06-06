package com.medical.clinic.controller;

import com.medical.clinic.dto.auth.*;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Tag(name = "Authentication", description = "Register, login, tokens, and password management")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new patient account")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        UserResponse user = userService.register(request, false);
        return ResponseEntity.ok(new ApiResponse<>(
                "User registered successfully. Please verify your email.",
                false,
                true,
                user
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new ApiResponse<>("Login success", false, true, response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LoginResponse response = userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>("Token refreshed", false, true, response));
    }

    @GetMapping("/verify-email/{token}")
    @Operation(summary = "Verify email from link")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@PathVariable String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse<>("Email verified successfully", false, true, null));
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend email verification link")
    public ResponseEntity<ApiResponse<String>> resendVerification(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.ok(new ApiResponse<>("Verification email sent", false, true, null));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Send OTP for password reset")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok(new ApiResponse<>("OTP sent to your email", false, true, null));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP before password reset")
    public ResponseEntity<ApiResponse<String>> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        userService.verifyOtp(email, otp);
        return ResponseEntity.ok(new ApiResponse<>("OTP verified successfully", false, true, null));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password after OTP verification")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>("Password reset successfully", false, true, null));
    }

}
