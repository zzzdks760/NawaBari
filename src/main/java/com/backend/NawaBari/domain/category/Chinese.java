package com.backend.NawaBari.domain.category;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("C")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chinese extends Category{

    private String chinese_img;
}
