package com.backend.NawaBari.domain.category;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("W")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Western extends Category{
    private String western_img;
}
