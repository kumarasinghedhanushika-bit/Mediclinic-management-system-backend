package com.medical.clinic.service;

import com.medical.clinic.dto.auth.ChangePasswordRequest;
import com.medical.clinic.dto.auth.LoginResponse;
import com.medical.clinic.dto.auth.RegisterRequest;
import com.medical.clinic.dto.auth.UserResponse;
import com.medical.clinic.enums.Role;
import com.medical.clinic.enums.Status;
import com.medical.clinic.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserResponse register(RegisterRequest request, boolean staffRegistration);

    UserResponse registerWalkInPatient(RegisterRequest request);

    LoginResponse login(String email, String password);

    LoginResponse refreshAccessToken(String refreshToken);

    User forgotPassword(String email);

    void verifyEmail(String token);

    void verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);

    void changePassword(String email, ChangePasswordRequest request);

    void resendVerificationEmail(String email);

    User getUserByEmail(String email);

    User getUserById(String userId);

    UserResponse getUserResponseByEmail(String email);

    void logoutUser(String userId);

    void statusChange(String email, Status newStatus);

    void roleChange(String email, Role newRole);

    List<User> getAllUsers();

    void deleteUser(String userId);

    User updateUser(String userId, User updatedUser, MultipartFile avatarFile);
}
