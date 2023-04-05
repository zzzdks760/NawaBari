package com.backend.NawaBari.domain.category;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("K")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Korean extends Category{

    private String korean_img;
}
