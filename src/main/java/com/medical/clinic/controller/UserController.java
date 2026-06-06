package com.medical.clinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.clinic.dto.auth.ChangePasswordRequest;
import com.medical.clinic.dto.auth.RegisterRequest;
import com.medical.clinic.dto.auth.UserResponse;
import com.medical.clinic.enums.Role;
import com.medical.clinic.enums.Status;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.User;
import com.medical.clinic.mapper.UserMapper;
import com.medical.clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
@Tag(name = "User Management", description = "Profile and admin user operations")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, UserMapper userMapper, ObjectMapper objectMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        String email = currentUserEmail();
        UserResponse user = userService.getUserResponseByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>("User fetched successfully", false, true, user));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update current user profile")
    public ResponseEntity<ApiResponse<User>> update(
            @RequestPart(value = "user", required = false) String userJson,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile
    ) throws Exception {

        String email = currentUserEmail();
        User user = userService.getUserByEmail(email);

        User updatedUser = new User();
        if (userJson != null) {
            updatedUser = objectMapper.readValue(userJson, User.class);
        }

        User updated = userService.updateUser(user.getId(), updatedUser, avatarFile);
        updated.setPassword(null);

        return ResponseEntity.ok(new ApiResponse<>("User updated successfully", false, true, updated));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for current user")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(currentUserEmail(), request);
        return ResponseEntity.ok(new ApiResponse<>("Password changed successfully", false, true, null));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete current user account")
    public ResponseEntity<ApiResponse<String>> deleteUser() {
        User user = userService.getUserByEmail(currentUserEmail());
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", false, true, null));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and invalidate refresh token")
    public ResponseEntity<ApiResponse<String>> logout() {
        User user = userService.getUserByEmail(currentUserEmail());
        userService.logoutUser(user.getId());
        return ResponseEntity.ok(new ApiResponse<>("Logged out successfully", false, true, null));
    }

    @PostMapping("/register/staff")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin registers staff with role: ADMIN, DOCTOR, RECEPTIONIST, PHARMACIST, LAB_TECHNICIAN, NURSE")
    public ResponseEntity<ApiResponse<UserResponse>> registerStaff(
            @Valid @RequestBody RegisterRequest request
    ) {
        UserResponse user = userService.register(request, true);
        return ResponseEntity.ok(new ApiResponse<>("Staff user registered successfully", false, true, user));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users (admin only)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .peek(u -> u.setPassword(null))
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>("Users fetched successfully", false, true, users));
    }

    @PutMapping("/status-change")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change user account status")
    public ResponseEntity<ApiResponse<String>> changeStatus(
            @RequestParam String email,
            @RequestParam Status newStatus
    ) {
        userService.statusChange(email, newStatus);
        return ResponseEntity.ok(new ApiResponse<>("Status updated", false, true, null));
    }

    @PutMapping("/role-change")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change user role (allowed roles only)")
    public ResponseEntity<ApiResponse<String>> changeRole(
            @RequestParam String email,
            @RequestParam Role newRole
    ) {
        userService.roleChange(email, newRole);
        return ResponseEntity.ok(new ApiResponse<>("Role updated", false, true, null));
    }

    private String currentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("Unauthorized");
        }
        return auth.getName();
    }
}
