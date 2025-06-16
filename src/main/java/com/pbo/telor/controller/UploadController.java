package com.pbo.telor.controller;

import com.pbo.telor.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Upload multiple files to a folder",
               description = "Endpoint untuk mengupload beberapa file ke folder tertentu")
    @PostMapping(path = "/{folder}", consumes = "multipart/form-data")
    public ResponseEntity<List<String>> uploadMultipleFiles(
            @Parameter(description = "Nama folder tujuan", required = true)
            @PathVariable("folder") String folder,

            @Parameter(description = "Daftar file yang akan diupload", required = true)
            @RequestPart("file") MultipartFile[] files) {

        List<String> uploadedUrls = uploadService.saveFiles(folder, files);
        return ResponseEntity.ok(uploadedUrls);
    }
}
