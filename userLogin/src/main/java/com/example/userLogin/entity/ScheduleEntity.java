package com.example.userLogin.entity;
import com.example.userLogin.dto.request.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "startDate", nullable = false)    // 시작 날짜
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)    // 종료 날짜
    private LocalDate endDate;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "startTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "endTime", nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "scheduleEntity" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userEntity_id", nullable = false)
//    private UserEntity userEntity;

    @Builder
    public ScheduleEntity(LocalDate startDate, LocalDate endDate, String title, String content, LocalTime startTime, LocalTime endTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.contents = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void updateSchedule(ScheduleRequestDto dto) {
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
    }



}
