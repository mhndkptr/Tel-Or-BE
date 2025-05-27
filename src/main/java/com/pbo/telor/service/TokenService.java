package com.pbo.telor.service;

import com.pbo.telor.model.TokenBlacklistEntity;
import com.pbo.telor.model.TokenBlacklistEntity.TokenType;
import com.pbo.telor.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private TokenBlacklistRepository blacklistRepo;

    public void blacklistToken(String token, TokenType type) {
        if (!blacklistRepo.existsByToken(token)) {
            TokenBlacklistEntity entity = TokenBlacklistEntity.builder()
                    .token(token)
                    .type(type)
                    .build();
            blacklistRepo.save(entity);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistRepo.existsByToken(token);
    }
}