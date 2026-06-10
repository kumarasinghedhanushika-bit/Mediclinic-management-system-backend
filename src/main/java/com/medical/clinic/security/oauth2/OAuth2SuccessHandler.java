package com.medical.clinic.security.oauth2;

import com.medical.clinic.model.User;
import com.medical.clinic.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String token = jwtUtil.generateToken(user);

        // ← Add this log to confirm the value is loaded
        System.out.println(">>> OAuth2 redirect URI: " + redirectUri);
        System.out.println(">>> Generated token: " + token.substring(0, 20) + "...");

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .queryParam("email", user.getEmail())
                .queryParam("role", user.getRole().name())
                .build().toUriString();

        System.out.println(">>> Redirecting to: " + targetUrl);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}