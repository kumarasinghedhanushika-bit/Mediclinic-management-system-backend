package com.medical.clinic.security.oauth2;

import java.util.Map;

public class OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getFirstName() {
        String name = (String) attributes.get("given_name");
        return name != null ? name : "";
    }

    public String getLastName() {
        String name = (String) attributes.get("family_name");
        return name != null ? name : "";
    }

    public String getAvatar() {
        return (String) attributes.get("picture");
    }
}