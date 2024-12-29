package com.example.userLogin.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "Title", nullable = false)
    private String Title;

    @Column(name = "Contents", nullable = false)
    private String Contents;

    @Builder
    public ScheduleEntity(String title, String content) {
        this.Title = title;
        this.Contents = content;
    }



}
