package com.uet.jobfinder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.file.Path;
import java.sql.Timestamp;
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
    @Value("${spring.datasource.username}")
    private String databaseUser;
    @Value("${database.name}")
    private String databaseName;

    @Scheduled(fixedRate = 10000)
    public void backupDatabase() throws IOException, InterruptedException {
//        System.out.println("Daily message");
        Runtime runtime = Runtime.getRuntime();
        Path backupPath = Path.of(fileStoragePath, "backup",
                String.valueOf(LocalDateTime.now().toString()));
        System.out.println(backupPath.toString());
        String command = String.format("mysqldump -u %s -p%s %s --no-create-info > %s",
                databaseUser, databasePassword,databaseName, backupPath.toString()
        );
        String[] execCommand = {"/bin/sh", "-c", command};
        System.out.println(command);
        Process mysqldump = runtime.exec(execCommand);
//        mysqldump.getInputStream();

        new Thread(new Runnable() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(mysqldump.getInputStream()));
                String line = null;

                try {
                    while ((line = input.readLine()) != null)
                        System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mysqldump.waitFor();
    }


}
