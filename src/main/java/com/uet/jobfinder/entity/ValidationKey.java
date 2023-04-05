package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;


@Table(name = "validation_key")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String validationKey;
    private LocalDateTime createDate;
    private LocalDateTime expirationDate;
    private boolean activated = false;
//    @ManyToOne(cascade = CascadeType.REFRESH)
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public ValidationKey(
            @NotNull String validationKey, @NotNull LocalDateTime createDate,
            @NotNull LocalDateTime expirationDate, @NotNull User user) {
        this.validationKey = validationKey;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}
