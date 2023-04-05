package com.backend.NawaBari.domain.category;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("E")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Etc extends Category{

    private String etc_img;
}
