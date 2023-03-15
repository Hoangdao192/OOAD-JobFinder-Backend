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
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.path}")
    private String fileStoragePath;

    @Autowired
    private FileRepository fileRepository;

    public AppFile saveFile(String fileOriginName, String fileType, byte[] bytes) {
        String fileName = UUID.randomUUID().toString();
        File file = new File(fileStoragePath + "/" + fileName);
        try {
            FileCopyUtils.copy(bytes, file);
        } catch (Exception e) {
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

}
