package com.pbo.telor.controller;

import com.pbo.telor.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/{folder}")
    public ResponseEntity<List<String>> uploadMultipleFiles(
            @PathVariable String folder,
            @RequestParam("file") MultipartFile[] files) {

        List<String> uploadedUrls = uploadService.saveFiles(folder, files);
        return ResponseEntity.ok(uploadedUrls);
    }
}
