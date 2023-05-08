package com.uet.jobfinder.configuration;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.UserRequestLog;
import com.uet.jobfinder.repository.UserRequestLogRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import com.uet.jobfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class LogbookConfiguration {

    @Value("${upload.path}")
    private String serverStoragePath;
    @Autowired
    private UserRequestLogRepository requestLogRepository;
    @Autowired
    private JsonWebTokenProvider jwtProvider;
    @Autowired
    private UserService userService;

    @Bean
    public Logbook logbook() {
        String logFilePath = serverStoragePath + "logbook.txt";
        PrintStream printStream;
        try {
            printStream = new PrintStream(
                    new FileOutputStream(logFilePath)
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (printStream != null) {
            HttpLogFormatter httpLogFormatter = new HttpLogFormatter() {
                @Override
                public String format(Precorrelation precorrelation, HttpRequest request)
                        throws IOException {
                    if (request.getHeaders().get("Authorization") != null) {
                        String tokenHeader = request.getHeaders().get("Authorization").get(0);
                        System.out.println("LOG TOKEN");
                        for (String str : request.getHeaders().get("Authorization")) {
                            System.out.println(str);
                        }
                        String token = jwtProvider.getJWTFromString(tokenHeader);
                        Long userId = Long.getLong(jwtProvider.getUserIdFromJWT(token));
                        User user = userService.getUserById(userId);
                        if (user != null) {
                            UserRequestLog userRequestLog =
                                    UserRequestLog
                                            .builder()
                                            .method(request.getMethod())
                                            .url(request.getPath())
                                            .requestBody(request.getBodyAsString())
                                            .requestDetail(request.toString())
                                            .user(user)
                                            .localDateTime(LocalDateTime.now())
                                            .build();
                            requestLogRepository.save(userRequestLog);
                        }
                    }
                    return new DefaultHttpLogFormatter().format(
                            precorrelation, request
                    );
                }

                @Override
                public String format(Correlation correlation, HttpResponse response) throws IOException {
                    return new DefaultHttpLogFormatter().format(correlation, response);
                }
            };

            Logbook logbook = Logbook.builder()
                    .sink(new DefaultSink(
                            httpLogFormatter,
            new StreamHttpLogWriter(printStream)
                    )).build();
            return logbook;
        }
        return Logbook.builder().build();
    }

}
