package com.backend.NawaBari.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass //매핑정보만 상속받을떄
@EntityListeners(AuditingEntityListener.class) //시간에 대해서 자동으로 값을 넣어주는 기능
@Getter

public class Base {
    @CreationTimestamp //생성시 시간정보를 줌
    @Column(updatable = false) //수정시 관여x
    private LocalDateTime createTime;

    @UpdateTimestamp //업데이트시 시간정보 줌
    @Column(insertable = false) //입력시 관여x
    private LocalDateTime updateTime;


    public String getFormattedTime() {
        if (updateTime != null) {
            return updateTime.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
        }
        return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
