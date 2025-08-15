package com.dbiz.app.integrationservice.file.service.impl;


import com.dbiz.app.integrationservice.file.service.FileStorageService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.exception.PosException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {


    private final static String FILE_LOCATION = "upload/file";

    @Override
    public Path storeFile(MultipartFile file, String fileName) {

        try {
            // Chuẩn hóa tên file
            String cleanFileName = fileName.replaceAll("\\s+", "_");

            String location = FILE_LOCATION + "/" + AuditContext.getAuditInfo().getMainTenantId();
            // Đường dẫn thư mục lưu file (ví dụ thư mục "uploads")
            Path uploadDir = Paths.get(location).toAbsolutePath().normalize();

            // 🔍 Kiểm tra và tạo folder nếu chưa tồn tại
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Đường dẫn file đầy đủ
            Path targetLocation = uploadDir.resolve(cleanFileName);

            // Ghi file vào đĩa
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation;
        } catch (IOException ex) {
            throw new PosException(ex.getMessage());
        }


    }

    @Override
    public String deleteFile(String fileName) {
        try {

            // Đường dẫn thư mục lưu file (ví dụ thư mục "uploads")
            Path uploadDir = Paths.get(FILE_LOCATION + "/" + AuditContext.getAuditInfo().getMainTenantId()).toAbsolutePath().normalize();

            String cleanFileName = fileName.replaceAll("\\s+", "_");

            // Đường dẫn file đầy đủ
            Path targetLocation = uploadDir.resolve(cleanFileName);

            // Xóa file
            Files.deleteIfExists(targetLocation);
            return "COM";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "FAI";
        }
    }
}
