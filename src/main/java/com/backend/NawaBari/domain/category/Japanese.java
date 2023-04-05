package com.backend.NawaBari.domain.category;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("J")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Japanese extends Category{

    private String japanese_img;
}
