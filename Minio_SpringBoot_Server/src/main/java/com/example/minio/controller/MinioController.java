package com.example.minio.controller;

import com.example.minio.service.MinioService;
import com.example.minio.util.FileTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/minio")
@RestController
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file, String bucketName) {
        String fileType = FileTypeUtils.getFileType(file);
        if (fileType != null) {
            return minioService.putObject(file, bucketName, fileType);
        }
        return "不支持的文件格式。请确认格式,重新上传！！！";
    }

    @PostMapping("/upload1")
    public String uploadFileToMinio(MultipartFile file, Integer userId) {
        String fileType = FileTypeUtils.getFileType(file);
        if (fileType != null) {
//            return minioService.putObject(file, userId, fileType);
        }
        return "不支持的文件格式。请确认格式,重新上传！！！";
    }
}
