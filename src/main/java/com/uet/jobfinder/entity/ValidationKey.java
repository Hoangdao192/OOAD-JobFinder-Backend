package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Table(name = "validation_key")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long validationKey;
    private LocalDateTime createDate;
    private LocalDateTime expirationDate;
    private boolean activated = false;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false)
    private User user;

    public ValidationKey(Long validationKey, LocalDateTime createDate, LocalDateTime expirationDate, User user) {
        this.validationKey = validationKey;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}
