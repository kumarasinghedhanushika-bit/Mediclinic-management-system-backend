package com.medical.clinic.model;

import com.medical.clinic.enums.Role;
import com.medical.clinic.enums.Status;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    @Email
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String avatar;

    private Role role;
    private Status status;


    private Boolean passwordChangeRequired = false;

    @Builder.Default
    private Boolean emailVerified = false;
    @Builder.Default
    private Boolean accountLocked = false;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiry;
    private LocalDateTime lastLogin;

    private Integer failedLoginAttempts;

    @Builder.Default
    private Boolean passwordResetVerified = false;

    // OTP
    private String otp;
    private LocalDateTime otpExpiry;

    // Email Verification
    private String emailVerificationToken;

    // Password Reset
    private String forgotPasswordOtp;
    private LocalDateTime forgotPasswordExpiry;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLocked == null || !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == null || status == Status.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public Role getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public String getOtp() {
        return otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public String getForgotPasswordOtp() {
        return forgotPasswordOtp;
    }

    public LocalDateTime getForgotPasswordExpiry() {
        return forgotPasswordExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }
}
