package com.medical.clinic.service;

import com.medical.clinic.enums.CloudinaryFolder;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    String uploadImage(MultipartFile file, CloudinaryFolder folder);

    String uploadPdf(MultipartFile file, CloudinaryFolder folder);

    String uploadFile(MultipartFile file, CloudinaryFolder folder);

    void deleteFile(String fileUrl);

    @Deprecated
    default String uploadImage(MultipartFile file) {
        return uploadImage(file, CloudinaryFolder.PROFILE_IMAGES);
    }

    @Deprecated
    default void deleteImage(String imageUrl) {
        deleteFile(imageUrl);
    }
}
