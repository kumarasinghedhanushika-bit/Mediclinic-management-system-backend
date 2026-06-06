package com.medical.clinic.enums;

public enum CloudinaryFolder {
    PROFILE_IMAGES("medical-system/profile-images"),
    REPORTS("medical-system/reports"),
    PRESCRIPTIONS("medical-system/prescriptions"),
    LAB_REPORTS("medical-system/lab-reports");

    private final String path;

    CloudinaryFolder(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
