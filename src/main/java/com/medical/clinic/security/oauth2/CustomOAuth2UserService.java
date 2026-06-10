package com.medical.clinic.security.oauth2;

import com.medical.clinic.enums.Role;
import com.medical.clinic.enums.Status;
import com.medical.clinic.model.User;
import com.medical.clinic.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = new OAuth2UserInfo(oAuth2User.getAttributes());

        User user = userRepository.findByEmail(userInfo.getEmail())
                .map(existing -> updateExistingUser(existing, userInfo))
                .orElseGet(() -> createNewUser(userInfo));

        userRepository.save(user);

        return new CustomOAuth2User(oAuth2User, user);
    }

    private User createNewUser(OAuth2UserInfo userInfo) {
        return User.builder()
                .email(userInfo.getEmail())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .avatar(userInfo.getAvatar())
                .role(Role.PATIENT)
                .status(Status.ACTIVE)
                .emailVerified(true)           // Google already verified it
                .accountLocked(false)
                .passwordChangeRequired(false)
                .passwordResetVerified(false)
                .build();
    }

    private User updateExistingUser(User user, OAuth2UserInfo userInfo) {
        // Only update avatar if they don't have a custom one
        if (user.getAvatar() == null || user.getAvatar().isBlank()) {
            user = User.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .password(user.getPassword())
                    .phone(user.getPhone())
                    .gender(user.getGender())
                    .avatar(userInfo.getAvatar())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .emailVerified(true)
                    .accountLocked(user.getAccountLocked())
                    .refreshToken(user.getRefreshToken())
                    .refreshTokenExpiry(user.getRefreshTokenExpiry())
                    .lastLogin(user.getLastLogin())
                    .failedLoginAttempts(user.getFailedLoginAttempts())
                    .passwordChangeRequired(user.getPasswordChangeRequired())
                    .passwordResetVerified(user.getPasswordResetVerified())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
        return user;
    }
}