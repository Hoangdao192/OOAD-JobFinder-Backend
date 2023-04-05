package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.AppFile;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.path}")
    private String fileStoragePath;
    @Value("${domain.url}")
    private String domainUrl;

    @Autowired
    private FileRepository fileRepository;

    public AppFile saveFile(String fileOriginName, String fileType, byte[] bytes) {
        String fileName = UUID.randomUUID().toString();
        File file = new File(fileStoragePath + "/" + fileName);
        try {
            FileCopyUtils.copy(bytes, file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomIllegalArgumentException(
                    ServerError.SERVER_ERROR
            );
        }

        AppFile appFile = new AppFile();
        appFile.setFileName(fileOriginName);
        appFile.setFileType(fileType);
        appFile.setFilePath(file.getPath());
        return fileRepository.save(appFile);
    }

    public byte[] getImageFile(Long id) {
        AppFile appFile = getFileById(id);
        if (!isImageFile(appFile)) {
            throw new CustomIllegalArgumentException(
                    ServerError.INVALID_FILE_TYPE
            );
        }

        File file = new File(appFile.getFilePath());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = fileInputStream.readAllBytes();
            fileInputStream.close();
            return bytes;
        } catch (IOException ioException) {
            throw new CustomIllegalArgumentException(ServerError.FILE_NOT_EXISTS);
        }
    }

    public byte[] getPdfFile(Long id) {
        AppFile appFile = getFileById(id);
        if (!isPdfFile(appFile)) {
            throw new CustomIllegalArgumentException(
                    ServerError.INVALID_FILE_TYPE
            );
        }

        File file = new File(appFile.getFilePath());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = fileInputStream.readAllBytes();
            fileInputStream.close();
            return bytes;
        } catch (IOException ioException) {
            throw new CustomIllegalArgumentException(ServerError.FILE_NOT_EXISTS);
        }
    }

    public String generateFileUrl(Long id) {
        AppFile appFile = getFileById(id);
        if (isPdfFile(appFile)) {
            return domainUrl + "api/file/pdf/" + id;
        }

        if (isImageFile(appFile)) {
            return domainUrl + "api/file/image/" + id;
        }
        return "";
    }

    public boolean isImageFile(AppFile appFile) {
        return true;
    }


    public boolean isPdfFile(AppFile appFile) {
        return appFile.getFileType().equals("application/pdf");
    }

    public AppFile getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() ->
                    new CustomIllegalArgumentException(
                            ServerError.FILE_NOT_EXISTS
                    ));
    }


}
