package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_request_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String method;
    private String requestDetail;
    private String requestBody;
    @ManyToOne
    private User user;
    private LocalDateTime localDateTime;
}
