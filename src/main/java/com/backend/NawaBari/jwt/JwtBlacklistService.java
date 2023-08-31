package com.backend.NawaBari.jwt;

import com.backend.NawaBari.domain.BlacklistToken;
import com.backend.NawaBari.repository.BlacklistTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final BlacklistTokenRepository blacklistTokenRepository;


    //블랙리스트 체크
    public boolean isTokenBlacklist(String token) {
        return blacklistTokenRepository.existsByToken(token);
    }

    //블랙리스트 추가
    @Transactional
    public void blacklistToken(String refreshToken) {
        BlacklistToken blacklistToken = BlacklistToken.addBlacklist(refreshToken);
        blacklistTokenRepository.save(blacklistToken);
    }
}
