package com.medical.clinic.ServiceImplimentation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.medical.clinic.enums.CloudinaryFolder;
import com.medical.clinic.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final long MAX_IMAGE_BYTES = 5 * 1024 * 1024;
    private static final long MAX_PDF_BYTES = 10 * 1024 * 1024;

    private static final Set<String> IMAGE_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );
    private static final Set<String> PDF_TYPES = Set.of("application/pdf");

    private final Cloudinary cloudinary;

    @Value("${cloudinary.max-image-mb:5}")
    private int maxImageMb;

    @Value("${cloudinary.max-pdf-mb:10}")
    private int maxPdfMb;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file, CloudinaryFolder folder) {
        validateFile(file, IMAGE_TYPES, maxImageMb * 1024L * 1024L, "image");
        return upload(file, folder, "image");
    }

    @Override
    public String uploadPdf(MultipartFile file, CloudinaryFolder folder) {
        validateFile(file, PDF_TYPES, maxPdfMb * 1024L * 1024L, "PDF");
        return upload(file, folder, "raw");
    }

    @Override
    public String uploadFile(MultipartFile file, CloudinaryFolder folder) {
        String contentType = file.getContentType() != null ? file.getContentType() : "";
        if (PDF_TYPES.contains(contentType)) {
            return uploadPdf(file, folder);
        }
        return uploadImage(file, folder);
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String publicId = extractPublicId(fileUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("File delete failed: " + e.getMessage());
        }
    }

    private String upload(MultipartFile file, CloudinaryFolder folder, String resourceType) {
        try {
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder.getPath(),
                    "resource_type", resourceType
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    private void validateFile(
            MultipartFile file,
            Set<String> allowedTypes,
            long maxBytes,
            String label
    ) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required");
        }
        if (file.getSize() > maxBytes) {
            throw new RuntimeException(label + " file exceeds maximum size of " + (maxBytes / 1024 / 1024) + " MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new RuntimeException("Invalid " + label + " file type: " + contentType);
        }
    }

    private String extractPublicId(String fileUrl) {
        try {
            int uploadIndex = fileUrl.indexOf("/upload/");
            if (uploadIndex < 0) {
                throw new RuntimeException("Invalid Cloudinary URL");
            }
            String path = fileUrl.substring(uploadIndex + "/upload/".length());
            if (path.startsWith("v")) {
                int slash = path.indexOf('/');
                if (slash > 0) {
                    path = path.substring(slash + 1);
                }
            }
            int dot = path.lastIndexOf('.');
            if (dot > 0) {
                path = path.substring(0, dot);
            }
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Could not parse Cloudinary public id from URL");
        }
    }
}
