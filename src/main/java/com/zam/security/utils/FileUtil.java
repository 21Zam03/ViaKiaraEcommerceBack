package com.zam.security.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastIndexOfDot = fileName.lastIndexOf('.');

        if (lastIndexOfDot == -1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public static String getFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return originalFilename;
        }
        int dotIndex = originalFilename.lastIndexOf('.');
        return originalFilename.substring(0, dotIndex);
    }

}
