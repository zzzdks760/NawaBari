package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.jwt.JwtBlacklistService;
import com.backend.NawaBari.jwt.JwtService;
import com.backend.NawaBari.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoLogoutService {

    private final MemberRepository memberRepository;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtService jwtService;

    /**
     * isTokenValid 메서드 사용해서 유효성검사 하는 방식
     */
    @Transactional
    public void logout(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        jwtBlacklistService.blacklistToken(member.getRefreshToken());
    }

    /**
     * 직접 토큰 기한 추출해서 하는방식
     */
    public void logout2(Long id, String accessToken) {
        Member member = memberRepository.findOne(id);

        String[] chunks = accessToken.split("\\."); //액세스토큰을 .을기준으로 분리
        Base64.Decoder decoder = Base64.getUrlDecoder(); //디코터 생성

        String payload = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8); //payload 디코딩 하여 문자열로 변환
        try {
            ObjectMapper objectMapper = new ObjectMapper(); //JSON 문자열을 객체로 매핑하기 위한 ObjectMapper를 생성
            //payload JSON 문자열을 Map 으로 변환, TypeReference 를 사용하여 변환 타입을 명시
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

            Long expirationTime = (Long) payloadMap.get("exp"); //exp 필드 추출
            System.out.println("exp 추출 = " + expirationTime);
            long currentTimeInSeconds = System.currentTimeMillis() / 1000; //현재시간을 초 단위로 변환

            if (expirationTime > currentTimeInSeconds) { //만료시간이 현재보다 미래인경우 기간만료, 리프레시토큰 블랙리스트 추가
                String refreshToken = member.getRefreshToken();
                jwtBlacklistService.blacklistToken(refreshToken);
            }
        } catch (JsonProcessingException e) {
            System.out.println("예외발생");
        }
    }
}
