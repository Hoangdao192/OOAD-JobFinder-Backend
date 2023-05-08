package com.uet.jobfinder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
@Configuration
public class DatabaseBackupService {

    //  Time in milliseconds
    //  30 days is default
    private static final long BACKUP_TIME = 2592000000L;
    @Value("${upload.path}")
    private String fileStoragePath;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Scheduled(fixedRate = 5000)
    public void backupDatabase() {
//        System.out.println("Daily message");
        Runtime runtime = Runtime.getRuntime();
        Path backupPath = Path.of(fileStoragePath, "backup", LocalDateTime.now().toString());
        System.out.println(backupPath.toString());
//        Process mysqldump = runtime.exec(
//                String.format("mysqldump -u root -p%s ")
//        )
    }


}
