package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.Emails.emailTemplates;
import com.medical.clinic.dto.auth.ChangePasswordRequest;
import com.medical.clinic.dto.auth.LoginResponse;
import com.medical.clinic.dto.auth.RegisterRequest;
import com.medical.clinic.dto.auth.UserResponse;
import com.medical.clinic.enums.Role;
import com.medical.clinic.enums.Status;
import com.medical.clinic.mapper.UserMapper;
import com.medical.clinic.model.User;
import com.medical.clinic.repository.UserRepository;
import com.medical.clinic.security.JwtUtil;
import com.medical.clinic.service.CloudinaryService;
import com.medical.clinic.service.EmailServise;
import com.medical.clinic.service.PatientService;
import com.medical.clinic.service.UserService;
import com.medical.clinic.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int OTP_EXPIRY_MINUTES = 10;
    private static final int PASSWORD_RESET_WINDOW_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServise emailService;
    private final CloudinaryService cloudinaryService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PatientService patientService;
    private final PasswordGenerator passwordGenerator;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpirationMs;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailServise emailService,
            CloudinaryService cloudinaryService,
            UserMapper userMapper,
            JwtUtil jwtUtil,
            PatientService patientService, PasswordGenerator passwordGenerator
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.cloudinaryService = cloudinaryService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.patientService = patientService;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public UserResponse registerWalkInPatient(RegisterRequest request) {
        UserResponse response = register(request, false);
        User user = getUserByEmail(request.getEmail());
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
        response.setEmailVerified(true);
        sendWalkInWelcomeEmail(user);
        return response;
    }

    @Override
    public UserResponse register(RegisterRequest request,
                                 boolean staffRegistration) {

        if (userRepository.existsByEmail(
                request.getEmail().trim().toLowerCase())) {
            throw new RuntimeException("Email already exists");
        }

        Role role =
                resolveRegistrationRole(
                        request.getRole(),
                        staffRegistration);

        User user = userMapper.toEntity(request, role);

        String rawPassword;

        if (staffRegistration) {
            rawPassword = passwordGenerator.generate();
        } else {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new RuntimeException("Password is required");
            }
            rawPassword = request.getPassword();
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPasswordChangeRequired(staffRegistration);

// 🔥 GENERATE TOKEN LAST (SAFE)
        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);

// 🔥 SAVE AND VERIFY RESULT
        User savedUser = userRepository.save(user);

        System.out.println("SAVED TOKEN = " + savedUser.getEmailVerificationToken());
        if (role == Role.PATIENT) {
            patientService.getOrCreateForUser(savedUser);
        }

        if (staffRegistration) {


            sendStaffWelcomeEmail(
                    savedUser,
                    rawPassword,
                    token);
        } else {
            sendVerificationEmail(
                    savedUser,
                    token);
        }

        return userMapper.toResponse(savedUser);
    }
    @Override
    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (Boolean.TRUE.equals(user.getAccountLocked())) {
            throw new RuntimeException("Account is locked. Contact administrator.");
        }

        if (user.getStatus() == Status.SUSPENDED || user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            handleFailedLogin(user);
            throw new RuntimeException("Invalid email or password");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setLastLogin(LocalDateTime.now());

        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000));

        userRepository.save(user);

        return new LoginResponse(
                jwtUtil.generateToken(user),
                refreshToken,
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (user.getRefreshTokenExpiry() == null
                || user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        if (Boolean.TRUE.equals(user.getAccountLocked())) {
            throw new RuntimeException("Account is locked");
        }

        String newRefreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000));
        userRepository.save(user);

        return new LoginResponse(
                jwtUtil.generateToken(user),
                newRefreshToken,
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public User forgotPassword(String email) {
        User user = getUserByEmail(email);
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        user.setForgotPasswordOtp(otp);
        user.setForgotPasswordExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        user.setPasswordResetVerified(false);

        emailService.sendEmail(
                user.getEmail(),
                "Your OTP Code",
                emailTemplates.otpEmail(user.getFirstName(), otp)
        );

        return userRepository.save(user);
    }

    @Override
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
    }

    @Override
    public void verifyOtp(String email, String otp) {
        User user = getUserByEmail(email);

        if (user.getForgotPasswordExpiry() == null
                || user.getForgotPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!otp.equals(user.getForgotPasswordOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setForgotPasswordOtp(null);
        user.setPasswordResetVerified(true);
        user.setForgotPasswordExpiry(LocalDateTime.now().plusMinutes(PASSWORD_RESET_WINDOW_MINUTES));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = getUserByEmail(email);

        if (!Boolean.TRUE.equals(user.getPasswordResetVerified())) {
            throw new RuntimeException("Please verify OTP before resetting password");
        }

        if (user.getForgotPasswordExpiry() == null
                || user.getForgotPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset session expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetVerified(false);
        user.setForgotPasswordExpiry(null);
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setPasswordChangeRequired(false);
        userRepository.save(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangeRequired(false);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = getUserByEmail(email);

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email is already verified");
        }

        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        sendVerificationEmail(user, token);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserResponse getUserResponseByEmail(String email) {
        return userMapper.toResponse(getUserByEmail(email));
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist."));
    }

    @Override
    public void logoutUser(String userId) {
        User user = getUserById(userId);
        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public void statusChange(String email, Status newStatus) {
        User user = getUserByEmail(email);
        user.setStatus(newStatus);
        if (newStatus == Status.SUSPENDED || newStatus == Status.INACTIVE) {
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
        }
        userRepository.save(user);
    }

    @Override
    public void roleChange(String email, Role newRole) {
        validateRole(newRole);
        User user = getUserByEmail(email);
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(String userId, User updatedUser, MultipartFile avatarFile) {
        User user = getUserById(userId);

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                cloudinaryService.deleteImage(user.getAvatar());
            }
            String newImageUrl = cloudinaryService.uploadImage(avatarFile);
            user.setAvatar(newImageUrl);
        }

        return userRepository.save(user);
    }

    private Role resolveRegistrationRole(Role requestedRole, boolean staffRegistration) {
        if (staffRegistration) {
            if (requestedRole == null) {
                throw new RuntimeException("Role is required for staff registration");
            }
            validateRole(requestedRole);
            return requestedRole;
        }
        if (requestedRole != null && requestedRole != Role.PATIENT) {
            throw new RuntimeException("Public registration is only allowed for PATIENT role");
        }
        return Role.PATIENT;
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new RuntimeException("Invalid role");
        }
    }

    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        attempts++;
        user.setFailedLoginAttempts(attempts);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
        }
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user, String token) {
        System.out.println("frontend url"+frontendUrl);
        String link = frontendUrl +
                "/verify-email?token=" + token;
        emailService.sendEmail(
                user.getEmail(),
                "Please verify your email",
                emailTemplates.welcomeEmail(user.getFirstName(), link)
        );
    }

    private void sendStaffWelcomeEmail(
            User user,
            String tempPassword,
            String token) {

        String verifyUrl =
                frontendUrl +
                        "/verify-email?token=" +
                        token;

        String resetUrl =
                frontendUrl +
                        "/forgot-password";

        String html =
                emailTemplates.staffAccountCreatedEmail(
                        user.getFirstName(),
                        user.getEmail(),
                        tempPassword,
                        verifyUrl,
                        resetUrl);

        emailService.sendEmail(
                user.getEmail(),
                "Your Clinic Staff Account",
                html);
    }

    private void sendWalkInWelcomeEmail(User user) {

        String subject = "Welcome to Clinic Management System";

        String body = emailTemplates.walkInWelcomeEmail(
                user.getFirstName(),
                user.getEmail()
        );

        emailService.sendEmail(user.getEmail(), subject, body);
    }
}
