package com.backend.NawaBari.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private Long id;
    private String access_token;
    private Long member_id;
}
