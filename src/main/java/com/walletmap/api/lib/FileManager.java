package com.walletmap.api.lib;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager {

    /**
     * Uploads a file to the specified path with a UUID-based name.
     *
     * @param file the file to upload
     * @param path the path to upload the file to
     * @return the uploaded file's path
     * @throws IOException if an error occurs during file upload
     */
    public static String uploadFile(MultipartFile file, String path) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path is empty or null");
        }

        String filepath = System.getProperty("user.dir") + "/src" + path;

        // Ensure the directory exists
        File directory = new File(filepath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Generate a unique file name using UUID and preserve the original file
        // extension
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null && originalFileName.lastIndexOf(".") > -1
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ".jpg"; // Default to ".jpg" if no extension is present

        String uniqueFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
        File destinationFile = new File(directory, uniqueFileName);

        // Save the file to the specified path
        file.transferTo(destinationFile);

        return path + uniqueFileName;
    }

    /**
     * Deletes a file at the specified path.
     *
     * @param filePath the path of the file to delete
     * @return true if the file was successfully deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path is null or empty");
        }

        File file = new File(filePath);
        return file.exists() && file.delete();
    }
}
