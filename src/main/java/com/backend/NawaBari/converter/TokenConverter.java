package com.backend.NawaBari.converter;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Token;
import com.backend.NawaBari.dto.TokenDTO;
import com.backend.NawaBari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class TokenConverter {

    private final MemberRepository memberRepository;

    public Token toEntity(TokenDTO tokenDTO) {
        Member member = memberRepository.findOne(tokenDTO.getMember_id());

        return Token.builder()
                .access_token(tokenDTO.getAccess_token())
                .member(member)
                .build();
    }

    public TokenDTO toDTO(Token token) {
        return TokenDTO.builder()
                .access_token(token.getAccess_token())
                .member_id(token.getMember().getId())
                .build();
    }
}
