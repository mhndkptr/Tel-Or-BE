package com.pbo.telor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadService {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/uploads/";
    private static final String BASE_URL = "/assets/";

    public List<String> saveFiles(String folder, MultipartFile[] files) {
        List<String> uploadedUrls = new ArrayList<>();
        String fullPath = BASE_PATH + folder;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile file : files) {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(dir, filename);

            try {
                file.transferTo(destination);
                uploadedUrls.add(BASE_URL + folder + "/" + filename);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + filename, e);
            }
        }

        return uploadedUrls;
    }

    public String saveFile(String folder, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fullPath = BASE_PATH + folder;
        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destination = new File(dir, filename);

        try {
            file.transferTo(destination);
            return BASE_URL + folder + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + filename, e);
        }
    }

}
