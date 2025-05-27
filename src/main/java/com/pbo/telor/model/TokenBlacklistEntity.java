package com.pbo.telor.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Table(name = "token_blacklist")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type; 

    public enum TokenType {
        ACCESS,
        REFRESH
    }
}