package com.example.userLogin.dto.response;

import com.example.userLogin.entity.ScheduleEntity;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {

    private final String Title;
    private final String Contents;

    public ScheduleResponseDto(ScheduleEntity scheduleEntity) {
        this.Title = scheduleEntity.getTitle();
        this.Contents = scheduleEntity.getContents();
    }

}
