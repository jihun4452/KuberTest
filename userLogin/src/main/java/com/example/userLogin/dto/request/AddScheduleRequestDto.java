package com.example.userLogin.dto.request;

import com.example.userLogin.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddScheduleRequestDto {
    private String Title;

    private String Contents;

    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
                .title(Title)
                .content(Contents)
                .build();
    }
}
